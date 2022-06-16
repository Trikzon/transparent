package com.trikzon.transparent.client.fabric;

import com.trikzon.transparent.client.TransparentClient;
import com.trikzon.transparent.client.TransparentConfigReloadListener;
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
        TransparentClient.initialize();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                new IdentifiableResourceReloadListener() {
                    private final TransparentConfigReloadListener reloadListener = new TransparentConfigReloadListener();

                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation(reloadListener.getName());
                    }

                    @Override
                    public CompletableFuture<Void> reload(
                            PreparationBarrier preparationBarrier,
                            ResourceManager resourceManager,
                            ProfilerFiller prepareProfiler,
                            ProfilerFiller applyProfiler,
                            Executor prepareExecutor,
                            Executor applyExecutor
                    ) {
                        return reloadListener.reload(
                                preparationBarrier,
                                resourceManager,
                                prepareProfiler,
                                applyProfiler,
                                prepareExecutor,
                                applyExecutor
                        );
                    }
                }
        );
    }
}
