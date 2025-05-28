package com.diontryban.transparent.mixin.client.accessor;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Accessor(value="ENTITY_SNIPPET")
    static RenderPipeline.Snippet getEntitySnippet() {
        throw new AssertionError();
    }
}
