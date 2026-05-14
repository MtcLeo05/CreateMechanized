package com.chloe.cm.impl.module.botania.server.recipe;

import com.chloe.cm.CreateMechanized;
import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AutoTerraPlateRecipe extends ProcessingRecipe<Container> {
    
    private int manaRequired;
    
    public AutoTerraPlateRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(CMBotaniaRecipes.AUTO_TERRA_PLATE_TYPE, params);
        
        if(params instanceof AutoTerraPlateRecipeBuilder.AutoTerraPlateRecipeParams p) {
            manaRequired = p.manaRequired;
        }
    }
    
    @Override
    public void readAdditional(JsonObject json) {
        try {
            manaRequired = json.get("mana_required").getAsInt();
        } catch (NullPointerException e) {
            CreateMechanized.LOGGER.error("Could not load recipe " + id + ": Missing mana_required field");
        }
    }
    
    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        manaRequired = buffer.readInt();
    }
    
    @Override
    public void writeAdditional(JsonObject json) {
        json.addProperty("mana_required", manaRequired);
    }
    
    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(manaRequired);
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return super.getResultItem(registryAccess);
    }
    
    public int getManaRequired() {
        return manaRequired;
    }
    
    @Override
    protected int getMaxInputCount() {
        return 16;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 1;
    }
    
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        if (pContainer.isEmpty())
            return false;
        
        List<Ingredient> toCheck = new ArrayList<>(getIngredients());
        
        first:
        for  (Ingredient ing : toCheck) {
            for (int i = 0; i < pContainer.getContainerSize(); i++) {
                if(ing.test(pContainer.getItem(i))) continue first;
            }
            
            return false;
        }
        
        return true;
    }
}
