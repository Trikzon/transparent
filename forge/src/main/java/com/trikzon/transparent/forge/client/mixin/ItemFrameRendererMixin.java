package com.trikzon.transparent.forge.client.mixin;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.entity.item.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin extends EntityRenderer<ItemFrameEntity> {
    public ItemFrameRendererMixin(EntityRendererManager manager) {
        super(manager);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Atlases;solidBlockSheet()Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_getItemFrameRenderType() {
        return TransparentClient.CONFIG.item_frame ?
                Atlases.translucentCullBlockSheet() :
                Atlases.solidBlockSheet();
    }
}
