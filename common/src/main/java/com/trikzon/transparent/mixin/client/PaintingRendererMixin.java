package com.trikzon.transparent.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.trikzon.transparent.Transparent;
import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting> {
    public PaintingRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_render_getPaintingRenderType(ResourceLocation texture) {
        return Transparent.CONFIG.painting ?
                RenderType.entityTranslucent(texture) :
                RenderType.entitySolid(texture);
    }

    @ModifyVariable(method = "renderPainting", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private TextureAtlasSprite transparent_renderPainting(
            TextureAtlasSprite backSprite,
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            Painting painting,
            int width,
            int height,
            TextureAtlasSprite paintingSprite
    ) {
        TextureAtlasHolderAccessor accessor = ((TextureAtlasHolderAccessor)Minecraft.getInstance().getPaintingTextures());
        TextureAtlasSprite blankSprite = accessor.callGetSprite(new ResourceLocation(Transparent.MOD_ID, "blank"));
        return TransparentClient.isTextureAtlasSpriteTransparent(paintingSprite) ? blankSprite : backSprite;
    }
}
