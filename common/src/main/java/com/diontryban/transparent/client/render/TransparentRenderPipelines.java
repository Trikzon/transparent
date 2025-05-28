/*
 * This file is part of Transparent. A copy of this program can be found at
 * https://github.com/Trikzon/transparent.
 * Copyright (C) 2025 Dion Tryban
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

import com.diontryban.transparent.mixin.client.accessor.RenderPipelinesAccessor;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;

// Creates versions of vanilla RenderPipelines but with transparency enabled.
// This *should* allow for vanilla shaders to work the same.
// If it doesn't, please create an issue on GitHub.
public class TransparentRenderPipelines {
    public static final RenderPipeline ARMOR_CUTOUT_NO_CULL = RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet())
            .withLocation("pipeline/armor_cutout_no_cull")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withShaderDefine("NO_OVERLAY")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();

    public static final RenderPipeline ENTITY_SOLID = RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet())
            .withLocation("pipeline/entity_solid")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withSampler("Sampler1")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();

    public static final RenderPipeline ENTITY_SOLID_Z_OFFSET_FORWARD = RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet())
            .withLocation("pipeline/entity_solid_offset_forward")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withSampler("Sampler1")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();

    public static final RenderPipeline ENTITY_CUTOUT_NO_CULL = RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet())
            .withLocation("pipeline/entity_cutout_no_cull")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withSampler("Sampler1")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();

    public static final RenderPipeline ENTITY_SMOOTH_CUTOUT = RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet())
            .withLocation("pipeline/entity_smooth_cutout")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withSampler("Sampler1")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();
}
