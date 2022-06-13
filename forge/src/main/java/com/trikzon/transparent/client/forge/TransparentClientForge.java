package com.trikzon.transparent.client.forge;

import com.trikzon.transparent.client.TransparentConfigReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

public class TransparentClientForge {
    public TransparentClientForge() {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ((ReloadableResourceManager) resourceManager).registerReloadListener(new TransparentConfigReloadListener());
        }
    }
}
