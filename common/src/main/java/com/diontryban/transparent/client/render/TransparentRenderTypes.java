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

import com.diontryban.transparent.mixin.client.accessor.*;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

// Creates versions of vanilla RenderTypes but with transparency enabled.
// This *should* allow for vanilla shaders to work the same.
// If it doesn't, please create an issue on GitHub.
public class TransparentRenderTypes extends RenderStateShard {
    // To access protected fields in RenderStateShard.
    private TransparentRenderTypes() {
        super(null, null, null);
    }

    private static final Function<ResourceLocation, RenderType> ARMOR_CUTOUT_NO_CULL = Util.memoize(
            (location) -> RenderTypeAccessor.callCreate(
                    "armor_cutout_no_cull", 1536, true, true,
                    TransparentRenderPipelines.ARMOR_CUTOUT_NO_CULL,
                    new TransparentCompositeStateBuilder()
                            .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                            .createCompositeState(true)
            )
    );

    private static final Function<ResourceLocation, RenderType> ENTITY_SOLID = Util.memoize(
            (location) -> RenderTypeAccessor.callCreate(
                    "entity_solid", 1536, true, true,
                    TransparentRenderPipelines.ENTITY_SOLID,
                    new TransparentCompositeStateBuilder()
                            .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .createCompositeState(true)
            )
    );

    private static final Function<ResourceLocation, RenderType> ENTITY_SOLID_Z_OFFSET_FORWARD = Util.memoize(
            (location) -> RenderTypeAccessor.callCreate(
                    "entity_solid_z_offset_forward", 1536, true, false,
                    TransparentRenderPipelines.ENTITY_SOLID_Z_OFFSET_FORWARD,
                    new TransparentCompositeStateBuilder()
                            .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .setLayeringState(VIEW_OFFSET_Z_LAYERING_FORWARD)
                            .createCompositeState(true)
            )
    );

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize(
            (location, outline) -> RenderTypeAccessor.callCreate(
                    "entity_cutout_no_cull", 1536, true, true,
                    TransparentRenderPipelines.ENTITY_CUTOUT_NO_CULL,
                    new TransparentCompositeStateBuilder()
                            .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .createCompositeState(outline)
            )
    );

    private static final Function<ResourceLocation, RenderType> ENTITY_SMOOTH_CUTOUT = Util.memoize(
            (location) -> RenderTypeAccessor.callCreate(
                    "entity_smooth_cutout", 1536, false, true,
                    TransparentRenderPipelines.ENTITY_SMOOTH_CUTOUT,
                    new TransparentCompositeStateBuilder()
                            .setTextureState(new TextureStateShard(location, TriState.FALSE, false))
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .createCompositeState(true)
            )
    );

    public static RenderType armorCutoutNoCull(ResourceLocation location) {
        return ARMOR_CUTOUT_NO_CULL.apply(location);
    }

    public static RenderType entitySolid(ResourceLocation location) {
        return ENTITY_SOLID.apply(location);
    }

    public static RenderType entitySolidZOffsetForward(ResourceLocation location) {
        return ENTITY_SOLID_Z_OFFSET_FORWARD.apply(location);
    }

    public static RenderType entityCutoutNoCull(ResourceLocation location, boolean outline) {
        return ENTITY_CUTOUT_NO_CULL.apply(location, outline);
    }

    public static RenderType entityCutoutNoCull(ResourceLocation location) {
        return entityCutoutNoCull(location, true);
    }

    public static RenderType entitySmoothCutout(ResourceLocation location) {
        return ENTITY_SMOOTH_CUTOUT.apply(location);
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
