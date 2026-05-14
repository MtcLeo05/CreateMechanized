package com.chloe.cm.mixin;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ProcessingRecipeBuilder.class, remap = false)
public interface ProcessingRecipeBuilderAccessor {

    @Accessor(value = "params", remap = false)
    ProcessingRecipeBuilder.ProcessingRecipeParams params();
}
