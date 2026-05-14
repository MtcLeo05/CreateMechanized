package com.chloe.cm.impl.module.botania.compat.jei;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.chloe.cm.impl.module.botania.server.recipe.AutoTerraPlateRecipe;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.chloe.cm.impl.module.botania.server.recipe.TradingRiftRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class CMBotaniaJEIPlugin implements IModPlugin {
    
    public static RecipeType<ManaSpoutRecipe> MANA_SPOUT_TYPE = new RecipeType<>(CMConstants.modLoc("mana_spout"), ManaSpoutRecipe.class);
    public static RecipeType<AutoTerraPlateRecipe> AUTO_TERRA_PLATE_TYPE = new RecipeType<>(CMConstants.modLoc("auto_terra_plate"), AutoTerraPlateRecipe.class);
    public static RecipeType<TradingRiftRecipe> TRADING_RIFT_TYPE = new RecipeType<>(CMConstants.modLoc("trading_rift"), TradingRiftRecipe.class);
    
    @Override
    public ResourceLocation getPluginUid() {
        return CMConstants.modLoc("botania_jei_plugin");
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        ManaSpoutJEICategory manaSpout = new ManaSpoutJEICategory();
        registration.addRecipeCategories(
            manaSpout
        );
        
        AutoTerraPlateJEICategory autoTerraPlate = new AutoTerraPlateJEICategory();
        registration.addRecipeCategories(
            autoTerraPlate
        );
        
        TradingRiftJEICategory tradingRift = new TradingRiftJEICategory();
        registration.addRecipeCategories(
            tradingRift
        );
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ManaSpoutRecipe> manaSpoutRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.MANA_SPOUT_TYPE.getType());
        registration.addRecipes(MANA_SPOUT_TYPE, manaSpoutRecipes);
        
        List<AutoTerraPlateRecipe> autoTerraPlateRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.AUTO_TERRA_PLATE_TYPE.getType());
        registration.addRecipes(AUTO_TERRA_PLATE_TYPE, autoTerraPlateRecipes);
        
        List<TradingRiftRecipe> tradingRiftRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.TRADING_RIFT_TYPE.getType());
        registration.addRecipes(TRADING_RIFT_TYPE, tradingRiftRecipes);
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(CMBotaniaBlocks.MANA_SPOUT.get(), MANA_SPOUT_TYPE);
        registration.addRecipeCatalyst(CMBotaniaBlocks.AUTO_TERRA_PLATE.get(), AUTO_TERRA_PLATE_TYPE);
        registration.addRecipeCatalyst(CMBotaniaBlocks.MANA_PEDESTAL.get(), AUTO_TERRA_PLATE_TYPE);
        registration.addRecipeCatalyst(CMBotaniaBlocks.TRADING_RIFT.get(), TRADING_RIFT_TYPE);
        registration.addRecipeCatalyst(CMBotaniaBlocks.MANA_PEDESTAL.get(), TRADING_RIFT_TYPE);
        registration.addRecipeCatalyst(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get(), TRADING_RIFT_TYPE);
    }
}
