/*
 * Copyright 2020 Trikzon
 *
 * Transparent is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * https://www.gnu.org/licenses/
 */
package com.trikzon.transparent.mixin;

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
public abstract class PaintingEntityRendererMixin extends EntityRenderer<PaintingEntity>
{
    public PaintingEntityRendererMixin(EntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer transparent_getRenderLayer(Identifier texture)
    {
        return Transparent.CONFIG.entities.painting ?
                RenderLayer.getEntityTranslucentCull(texture) :
                RenderLayer.getEntitySolid(texture);
    }
}
