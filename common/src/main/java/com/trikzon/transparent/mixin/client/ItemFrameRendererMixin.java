package com.trikzon.transparent.mixin.client;

import com.trikzon.transparent.Transparent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin<T extends ItemFrame> extends EntityRenderer<T> {
    public ItemFrameRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;solidBlockSheet()Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_render_getItemFrameRenderType() {
        return Transparent.CONFIG.itemFrame ?
                Sheets.translucentCullBlockSheet() :
                Sheets.solidBlockSheet();
    }
}
