package com.trikzon.transparent.forge.client.mixin;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconTileEntityRenderer.class)
public abstract class BeaconRendererMixin extends TileEntityRenderer<BeaconTileEntity> {
    public BeaconRendererMixin(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Redirect(
            method = "renderBeaconBeam(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/util/ResourceLocation;FFJII[FFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;beaconBeam(Lnet/minecraft/util/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;")
    ) private static RenderType transparent_getBeaconBeamRenderType(ResourceLocation texture, boolean transparent) {
        return TransparentClient.CONFIG.beacon_beam ?
                RenderType.beaconBeam(texture, true) :
                RenderType.beaconBeam(texture, transparent);
    }
}
