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

package com.diontryban.transparent.mixin.client;

import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.client.TransparentClient;
import com.diontryban.transparent.client.render.TransparentRenderTypes;
import com.diontryban.transparent.mixin.client.accessor.TextureAtlasHolderAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.entity.state.PaintingRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting, PaintingRenderState> {
    protected PaintingRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/client/renderer/entity/state/PaintingRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolidZOffsetForward(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType wrapEntitySolidZOffsetForwardInRender(ResourceLocation location, Operation<RenderType> original) {
        return Transparent.CONFIG.painting
                ? TransparentRenderTypes.entitySolidZOffsetForward(location)
                : original.call(location);
    }

    @ModifyVariable(method = "renderPainting", at = @At(value = "HEAD"), argsOnly = true, ordinal = 1)
    private TextureAtlasSprite modifyBackSpriteInRenderPainting(
            TextureAtlasSprite currentBackSprite,
            PoseStack poseStack,
            VertexConsumer buffer,
            int[] lightCoords,
            int width,
            int height,
            TextureAtlasSprite frontSprite
    ) {
        if (Transparent.CONFIG.painting && TransparentClient.isSpriteContentsTransparent(frontSprite.contents())) {
            var paintingTextures = ((TextureAtlasHolderAccessor) Minecraft.getInstance().getPaintingTextures());
            var blankSprite = paintingTextures.callGetSprite(ResourceLocation.fromNamespaceAndPath(Transparent.MOD_ID, "blank"));
            if (blankSprite.atlasLocation().equals(frontSprite.atlasLocation())) {
                return blankSprite;
            }
        }
        return currentBackSprite;
    }
}
