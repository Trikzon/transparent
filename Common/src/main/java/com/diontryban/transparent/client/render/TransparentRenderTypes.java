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

import com.diontryban.transparent.mixin.client.accessor.CompositeRenderTypeAccessor;
import com.diontryban.transparent.mixin.client.accessor.CompositeStateAccessor;
import com.diontryban.transparent.mixin.client.accessor.RenderTypeAccessor;
import com.diontryban.transparent.mixin.client.accessor.TextureStateShardAccessor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

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

    // Tries to get the texture from a RenderType by using mixin accessors and access widener hacky-ness.
    // Used to get the texture of other mods that create RenderTypes that we want to replace.
    public static ResourceLocation getTexture(RenderType renderType, ResourceLocation defaultTexture) {
        if (renderType instanceof RenderType.CompositeRenderType compositeRenderType) {
            RenderType.CompositeState state = ((CompositeRenderTypeAccessor)(Object)compositeRenderType).getState();
            EmptyTextureStateShard textureState = ((CompositeStateAccessor)(Object)state).getTextureState();
            if (textureState instanceof TextureStateShard textureStateShard) {
                Optional<ResourceLocation> optTextureLoc = ((TextureStateShardAccessor)textureStateShard).getTexture();
                if (optTextureLoc.isPresent()) {
                    return optTextureLoc.get();
                }
            }
        }
        return defaultTexture;
    }
}
