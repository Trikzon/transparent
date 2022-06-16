package com.trikzon.transparent.client.render.fabric;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.resources.ResourceLocation;

public class SpriteRegistryImpl {
    public static void register(ResourceLocation atlas, ResourceLocation... sprites) {
        ClientSpriteRegistryCallback.event(atlas).register((atlasTexture, registry) -> {
            for (ResourceLocation sprite : sprites) {
                registry.register(sprite);
            }
        });
    }
}
