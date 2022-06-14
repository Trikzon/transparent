package com.trikzon.transparent.client;

import com.google.gson.Gson;
import com.trikzon.transparent.ConfigBean;
import com.trikzon.transparent.Transparent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TransparentConfigReloadListener implements PreparableReloadListener {
    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier preparationBarrier,
            ResourceManager resourceManager,
            ProfilerFiller prepareProfiler,
            ProfilerFiller applyProfiler,
            Executor prepareExecutor,
            Executor applyExecutor
    ) {
        return CompletableFuture.supplyAsync(() -> null).thenCompose(preparationBarrier::wait).thenRun(() -> {
            ResourceLocation configLocation = new ResourceLocation(Transparent.MOD_ID, Transparent.MOD_ID + ".json");
            Gson gson = new Gson();

            if (resourceManager.getResource(configLocation).isPresent()) {
                try {
                    ConfigBean config = ConfigBean.empty();
                    List<Resource> resources = resourceManager.getResourceStack(configLocation);
                    for (Resource resource : resources) {
                        String configContents = IOUtils.toString(resource.open(), StandardCharsets.UTF_8);
                        config.or(gson.fromJson(configContents, ConfigBean.class));
                    }
                    Transparent.CONFIG = config;
                } catch (IOException e) {
                    Transparent.LOGGER.error("Error reading config file from resource pack.");
                    e.printStackTrace();
                }
            } else {
                Transparent.CONFIG = new ConfigBean();
            }

            Transparent.LOGGER.info("Painting transparency set to: " + Transparent.CONFIG.painting);
            Transparent.LOGGER.info("Item Frame transparency set to: " + Transparent.CONFIG.itemFrame);
            Transparent.LOGGER.info("Beacon Beam transparency set to: " + Transparent.CONFIG.beaconBeam);
        });
    }

    @Override
    public String getName() {
        return new ResourceLocation(Transparent.MOD_ID, "config").toString();
    }
}
