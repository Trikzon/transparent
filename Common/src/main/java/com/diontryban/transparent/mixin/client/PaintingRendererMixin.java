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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting> {
    @Shadow public abstract ResourceLocation getTextureLocation(Painting $$0);

    protected PaintingRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer modifyGetBufferResultInRender(
            VertexConsumer original,
            Painting entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        return Transparent.CONFIG.painting
                ? bufferSource.getBuffer(TransparentRenderTypes.entitySolid(this.getTextureLocation(entity)))
                : original;
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
        if (TransparentClient.isSpriteContentsTransparent(paintingSprite.contents())) {
            var accessor = ((TextureAtlasHolderAccessor) Minecraft.getInstance().getPaintingTextures());
            return accessor.callGetSprite(new ResourceLocation(Transparent.MOD_ID, "blank"));
        }
        return originalBackSprite;
    }
}
