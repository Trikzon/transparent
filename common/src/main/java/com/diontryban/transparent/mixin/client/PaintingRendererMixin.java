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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting> {
    @Unique
    private MultiBufferSource transparent$multiBufferSource;

    protected PaintingRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void onRender(Painting painting, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        this.transparent$multiBufferSource = multiBufferSource;
    }

    @ModifyVariable(method = "renderPainting", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private VertexConsumer modifyVertexConsumerInRenderPainting(
            VertexConsumer originalVertexConsumer,
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            Painting painting,
            int width,
            int height,
            TextureAtlasSprite paintingSprite
    ) {
        if (!Transparent.CONFIG.painting || this.transparent$multiBufferSource == null) {
            return originalVertexConsumer;
        } else {
            MultiBufferSource multiBufferSource = this.transparent$multiBufferSource;
            this.transparent$multiBufferSource = null;

            return multiBufferSource.getBuffer(TransparentRenderTypes.entitySolid(paintingSprite.atlasLocation()));
        }
    }

    @ModifyVariable(method = "renderPainting", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private TextureAtlasSprite modifyBackSpriteInRenderPainting(
            TextureAtlasSprite originalBackSprite,
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            Painting painting,
            int width,
            int height,
            TextureAtlasSprite paintingSprite
    ) {
        if (Transparent.CONFIG.painting && TransparentClient.isSpriteContentsTransparent(paintingSprite.contents())) {
            var accessor = ((TextureAtlasHolderAccessor) Minecraft.getInstance().getPaintingTextures());
            var blankSprite = accessor.callGetSprite(ResourceLocation.fromNamespaceAndPath(Transparent.MOD_ID, "blank"));
            // Only use blank sprite if it is on the same texture atlas as the painting sprite.
            if (blankSprite.atlasLocation().equals(paintingSprite.atlasLocation())) {
                return blankSprite;
            }
        }
        return originalBackSprite;
    }
}
