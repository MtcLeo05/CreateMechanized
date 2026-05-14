package com.chloe.cm.impl.module.botania.server.recipe;

import com.chloe.cm.CMConstants;
import com.chloe.cm.CreateMechanized;
import com.chloe.cm.impl.module.botania.compat.jei.JEIManaSpoutSubcategory;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ManaSpoutRecipe extends ProcessingRecipe<Container> implements IAssemblyRecipe {
    
    private int manaRequired;
    private StateIngredient catalyst;
    
    public ManaSpoutRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(CMBotaniaRecipes.MANA_SPOUT_TYPE, params);
        
        if(params instanceof ManaSpoutRecipeBuilder.ManaSpoutRecipeParams p) {
            manaRequired = p.manaRequired;
            catalyst = p.catalyst;
        }
    }
    
    public StateIngredient getRecipeCatalyst() {
        return catalyst;
    }
    
    @Override
    public void readAdditional(JsonObject json) {
        try {
            manaRequired = json.get("mana_required").getAsInt();
            
            if (json.has("catalyst")) {
                JsonElement element = json.get("catalyst");
                if (!element.isJsonObject() || !element.getAsJsonObject().has("type")) {
                    throw new JsonParseException("Legacy mana infusion catalyst syntax used");
                }
                
                catalyst = StateIngredientHelper.deserialize(element.getAsJsonObject());
            }
        } catch (NullPointerException e) {
            CreateMechanized.LOGGER.error("Could not load recipe " + id + ": Missing mana_required field");
        }
    }
    
    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        manaRequired = buffer.readInt();
        
        if (buffer.readBoolean()) {
            catalyst = StateIngredientHelper.read(buffer);
        }
    }
    
    @Override
    public void writeAdditional(JsonObject json) {
        json.addProperty("mana_required", manaRequired);
        
        if(getRecipeCatalyst() != null) {
            json.add("catalyst", getRecipeCatalyst().serialize());
        }
    }
    
    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(manaRequired);
        
        boolean hasCatalyst = getRecipeCatalyst() != null;
        buffer.writeBoolean(hasCatalyst);
        if (hasCatalyst) {
            getRecipeCatalyst().write(buffer);
        }
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
        return 1;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 1;
    }
    
    @Override
    public Component getDescriptionForAssembly() {
        return Component.translatable(CMConstants.MODID + ".recipe.assembly.mana_spout");
    }
    
    @Override
    public void addRequiredMachines(Set<ItemLike> list) {
        list.add(CMBotaniaBlocks.MANA_SPOUT.get());
    }
    
    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
    
    }
    
    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> JEIManaSpoutSubcategory::new;
    }
    
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        if (pContainer.isEmpty())
            return false;
        return ingredients.get(0)
            .test(pContainer.getItem(0));
        
    }
}
