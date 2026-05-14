package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.recipe.AutoTerraPlateRecipe;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.chloe.cm.impl.module.botania.server.recipe.TradingRiftRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CMBotaniaRecipes {
    
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CMConstants.MODID);
    
    public static final DeferredRegister<RecipeType<?>> TYPES =
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CMConstants.MODID);
    
    public static IRecipeTypeInfo MANA_SPOUT_TYPE = CMBotaniaRecipes.createIRecipeTypeInfo("mana_spout", new ProcessingRecipeSerializer<>(ManaSpoutRecipe::new));
    
    public static IRecipeTypeInfo AUTO_TERRA_PLATE_TYPE = CMBotaniaRecipes.createIRecipeTypeInfo("auto_terra_plate", new ProcessingRecipeSerializer<>(AutoTerraPlateRecipe::new));
    
    public static IRecipeTypeInfo TRADING_RIFT_TYPE = CMBotaniaRecipes.createIRecipeTypeInfo("trading_rift", new ProcessingRecipeSerializer<>(TradingRiftRecipe::new));
    
    public static RecipeType<?> registerRecipe(String name, RecipeSerializer<?> serializer) {
        RecipeType<?> type = new RecipeType<>() {};
        
        TYPES.register(name, () -> type);
        SERIALIZERS.register(name, () -> serializer);
        
        return type;
    }
    
    public static IRecipeTypeInfo createIRecipeTypeInfo(String name, RecipeSerializer<?> serializer) {
        RecipeType<?> type = registerRecipe(name, serializer);
        
        return new IRecipeTypeInfo() {
            @Override
            public ResourceLocation getId() {
                return CMConstants.modLoc(name);
            }
            
            @Override
            public <T extends RecipeSerializer<?>> T getSerializer() {
                return (T) serializer;
            }
            
            @Override
            public <T extends RecipeType<?>> T getType() {
                return (T) type;
            }
        };
    }
    
}
