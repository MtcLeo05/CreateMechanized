package com.chloe.cm.impl.recipe;

import com.chloe.cm.CreateMechanized;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;
import java.util.function.BiFunction;

public class RecipeInjector {
    public static final RecipeInjector INSTANCE = new RecipeInjector();
    
    private final Map<UUID, InjectedRecipe<?, ?, ?, ?>> adaptedRecipes;
    
    private RecipeInjector() {
        adaptedRecipes = new HashMap<>();
    }
    
    public <C1 extends Container, O extends Recipe<C1>, C2 extends Container, N extends Recipe<C2>> void transmuteRecipe(RecipeType<O> oldType, BiFunction<O, RegistryAccess, N> recipeTransformer) {
        adaptedRecipes.put(UUID.randomUUID(),new InjectedRecipe<>(oldType, recipeTransformer));
    }
    
    public void injectRecipes(RecipeManager manager, RegistryAccess access) {
        ArrayList<Recipe<?>> recipes = new ArrayList<>(manager.getRecipes());
        
        for (InjectedRecipe<?, ?, ?, ?> recipe : adaptedRecipes.values()) {
            CreateMechanized.LOGGER.info("[Create: Mechanized]: Injecting recipes for type: {}", recipe.oldType());
            processInjectedRecipe(recipe, manager, recipes, access);
        }
        
        manager.replaceRecipes(recipes);
    }
    
    private <C1 extends Container, O extends Recipe<C1>, C2 extends Container, N extends Recipe<C2>> void processInjectedRecipe(
        InjectedRecipe<C1, O, C2, N> recipe, RecipeManager recipeManager, List<Recipe<?>> recipes, RegistryAccess access) {
        for (O r : recipeManager.getAllRecipesFor(recipe.oldType)) {
            recipes.add(recipe.recipeTransformer.apply(r, access));
        }
    }
    
    private record InjectedRecipe<C1 extends Container, O extends Recipe<C1>, C2 extends Container, N extends Recipe<C2>>(
        RecipeType<O> oldType,
        BiFunction<O, RegistryAccess, N> recipeTransformer) {
    }
}
