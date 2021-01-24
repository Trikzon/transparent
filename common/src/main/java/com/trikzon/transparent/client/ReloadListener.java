package com.trikzon.transparent.client;

import com.google.gson.Gson;
import com.trikzon.transparent.ConfigBean;
import com.trikzon.transparent.Transparent;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListener implements ResourceReloadListener {
    @Override
    public CompletableFuture<Void> reload(
            Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler,
            Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor
    ) {
        return CompletableFuture.supplyAsync(() -> null).thenCompose(synchronizer::whenPrepared).thenRun(() -> {
            Identifier configLocation = new Identifier(Transparent.MOD_ID, Transparent.MOD_ID + ".json");
            Gson gson = new Gson();

            // Read config file on reload
            if (manager.containsResource(configLocation)) {
                try {
                    Resource resource = manager.getResource(configLocation);
                    String configContents = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

                    Transparent.CONFIG = gson.fromJson(configContents, ConfigBean.class);
                } catch (IOException e) {
                    Transparent.LOGGER.error("Error reading config file from resource pack.");
                    e.printStackTrace();
                }
            } else {
                Transparent.CONFIG = new ConfigBean();
            }

            Transparent.LOGGER.info("Painting transparency set to: " + Transparent.CONFIG.painting);
            Transparent.LOGGER.info("Item Frame transparency set to: " + Transparent.CONFIG.item_frame);
            Transparent.LOGGER.info("Beacon Beam transparency set to: " + Transparent.CONFIG.beacon_beam);
        });
    }

    @Override
    public String getName() {
        return new Identifier(Transparent.MOD_ID, "config").toString();
    }
}
