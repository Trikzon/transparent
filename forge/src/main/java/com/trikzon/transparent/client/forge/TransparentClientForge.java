package com.trikzon.transparent.client.forge;

import com.trikzon.transparent.client.ReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;

public class TransparentClientForge {
    public TransparentClientForge() {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ((ReloadableResourceManager)resourceManager).registerListener(new ReloadListener());
        }
    }
}
