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

package com.diontryban.transparent.mixin.client;

import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.client.render.TransparentRenderTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderDragonRenderer.class)
public abstract class EnderDragonRendererMixin extends EntityRenderer<EnderDragon, EnderDragonRenderState> {
    @Shadow @Final public static ResourceLocation CRYSTAL_BEAM_LOCATION;

    protected EnderDragonRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapOperation(method = "renderCrystalBeams", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private static VertexConsumer wrapGetBufferInRenderCrystalBeams(
            MultiBufferSource instance,
            RenderType renderType,
            Operation<VertexConsumer> original
    ) {
        return Transparent.CONFIG.endCrystalBeam ?
                instance.getBuffer(TransparentRenderTypes.entitySmoothCutout(
                        TransparentRenderTypes.getTexture(renderType, CRYSTAL_BEAM_LOCATION))
                ) :
                original.call(instance, renderType);
    }
}
