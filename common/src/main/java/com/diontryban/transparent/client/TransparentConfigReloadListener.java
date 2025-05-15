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

import com.diontryban.transparent.Transparent;
import com.diontryban.transparent.config.TransparentConfig;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TransparentConfigReloadListener implements PreparableReloadListener {
    @Override
    public @NotNull CompletableFuture<Void> reload(
            @NotNull PreparationBarrier preparationBarrier,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profilerFiller,
            @NotNull ProfilerFiller profilerFiller1,
            @NotNull Executor executor,
            @NotNull Executor executor1
    ) {
        return CompletableFuture.supplyAsync(() -> null).thenCompose(preparationBarrier::wait).thenRun(() -> {
            var configLocation = ResourceLocation.fromNamespaceAndPath(Transparent.MOD_ID, Transparent.MOD_ID + ".json");
            Gson gson = new Gson();

            if (resourceManager.getResource(configLocation).isPresent()) {
                try {
                    var config = TransparentConfig.empty();
                    List<Resource> resources = resourceManager.getResourceStack(configLocation);
                    for (Resource resource : resources) {
                        String configContents = IOUtils.toString(resource.open(), StandardCharsets.UTF_8);
                        config.or(gson.fromJson(configContents, TransparentConfig.class));
                    }
                    Transparent.CONFIG = config;
                } catch (IOException e) {
                    Transparent.LOG.error("Error reading config file from resource pack.");
                    e.printStackTrace();
                }
            } else {
                Transparent.CONFIG = new TransparentConfig();
            }

            Transparent.LOG.info("Armor Stand transparency set to: " + Transparent.CONFIG.armorStand);
            Transparent.LOG.info("Beacon Beam transparency set to: " + Transparent.CONFIG.beaconBeam);
            Transparent.LOG.info("End Crystal transparency set to: " + Transparent.CONFIG.endCrystal);
            Transparent.LOG.info("Item Frame transparency set to: " + Transparent.CONFIG.itemFrame);
            Transparent.LOG.info("Painting transparency set to: " + Transparent.CONFIG.painting);
        });
    }

    @Override
    public @NotNull String getName() {
        return ResourceLocation.fromNamespaceAndPath(Transparent.MOD_ID, "config").toString();
    }
}
