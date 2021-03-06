package com.trikzon.transparent.client.fabric;

import com.trikzon.transparent.client.ReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class TransparentClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new WrappedReloadListener(new ReloadListener())
        );
    }
}
