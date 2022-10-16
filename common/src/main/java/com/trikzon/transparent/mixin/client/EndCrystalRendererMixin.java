package com.trikzon.transparent.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.trikzon.transparent.Transparent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystalRenderer.class)
public abstract class EndCrystalRendererMixin extends EntityRenderer<EndCrystal> {
    @Shadow @Final private static ResourceLocation END_CRYSTAL_LOCATION;
    private static final RenderType TRANSLUCENT_RENDER_TYPE = RenderType.entityTranslucent(END_CRYSTAL_LOCATION);

    public EndCrystalRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer transparent_getEndCrystalBuffer(MultiBufferSource multiBufferSource, RenderType renderType) {
        return Transparent.CONFIG.endCrystal ?
                multiBufferSource.getBuffer(TRANSLUCENT_RENDER_TYPE) :
                multiBufferSource.getBuffer(renderType);
    }
}
