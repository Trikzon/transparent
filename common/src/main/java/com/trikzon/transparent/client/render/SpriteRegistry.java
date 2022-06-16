package com.trikzon.transparent.client.render;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

public class SpriteRegistry {
    @ExpectPlatform
    public static void register(ResourceLocation atlas, ResourceLocation... sprites) {
        throw new AssertionError();
    }
}
