package com.chloe.cm.impl.module.botania.server.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

public class TradingRiftRecipeBuilder extends ProcessingRecipeBuilder<TradingRiftRecipe> {
    public TradingRiftRecipeBuilder(ProcessingRecipeFactory<TradingRiftRecipe> factory, ResourceLocation recipeId) {
        super(factory, recipeId);
        params = new TradingRiftRecipeParams(recipeId);
    }
    
    public TradingRiftRecipeBuilder manaRequired(int mana) {
        ((TradingRiftRecipeParams) params).manaRequired = mana;
        return this;
    }
    
    public static class TradingRiftRecipeParams extends ProcessingRecipeParams {
        protected int manaRequired = 0;
        
        protected TradingRiftRecipeParams(ResourceLocation id) {
            super(id);
        }
    }
}
