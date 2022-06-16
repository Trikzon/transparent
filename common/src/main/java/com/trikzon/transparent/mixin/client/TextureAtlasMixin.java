package com.trikzon.transparent.mixin.client;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin extends AbstractTexture implements Tickable {
    @Shadow @Final private ResourceLocation location;

    @Inject(method = "reload", at = @At("TAIL"))
    private void transparent_reload(TextureAtlas.Preparations preparations, CallbackInfo ci) {
        System.out.println(this.location);
        if (this.location.toString().equals("minecraft:textures/atlas/paintings.png")) {
            TransparentClient.clearPaintingTransparencyCache = true;
        }
    }
}
