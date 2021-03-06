package com.trikzon.transparent.forge;

import com.trikzon.transparent.Transparent;
import com.trikzon.transparent.client.forge.TransparentClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Transparent.MOD_ID)
public class TransparentForge {
    public TransparentForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TransparentClientForge::new);
    }
}
