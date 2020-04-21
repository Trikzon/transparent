/* ===========================================================================
 * Copyright 2020 Trikzon
 *
 * Transparent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * File: BlockMixin.java
 * Date: 2020-04-20 "1.15.2-1.0.0"
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.transparent.mixin;

import io.github.trikzon.transparent.ISetTransparent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin implements ISetTransparent
{
    @Mutable
    @Shadow @Final private boolean opaque;

    @Shadow public abstract BlockState getDefaultState();

    @Mutable
    @Shadow @Final protected Material material;

    private Boolean defaultOpaque = null;
    private boolean isGlass = false;

    // Determines how much shadow. 0.0f being dark, 1.0f being light
    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("RETURN"), cancellable = true)
    private void transparent_getAmbientOcclusionLightLevel(
            BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Float> cir)
    {
        if (defaultOpaque != null && !opaque)
        {
            cir.setReturnValue(1.0f);
        }
    }

    // Determines how far the light level decreases per block it goes through
    // Always set to 0 here because I don't see much use in this being configurable
    // Could be added in the future though, if so make it so it can return 1 for leaves
    @Inject(method = "getOpacity", at = @At("RETURN"), cancellable = true)
    private void transparent_getOpacity(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        if (defaultOpaque != null && !opaque)
        {
            cir.setReturnValue(0);
        }
    }

    // Determines whether blocks will cull their face if they are the same block. Similar to glass
    @Inject(method = "isSideInvisible", at = @At("RETURN"), cancellable = true)
    private void transparent_isSideInvisible(
            BlockState state, BlockState neighbor, Direction facing, CallbackInfoReturnable<Boolean> cir)
    {
        if (defaultOpaque != null && isGlass)
        {
            cir.setReturnValue(neighbor.getBlock() == state.getBlock());
        }
    }

    @Override
    public void setTransparent(boolean transparent, boolean isGlass)
    {
        if (defaultOpaque == null)
        {
            defaultOpaque = this.opaque;
        }
        this.opaque = !transparent;
        this.material = Material.GLASS;
        this.isGlass = isGlass;
        this.getDefaultState().initShapeCache();
    }

    @Override
    public void reset()
    {
        this.opaque = defaultOpaque;
        this.defaultOpaque = null;
        this.getDefaultState().initShapeCache();
    }
}
