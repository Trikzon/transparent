package com.trikzon.transparent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Transparent implements ClientModInitializer, IdentifiableResourceReloadListener
{
    public static final String MOD_ID = "transparent";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final File MOD_CONFIG_FILE = new File("./config/" + MOD_ID + ".json");

    public static ConfigBean CONFIG = new ConfigBean();

    @Override
    public Identifier getFabricId()
    {
        return new Identifier(MOD_ID, "config");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler,
                                          Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
    {
        return CompletableFuture.supplyAsync(() -> null).thenCompose(synchronizer::whenPrepared).thenRun(() -> {
            Identifier configId = new Identifier(MOD_ID, "transparent.json");
            Gson gson = new Gson();
            if (manager.containsResource(configId))
            {
                try
                {
                    Resource resource = manager.getResource(configId);
                    String configString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

                    CONFIG = gson.fromJson(configString, ConfigBean.class);
                } catch (IOException e)
                {
                    LOGGER.error("Error reading config file from resource pack.");
                    e.printStackTrace();
                }
            } else
            {
                try
                {
                    FileReader fileReader = new FileReader(MOD_CONFIG_FILE);
                    CONFIG = gson.fromJson(fileReader, ConfigBean.class);
                    fileReader.close();
                } catch (IOException e)
                {
                    LOGGER.error("Error reading config file from /config/.");
                    e.printStackTrace();
                }
            }

            if (CONFIG.entities.painting)
            {
                LOGGER.info("Registered paintings as transparent");
            }
        });
    }

    public static class ConfigBean
    {
        public EntitiesBean entities = new EntitiesBean();
        public HashMap<String, BlocksBean> blocks = new HashMap<>();

        public static class EntitiesBean
        {
            public boolean painting = false;
            public boolean itemframe = false;
        }

        // Keeping this here only for compatibility
        public static class BlocksBean
        {
//            public boolean isTransparent = false;
//            public boolean isTranslucent = false;
//            public boolean isGlass = false;
        }
    }

    @Override
    public void onInitializeClient()
    {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);

        if (!MOD_CONFIG_FILE.exists())
        {
            if (!MOD_CONFIG_FILE.getParentFile().exists() && !MOD_CONFIG_FILE.getParentFile().mkdirs())
            {
                LOGGER.error("Failed to create config folder to create config file.");
            }
            try (FileWriter file = new FileWriter(MOD_CONFIG_FILE))
            {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                CONFIG.entities.painting = false;
                CONFIG.entities.itemframe = false;
                file.write(gson.toJson(CONFIG));
                file.flush();
            } catch (IOException e)
            {
                LOGGER.error("Failed to create default config file.");
            }
        }
    }
}
