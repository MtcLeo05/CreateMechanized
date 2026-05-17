package com.chloe.cm.mixin.create;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ProcessingRecipeBuilder.ProcessingRecipeParams.class, remap = false)
public interface ProcessingRecipeParamsAccessor {
    
    @Accessor(value = "ingredients", remap = false)
    NonNullList<Ingredient> ingredients();
    
    @Accessor(value = "results", remap = false)
    NonNullList<ProcessingOutput> results();
    
    @Accessor(value = "fluidIngredients", remap = false)
    NonNullList<FluidIngredient> fluidIngredients();
    
    @Accessor(value = "fluidResults", remap = false)
    NonNullList<FluidStack> fluidResults();
}
