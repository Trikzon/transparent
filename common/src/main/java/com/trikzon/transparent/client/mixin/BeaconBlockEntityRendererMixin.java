package com.trikzon.transparent.client.mixin;

import com.trikzon.transparent.Transparent;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconBlockEntityRenderer.class)
public abstract class BeaconBlockEntityRendererMixin extends BlockEntityRenderer<BeaconBlockEntity> {
    public BeaconBlockEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(
            method = "Lnet/minecraft/client/render/block/entity/BeaconBlockEntityRenderer;renderLightBeam(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/util/Identifier;FFJII[FFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getBeaconBeam(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;")
    )
    private static RenderLayer transparent_getBeaconBeamRenderLayer(Identifier texture, boolean translucent) {
        return Transparent.CONFIG.beacon_beam ?
                RenderLayer.getBeaconBeam(texture, true) :
                RenderLayer.getBeaconBeam(texture, translucent);
    }
}
