package com.trikzon.transparent.client.fabric;

import com.trikzon.transparent.client.ReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class WrappedReloadListener implements IdentifiableResourceReloadListener {
    private final ReloadListener reloadListener;

    public WrappedReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(reloadListener.getName());
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return reloadListener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
    }
}
