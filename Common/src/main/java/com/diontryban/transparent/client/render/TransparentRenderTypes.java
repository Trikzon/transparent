/*
 * This file is part of Transparent. A copy of this program can be found at
 * https://github.com/Trikzon/transparent.
 * Copyright (C) 2023 Dion Tryban
 *
 * Transparent is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Transparent is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Transparent. If not, see <https://www.gnu.org/licenses/>.
 */

package com.diontryban.transparent.client.render;

import com.diontryban.ash.api.modloader.ModLoader;
import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.mixin.client.RenderTypeAccessor;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

// Creates versions of vanilla RenderTypes but with transparency enabled.
// This *should* allow for vanilla shaders to work the same.
public class TransparentRenderTypes extends RenderStateShard {
    // To access protected fields in RenderStateShard.
    private TransparentRenderTypes() {
        super(null, null, null);
    }

    private static final Function<ResourceLocation, RenderType> ENTITY_SOLID = Util.memoize((textureLoc) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                .setTextureState(new TextureStateShard(textureLoc, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderTypeAccessor.callCreate("entity_solid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    });

    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize((textureLoc) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
                .setTextureState(new TextureStateShard(textureLoc, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderTypeAccessor.callCreate("entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    });

    public static RenderType entitySolid(ResourceLocation textureLoc) {
        return ENTITY_SOLID.apply(textureLoc);
    }

    public static RenderType entityCutoutNoCull(ResourceLocation textureLoc) {
        return ENTITY_CUTOUT_NO_CULL.apply(textureLoc);
    }

    private static Map<String, Field> CACHED_FIELDS = null;
     // Tries to get the texture from a RenderType using reflection.
     // This could be done with Access Wideners and Mixins, but that is more
     // complicated due to private/protected subclasses.
     //
     // When updating, check that the following classes and fields still exist:
     // - RenderType$CompositeRenderType#state -> RenderType$CompositeState
     // - RenderType$CompositeState#textureState -> RenderStateShard$EmptyTextureStateShard
     // - RenderStateShard$TextureStateShard#texture -> Optional<ResourceLocation>
    public static ResourceLocation getTexture(RenderType renderType, ResourceLocation defaultTexture) {
        if (renderType instanceof RenderType.CompositeRenderType compositeRenderType) {
            if (CACHED_FIELDS == null) {
                CACHED_FIELDS = getFields(compositeRenderType);
                if (CACHED_FIELDS == null) {
                    return defaultTexture;
                }
            }

            try {
                Object state = CACHED_FIELDS.get("state").get(compositeRenderType);
                Object textureState = CACHED_FIELDS.get("textureState").get(state);
                Object texture = CACHED_FIELDS.get("texture").get(textureState);

                if (texture instanceof Optional<?> optionalTexture) {
                    if (optionalTexture.isPresent()) {
                        Object textureLoc = optionalTexture.get();
                        if (textureLoc instanceof ResourceLocation) {
                            return (ResourceLocation) textureLoc;
                        }
                    }
                }
            } catch (IllegalAccessException | NullPointerException e) {
                return defaultTexture;
            }
        }
        return defaultTexture;
    }

    private static Map<String, Field> getFields(RenderType.CompositeRenderType renderType) {
        final Map<String, String> FIELD_NAMES = ImmutableMap.of(
                "forge:state", "f_110511_", "fabric:state", "field_21403", "quilt:state", "f_gksitatb",
                "forge:textureState", "f_110576_", "fabric:textureState", "field_21406", "quilt:textureState", "f_dydjlbjf",
                "forge:texture", "f_110328_", "fabric:texture", "field_21397", "quilt:texture", "f_hsolgeid"
        );
        final boolean isDev = ModLoader.isDevelopmentEnvironment();
        final String modLoader = ModLoader.getName();

        try {
            Map<String, Field> result = new HashMap<>();

            String fieldName = isDev ? "state" : FIELD_NAMES.get(modLoader + ":state");
            Field field = renderType.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result.put("state", field);

            Object o = field.get(renderType);
            if (o instanceof RenderType.CompositeState state) {
                fieldName = isDev ? "textureState" : FIELD_NAMES.get(modLoader + ":textureState");
                field = state.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                result.put("textureState", field);

                Object textureState = field.get(state);

                fieldName = isDev ? "texture" : FIELD_NAMES.get(modLoader + ":texture");
                field = textureState.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                result.put("texture", field);

                return result;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Transparent.LOG.warn("Failed to use reflection to get texture field. Please report on Transparent mod's GitHub.");
            Transparent.LOG.warn(e.toString());
        }
        return null;
    }
}
