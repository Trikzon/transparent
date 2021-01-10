package com.trikzon.transparent.forge.client.mixin;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<PaintingEntity> {
    public PaintingRendererMixin(EntityRendererManager manager) {
        super(manager);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_getPaintingRenderType(ResourceLocation texture) {
        return TransparentClient.CONFIG.painting ?
                RenderType.entityTranslucent(texture) :
                RenderType.entitySolid(texture);
    }
}
