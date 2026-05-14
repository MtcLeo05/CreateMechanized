package com.chloe.cm.impl.module.botania.compat.jei;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.client.animated.AnimatedManaSpout;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.server.recipe.TradingRiftRecipe;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

public class TradingRiftJEICategory implements IRecipeCategory<TradingRiftRecipe> {
    
    public TradingRiftJEICategory() {
    }
    
    @Override
    public RecipeType<TradingRiftRecipe> getRecipeType() {
        return CMBotaniaJEIPlugin.TRADING_RIFT_TYPE;
    }
    
    private final AnimatedManaSpout spout = new AnimatedManaSpout();
    
    @Override
    public Component getTitle() {
        return Component.translatable("category." + CMConstants.MODID + ".trading_rift");
    }
    
    @Override
    public @Nullable IDrawable getIcon() {
        return new ItemIcon(CMBotaniaBlocks.TRADING_RIFT.get().asItem()::getDefaultInstance);
    }
    
    @Override
    public @Nullable IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 220;
            }
            
            @Override
            public int getHeight() {
                return 80;
            }
            
            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            }
        };
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TradingRiftRecipe recipe, IFocusGroup iFocusGroup) {
        NonNullList<Ingredient> ings = recipe.getIngredients();
        List<ItemStack> outputs = recipe.getRollableResultsAsItemStacks();
        
        int centerY = getHeight() / 2;
        int arrowWidth = AllGuiTextures.JEI_ARROW.getWidth();
        int arrowX = (getWidth() - arrowWidth) / 2;
        int padding = 4;
        
        int numInputs = ings.size();
        if (numInputs > 0) {
            int inCols = Math.min(4, numInputs);
            int inRows = (numInputs - 1) / 4 + 1;
            
            int inGridW = (inCols - 1) * 20 + 18;
            int inGridH = (inRows - 1) * 20 + 18;
            
            int inStartX = arrowX - padding - inGridW;
            int inStartY = centerY - inGridH / 2;
            
            for (int i = 0; i < numInputs; i++) {
                int col = i % 4;
                int row = i / 4;
                
                builder.addSlot(RecipeIngredientRole.INPUT, inStartX + (col * 20), inStartY + (row * 20))
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(ings.get(i));
            }
        }
        
        int numOutputs = outputs.size();
        if (numOutputs > 0) {
            int outRows = (numOutputs - 1) / 4 + 1;
            
            int outGridH = (outRows - 1) * 20 + 18;
            
            int outStartX = arrowX + arrowWidth + padding;
            int outStartY = centerY - outGridH / 2;
            
            for (int i = 0; i < numOutputs; i++) {
                int col = i % 4;
                int row = i / 4;
                
                builder.addSlot(RecipeIngredientRole.OUTPUT, outStartX + (col * 20), outStartY + (row * 20))
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(outputs.get(i));
            }
        }
    }
    
    @Override
    public void draw(TradingRiftRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        int arrowWidth = AllGuiTextures.JEI_ARROW.getWidth();
        int arrowHeight = AllGuiTextures.JEI_ARROW.getHeight();
        
        int arrowX = (getWidth() - arrowWidth) / 2;
        int arrowY = (getHeight() - arrowHeight) / 2;
        
        AllGuiTextures.JEI_ARROW.render(graphics, arrowX, arrowY);
    }
}