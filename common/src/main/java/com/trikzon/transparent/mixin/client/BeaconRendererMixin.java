package com.trikzon.transparent.mixin.client;

import com.trikzon.transparent.Transparent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconRenderer.class)
public abstract class BeaconRendererMixin extends BlockEntityRenderer<BeaconBlockEntity> {
    public BeaconRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(
            method = "renderBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/resources/ResourceLocation;FFJII[FFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;beaconBeam(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;")
    )
    private static RenderType transparent_render_getBeaconBeamRenderType(ResourceLocation texture, boolean translucent) {
        return Transparent.CONFIG.beaconBeam ?
                RenderType.beaconBeam(texture, true) :
                RenderType.beaconBeam(texture, translucent);
    }
}
