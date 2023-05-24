package com.diontryban.transparent.mixin.client;

import com.diontryban.transparent.Transparent;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {
    public ArmorStandRendererMixin(EntityRendererProvider.Context $$0, ArmorStandArmorModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void onGetRenderType(ArmorStand armorStand, boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<RenderType> ci) {
        if (Transparent.CONFIG.armorStand && !armorStand.isMarker()) {
            ci.setReturnValue(RenderType.entityTranslucent(this.getTextureLocation(armorStand), false));
        }
    }
}
