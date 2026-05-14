package com.chloe.cm.impl.module.botania.server.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.api.recipe.StateIngredient;

public class ManaSpoutRecipeBuilder extends ProcessingRecipeBuilder<ManaSpoutRecipe> {
    public ManaSpoutRecipeBuilder(ProcessingRecipeFactory<ManaSpoutRecipe> factory, ResourceLocation recipeId) {
        super(factory, recipeId);
        params = new ManaSpoutRecipeParams(recipeId);
    }
    
    public ManaSpoutRecipeBuilder manaRequired(int mana) {
        ((ManaSpoutRecipeParams) params).manaRequired = mana;
        return this;
    }
    
    public ManaSpoutRecipeBuilder catalyst(StateIngredient catalyst) {
        ((ManaSpoutRecipeParams) params).catalyst = catalyst;
        return this;
    }
    
    public static class ManaSpoutRecipeParams extends ProcessingRecipeParams {
        protected int manaRequired = 0;
        protected StateIngredient catalyst;
        
        protected ManaSpoutRecipeParams(ResourceLocation id) {
            super(id);
        }
    }
}
