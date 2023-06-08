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

package com.diontryban.transparent.mixin.client.accessor;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderType.CompositeState.class)
public interface CompositeStateAccessor {
    @Accessor
    RenderStateShard.EmptyTextureStateShard getTextureState();
}
