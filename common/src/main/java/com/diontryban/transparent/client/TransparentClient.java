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

package com.diontryban.transparent.client;

import com.diontryban.ash_api.modloader.CommonClientModInitializer;
import com.diontryban.ash_api.resources.ResourceLoader;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.util.HashMap;
import java.util.Map;

public class TransparentClient extends CommonClientModInitializer {
    private static final Map<ResourceLocation, Boolean> PAINTING_TRANSPARENCY_CACHE = new HashMap<>();
    public static boolean clearPaintingTransparencyCache = false;

    @Override
    public void onInitializeClient() {
        ResourceLoader
                .get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new TransparentConfigReloadListener());
    }

    public static boolean isSpriteContentsTransparent(SpriteContents sprite) {
        if (clearPaintingTransparencyCache) {
            PAINTING_TRANSPARENCY_CACHE.clear();
            clearPaintingTransparencyCache = false;
        }

        if (PAINTING_TRANSPARENCY_CACHE.containsKey(sprite.name())) {
            return PAINTING_TRANSPARENCY_CACHE.get(sprite.name());
        }

        for (int u = 0; u < sprite.width(); u++) {
            for (int v = 0; v < sprite.height(); v++) {
                if (sprite.isTransparent(0, u, v)) {
                    PAINTING_TRANSPARENCY_CACHE.put(sprite.name(), true);
                    return true;
                }
            }
        }
        PAINTING_TRANSPARENCY_CACHE.put(sprite.name(), false);
        return false;
    }
}