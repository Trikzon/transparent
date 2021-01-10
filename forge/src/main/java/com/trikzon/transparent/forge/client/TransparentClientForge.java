package com.trikzon.transparent.forge.client;

import com.trikzon.transparent.client.TransparentClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;

public class TransparentClientForge {
    public TransparentClientForge() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager)resourceManager)
                    .registerReloadListener(new TransparentClient.ReloadListener());
        }
    }
}
