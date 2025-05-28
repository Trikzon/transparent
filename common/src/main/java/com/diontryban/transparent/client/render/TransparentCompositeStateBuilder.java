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

import com.diontryban.transparent.mixin.client.accessor.CompositeStateAccessor;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

// A copy of vanilla's CompositeStateBuilder because all of its functions are protected.
public class TransparentCompositeStateBuilder extends RenderStateShard {
    private RenderStateShard.EmptyTextureStateShard textureState;
    private RenderStateShard.LightmapStateShard lightmapState;
    private RenderStateShard.OverlayStateShard overlayState;
    private RenderStateShard.LayeringStateShard layeringState;
    private RenderStateShard.OutputStateShard outputState;
    private RenderStateShard.TexturingStateShard texturingState;
    private RenderStateShard.LineStateShard lineState;

    public TransparentCompositeStateBuilder() {
        super(null, null, null);

        this.textureState = RenderStateShard.NO_TEXTURE;
        this.lightmapState = RenderStateShard.NO_LIGHTMAP;
        this.overlayState = RenderStateShard.NO_OVERLAY;
        this.layeringState = RenderStateShard.NO_LAYERING;
        this.outputState = RenderStateShard.MAIN_TARGET;
        this.texturingState = RenderStateShard.DEFAULT_TEXTURING;
        this.lineState = RenderStateShard.DEFAULT_LINE;
    }

    public TransparentCompositeStateBuilder setTextureState(RenderStateShard.EmptyTextureStateShard textureState) {
        this.textureState = textureState;
        return this;
    }

    public TransparentCompositeStateBuilder setLightmapState(RenderStateShard.LightmapStateShard lightmapState) {
        this.lightmapState = lightmapState;
        return this;
    }

    public TransparentCompositeStateBuilder setOverlayState(RenderStateShard.OverlayStateShard overlayState) {
        this.overlayState = overlayState;
        return this;
    }

    public TransparentCompositeStateBuilder setLayeringState(RenderStateShard.LayeringStateShard layerState) {
        this.layeringState = layerState;
        return this;
    }

    public TransparentCompositeStateBuilder setOutputState(RenderStateShard.OutputStateShard outputState) {
        this.outputState = outputState;
        return this;
    }

    public TransparentCompositeStateBuilder setTexturingState(RenderStateShard.TexturingStateShard texturingState) {
        this.texturingState = texturingState;
        return this;
    }

    public TransparentCompositeStateBuilder setLineState(RenderStateShard.LineStateShard lineState) {
        this.lineState = lineState;
        return this;
    }

    public RenderType.CompositeState createCompositeState(boolean outline) {
        return this.createCompositeState(outline ? RenderType.OutlineProperty.AFFECTS_OUTLINE : RenderType.OutlineProperty.NONE);
    }

    public RenderType.CompositeState createCompositeState(RenderType.OutlineProperty outlineState) {
        return CompositeStateAccessor.callConstructor(this.textureState, this.lightmapState, this.overlayState, this.layeringState, this.outputState, this.texturingState, this.lineState, outlineState);
    }
}
