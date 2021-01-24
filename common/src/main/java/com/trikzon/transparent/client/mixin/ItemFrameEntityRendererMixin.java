package com.trikzon.transparent.client.mixin;

import com.trikzon.transparent.Transparent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin extends EntityRenderer<ItemFrameEntity> {
    public ItemFrameEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TexturedRenderLayers;getEntitySolid()Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer transparent_render_getItemFrameRenderLayer() {
        return Transparent.CONFIG.item_frame ?
                TexturedRenderLayers.getEntityTranslucentCull() :
                TexturedRenderLayers.getEntitySolid();
    }
}
