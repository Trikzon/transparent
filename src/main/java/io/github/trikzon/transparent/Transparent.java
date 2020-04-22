/* ===========================================================================
 * Copyright 2020 Trikzon
 *
 * Transparent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * File: Transparent.java
 * Date: 2020-04-20 "1.15.2-1.1.1"
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.transparent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Transparent implements ClientModInitializer, IdentifiableResourceReloadListener
{
    public static final String MOD_ID = "transparent";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    private static final File MOD_CONFIG_FILE = new File("./config/" + MOD_ID + ".json");

    public static ConfigBean CONFIG = new ConfigBean();

    public static ArrayList<Block> BANNED_BLOCKS = new ArrayList<>();

    @Override
    public void onInitializeClient()
    {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);

        if (!MOD_CONFIG_FILE.exists())
        {
            MOD_CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter file = new FileWriter(MOD_CONFIG_FILE))
            {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                CONFIG.blocks.put("minecraft:example1", new ConfigBean.BlocksBean());
                CONFIG.blocks.put("minecraft:example2", new ConfigBean.BlocksBean());
                file.write(gson.toJson(CONFIG));
                file.flush();
            } catch (IOException e)
            {
                LOGGER.error("Failed to create default config file.");
            }
        }
        BANNED_BLOCKS.add(Blocks.STONE);
        BANNED_BLOCKS.add(Blocks.SAND);
        BANNED_BLOCKS.add(Blocks.GRAVEL);
        BANNED_BLOCKS.add(Blocks.DIRT);
        BANNED_BLOCKS.add(Blocks.ANDESITE);
        BANNED_BLOCKS.add(Blocks.DIORITE);
        BANNED_BLOCKS.add(Blocks.GRANITE);
        BANNED_BLOCKS.add(Blocks.NETHERRACK);
    }

    @Override
    public Identifier getFabricId()
    {
        return new Identifier(MOD_ID, "config");
    }

    private static Map<Block, RenderLayer> BLOCK_LAYERS;
    private static Map<Block, RenderLayer> DEFAULT_BLOCK_LAYERS;
    private static List<Block> CHANGED_BLOCK_LAYERS = new ArrayList<>();

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

            if (DEFAULT_BLOCK_LAYERS == null || BLOCK_LAYERS == null)
            {
                BLOCK_LAYERS = ReflectionHelper.getValue(
                        RenderLayers.class, null,
                        DEBUG ? "BLOCKS" : "field_21469"
                );
                DEFAULT_BLOCK_LAYERS = new HashMap<>(BLOCK_LAYERS);
            }
            CHANGED_BLOCK_LAYERS.stream().filter(block -> !CONFIG.blocks.containsKey(Registry.BLOCK.getId(block).toString()))
                    .forEach(block -> {
                        if (DEFAULT_BLOCK_LAYERS.containsKey(block))
                            BLOCK_LAYERS.put(block, DEFAULT_BLOCK_LAYERS.get(block));
                        else
                            BLOCK_LAYERS.put(block, RenderLayer.getSolid());
                        ((ISetTransparent) block).reset();
                        LOGGER.info("Deregistered " + Registry.BLOCK.getId(block));
                    });
            CHANGED_BLOCK_LAYERS.clear();
            CONFIG.blocks.keySet().stream().filter(s -> Registry.BLOCK.containsId(new Identifier(s)))
                    .forEach(s -> {
                        Identifier id = new Identifier(s);
                        Block block = Registry.BLOCK.get(id);

                        if (true)
                        {
                            if (CONFIG.blocks.get(s).isTranslucent)
                            {
                                BLOCK_LAYERS.put(block, RenderLayer.getTranslucent());
                            } else if (CONFIG.blocks.get(s).isTransparent)
                            {
                                BLOCK_LAYERS.put(block, RenderLayer.getCutout());
                            }
                            CHANGED_BLOCK_LAYERS.add(block);
                            ((ISetTransparent) block).setTransparent(CONFIG.blocks.get(s).isTransparent, CONFIG.blocks.get(s).isGlass);
                            LOGGER.info("Registered " + id);
                        }
                    });
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
        }

        public static class BlocksBean
        {
            public boolean isTransparent = false;
            public boolean isTranslucent = false;
            public boolean isGlass = false;
        }
    }
}
