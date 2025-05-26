/*
 * This file is part of Transparent. A copy of this program can be found at
 * https://github.com/Trikzon/transparent.
 * Copyright (C) 2023 Dion Tryban
 *
 * Transparent is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Transparent is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Transparent. If not, see <https://www.gnu.org/licenses/>.
 */

package com.diontryban.transparent.mixin.client;

import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.client.render.TransparentRenderTypes;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStand, ArmorStandRenderState, ArmorStandArmorModel> {
    public ArmorStandRendererMixin(EntityRendererProvider.Context $$0, ArmorStandArmorModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Unique
    private boolean transparent$superGetRenderType = false;

    @Inject(
            method = "getRenderType(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At("HEAD"), cancellable = true
    )
    private void onGetRenderType(
            ArmorStandRenderState renderState,
            boolean isVisible, boolean renderTranslucent, boolean appearsGlowing,
            CallbackInfoReturnable<RenderType> cir
    ) {
        // Don't inject our logic if superGetRenderType flag is true. That way
        // we can get the render type that is made without our code and get the
        // texture from that. This is important to work with mods that allow
        // the change of textures such as Entity Texture Features.
        if (transparent$superGetRenderType) {
            transparent$superGetRenderType = false;
            return;
        }

        if (isVisible && !renderTranslucent) {
            if (Transparent.CONFIG.armorStand && !renderState.isMarker) {
                transparent$superGetRenderType = true;
                RenderType superType = this.getRenderType(renderState, true, false, appearsGlowing);
                if (superType != null) {
                    ResourceLocation textureLoc = TransparentRenderTypes.getTexture(superType, this.getTextureLocation(renderState));
                    cir.setReturnValue(TransparentRenderTypes.entityCutoutNoCull(textureLoc));
                }
            }
        }
    }
}
