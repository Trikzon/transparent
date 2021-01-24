package com.trikzon.transparent.client.mixin;

import com.trikzon.transparent.Transparent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PaintingEntityRenderer.class)
public abstract class PaintingEntityRendererMixin extends EntityRenderer<PaintingEntity> {
    public PaintingEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer transparent_getPaintingRenderLayer(Identifier texture) {
        return Transparent.CONFIG.painting ?
                RenderLayer.getEntityTranslucent(texture) :
                RenderLayer.getEntitySolid(texture);
    }
}
