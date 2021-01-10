package com.trikzon.transparent.fabric.client.mixin;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin extends EntityRenderer<ItemFrame> {
    public ItemFrameRendererMixin(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;solidBlockSheet()Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_getItemFrameRenderType() {
        return TransparentClient.CONFIG.item_frame ?
                Sheets.translucentCullBlockSheet() :
                Sheets.solidBlockSheet();
    }
}
