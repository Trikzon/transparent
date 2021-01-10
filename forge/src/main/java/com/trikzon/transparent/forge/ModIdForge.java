package com.trikzon.transparent.forge;

import com.trikzon.transparent.ModIdCore;
import com.trikzon.transparent.platform.AbstractPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

@Mod(ModIdCore.MOD_ID)
public class ModIdForge implements AbstractPlatform {
    public ModIdForge() {
        ModIdCore.init(this);
    }

    @Override
    public void registerBlocks(ArrayList<Pair<String, Block>> blocks) {
        final DeferredRegister<Block> register = DeferredRegister.create(ForgeRegistries.BLOCKS, ModIdCore.MOD_ID);
        blocks.forEach(pair -> register.register(pair.getLeft(), () -> pair.getRight()));
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public void registerItems(ArrayList<Pair<String, Item>> items) {
        final DeferredRegister<Item> register = DeferredRegister.create(ForgeRegistries.ITEMS, ModIdCore.MOD_ID);
        items.forEach(pair -> register.register(pair.getLeft(), () -> pair.getRight()));
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
