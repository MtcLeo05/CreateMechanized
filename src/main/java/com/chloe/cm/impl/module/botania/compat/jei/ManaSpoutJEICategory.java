package com.chloe.cm.impl.module.botania.compat.jei;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.client.animated.AnimatedManaSpout;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.gui.recipes.RecipesGui;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.recipe.StateIngredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

public class ManaSpoutJEICategory implements IRecipeCategory<ManaSpoutRecipe> {
    
    public ManaSpoutJEICategory() {
    }
    
    @Override
    public RecipeType<ManaSpoutRecipe> getRecipeType() {
        return CMBotaniaJEIPlugin.MANA_SPOUT_TYPE;
    }
    
    private final AnimatedManaSpout spout = new AnimatedManaSpout();
    
    @Override
    public Component getTitle() {
        return Component.translatable("category." + CMConstants.MODID + ".mana_spout");
    }
    
    @Override
    public @Nullable IDrawable getIcon() {
        return new ItemIcon(CMBotaniaBlocks.MANA_SPOUT.get().asItem()::getDefaultInstance);
    }
    
    @Override
    public @Nullable IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 177;
            }
            
            @Override
            public int getHeight() {
                return 86;
            }
            
            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            }
        };
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ManaSpoutRecipe recipe, IFocusGroup iFocusGroup) {
        builder
            .addSlot(RecipeIngredientRole.INPUT, 27, 51)
            .setBackground(getRenderedSlot(), -1, -1)
            .addIngredients(recipe.getIngredients().get(0));
        
        builder
            .addSlot(RecipeIngredientRole.OUTPUT, 132, 51)
            .setBackground(getRenderedSlot(), -1, -1)
            .addItemStack(CreateRecipeCategory.getResultItem(recipe));
    }
    
    @Override
    public void draw(ManaSpoutRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 62, recipe.getRecipeCatalyst() != null? 75: 57);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 126, 29);
        
        spout.mana = recipe.getManaRequired();
        spout.scale = 20;
        spout.catalyst = recipe.getRecipeCatalyst();
        spout.showCatalystUnder = true;
        spout.mouseX = mouseX;
        spout.mouseY = mouseY;
        spout.draw(graphics, getBackground().getWidth() / 2 - 15, -28);
    }
}
