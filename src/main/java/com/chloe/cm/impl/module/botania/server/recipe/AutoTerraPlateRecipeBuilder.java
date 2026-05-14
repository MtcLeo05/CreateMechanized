package com.chloe.cm.impl.module.botania.server.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.api.recipe.StateIngredient;

public class AutoTerraPlateRecipeBuilder extends ProcessingRecipeBuilder<AutoTerraPlateRecipe> {
    public AutoTerraPlateRecipeBuilder(ProcessingRecipeFactory<AutoTerraPlateRecipe> factory, ResourceLocation recipeId) {
        super(factory, recipeId);
        params = new AutoTerraPlateRecipeParams(recipeId);
    }
    
    public AutoTerraPlateRecipeBuilder manaRequired(int mana) {
        ((AutoTerraPlateRecipeParams) params).manaRequired = mana;
        return this;
    }
    
    public static class AutoTerraPlateRecipeParams extends ProcessingRecipeParams {
        protected int manaRequired = 0;
        
        protected AutoTerraPlateRecipeParams(ResourceLocation id) {
            super(id);
        }
    }
}
