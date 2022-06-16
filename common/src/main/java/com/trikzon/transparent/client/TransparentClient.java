package com.trikzon.transparent.client;

import com.trikzon.transparent.Transparent;
import com.trikzon.transparent.client.render.SpriteRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TransparentClient {
    public static void initialize() {
        SpriteRegistry.register(
                new ResourceLocation("textures/atlas/paintings.png"),
                new ResourceLocation(Transparent.MOD_ID, "painting/blank")
        );
    }

    private static final Map<ResourceLocation, Boolean> PAINTING_TRANSPARENCY_CACHE = new HashMap<>();
    public static boolean clearPaintingTransparencyCache = false;

    public static boolean isTextureAtlasSpriteTransparent(TextureAtlasSprite sprite) {
        if (TransparentClient.clearPaintingTransparencyCache) {
            TransparentClient.PAINTING_TRANSPARENCY_CACHE.clear();
            TransparentClient.clearPaintingTransparencyCache = false;
        }

        if (TransparentClient.PAINTING_TRANSPARENCY_CACHE.containsKey(sprite.getName())) {
            return TransparentClient.PAINTING_TRANSPARENCY_CACHE.get(sprite.getName());
        }

        for (int u = 0; u < sprite.getWidth(); u++) {
            for (int v = 0; v < sprite.getHeight(); v++) {
                if (sprite.isTransparent(0, u, v)) {
                    TransparentClient.PAINTING_TRANSPARENCY_CACHE.put(sprite.getName(), true);
                    return true;
                }
            }
        }
        TransparentClient.PAINTING_TRANSPARENCY_CACHE.put(sprite.getName(), false);
        return false;
    }
}
