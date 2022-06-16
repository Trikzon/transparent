package com.trikzon.transparent.client.render.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.*;

public class SpriteRegistryImpl {
    private static final Map<ResourceLocation, Set<ResourceLocation>> ATLAS_TO_SPRITES = new HashMap<>();

    public static void register(ResourceLocation atlas, ResourceLocation... sprites) {
        if (ATLAS_TO_SPRITES.containsKey(atlas)) {
            ATLAS_TO_SPRITES.get(atlas).addAll(Arrays.stream(sprites).toList());
        } else {
            ATLAS_TO_SPRITES.put(atlas, new HashSet<>(Arrays.stream(sprites).toList()));
        }
    }

    public static void onTextureStitchPre(final TextureStitchEvent.Pre event) {
        for (ResourceLocation atlas : ATLAS_TO_SPRITES.keySet()) {
            if (event.getMap().location().equals(atlas)) {
                for (ResourceLocation sprite : ATLAS_TO_SPRITES.get(atlas)) {
                    event.addSprite(sprite);
                }
            }
        }
    }
}
