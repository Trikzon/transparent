package com.trikzon.transparent.forge;

import com.trikzon.transparent.client.TransparentClient;
import com.trikzon.transparent.forge.client.TransparentClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(TransparentClient.MOD_ID)
public class TransparentForge {
    public TransparentForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TransparentClientForge::new);
    }
}
