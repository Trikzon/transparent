package com.trikzon.transparent.client.forge;

import com.trikzon.transparent.client.TransparentClient;
import com.trikzon.transparent.client.TransparentConfigReloadListener;
import com.trikzon.transparent.client.render.forge.SpriteRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class TransparentClientForge {
    public TransparentClientForge() {
        TransparentClient.initialize();

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ((ReloadableResourceManager) resourceManager).registerReloadListener(new TransparentConfigReloadListener());
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(SpriteRegistryImpl::onTextureStitchPre);
    }
}
