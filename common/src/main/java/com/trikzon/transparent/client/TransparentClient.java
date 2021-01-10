package com.trikzon.transparent.client;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TransparentClient {
    public static final String MOD_ID = "transparent";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ConfigBean CONFIG = new ConfigBean();

    public static class ConfigBean {
        public boolean painting = true;
        public boolean item_frame = true;
        public boolean beacon_beam = false;
    }

    public static class ReloadListener implements PreparableReloadListener {
        @Override
        public String getName() {
            return new ResourceLocation(MOD_ID, "config").toString();
        }

        @Override
        public CompletableFuture<Void> reload(
                PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller prepareProfiler,
                ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor
        ) {
            return CompletableFuture.supplyAsync(() -> null).thenCompose(preparationBarrier::wait).thenRun(() -> {
                ResourceLocation configLocation = new ResourceLocation(MOD_ID, MOD_ID + ".json");
                Gson gson = new Gson();

                // Read config file on reload
                if (manager.hasResource(configLocation)) {
                    try {
                        Resource resource = manager.getResource(configLocation);
                        String configContents = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

                        CONFIG = gson.fromJson(configContents, ConfigBean.class);
                    } catch (IOException e) {
                        LOGGER.error("Error reading config file from resource pack.");
                        e.printStackTrace();
                    }
                } else {
                    CONFIG = new ConfigBean();
                }

                // Log config settings
                LOGGER.info("Painting transparency set to: " + CONFIG.painting);
                LOGGER.info("Item Frame transparency set to: " + CONFIG.item_frame);
                LOGGER.info("Beacon Beam transparency set to: " + CONFIG.beacon_beam);
            });
        }
    }
}
