package com.trikzon.transparent.fabric.client;

import com.trikzon.transparent.client.TransparentClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TransparentClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                new WrappedReloadListener(new TransparentClient.ReloadListener())
        );
    }

    private static class WrappedReloadListener implements IdentifiableResourceReloadListener {
        private final TransparentClient.ReloadListener reloadListener;

        private WrappedReloadListener(TransparentClient.ReloadListener reloadListener) {
            this.reloadListener = reloadListener;
        }

        @Override
        public ResourceLocation getFabricId() {
            return new ResourceLocation(reloadListener.getName());
        }

        @Override
        public CompletableFuture<Void> reload(
                PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller prepareProfiler,
                ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor
        ) {
            return reloadListener.reload(
                    preparationBarrier, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor
            );
        }
    }
}
