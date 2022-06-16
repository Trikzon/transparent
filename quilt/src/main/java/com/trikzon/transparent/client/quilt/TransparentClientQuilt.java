package com.trikzon.transparent.client.quilt;

import com.trikzon.transparent.client.TransparentClient;
import com.trikzon.transparent.client.TransparentConfigReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TransparentClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        TransparentClient.initialize();

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
                new IdentifiableResourceReloader() {
                    private final TransparentConfigReloadListener reloadListener = new TransparentConfigReloadListener();

                    @Override
                    public ResourceLocation getQuiltId() {
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
