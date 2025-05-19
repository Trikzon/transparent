package com.diontryban.transparent.mixin.client;

import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.client.render.TransparentRenderTypes;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public CapeLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Redirect(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    public RenderType redirectEntitySolidInRender(ResourceLocation resourceLocation) {
        return Transparent.CONFIG.playerCape ?
                TransparentRenderTypes.entitySolid(resourceLocation) :
                RenderType.entitySolid(resourceLocation);
    }

    // Uncomment code below to give all players a debug transparent cape.
//    @Redirect(
//            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isModelPartShown(Lnet/minecraft/world/entity/player/PlayerModelPart;)Z")
//    )
//    public boolean redirectIsModelPartShownInRender(AbstractClientPlayer player, PlayerModelPart part) {
//        return true;
//    }
//
//    @Redirect(
//            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/PlayerSkin;capeTexture()Lnet/minecraft/resources/ResourceLocation;")
//    )
//    public ResourceLocation redirectCapeTextureInRender(PlayerSkin playerSkin) {
//        return ResourceLocation.fromNamespaceAndPath("transparent", "textures/cape.png");
//    }
}
