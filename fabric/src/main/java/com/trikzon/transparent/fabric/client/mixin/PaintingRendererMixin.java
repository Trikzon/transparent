package com.trikzon.transparent.fabric.client.mixin;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting> {
    public PaintingRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType transparent_getPaintingRenderType(ResourceLocation texture) {
        return TransparentClient.CONFIG.painting ?
                RenderType.entityTranslucent(texture) :
                RenderType.entitySolid(texture);
    }
}
