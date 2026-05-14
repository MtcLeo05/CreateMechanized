package com.chloe.cm.impl.module.botania.compat.jei;

import com.chloe.cm.impl.module.botania.client.animated.AnimatedManaSpout;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class JEIManaSpoutSubcategory extends SequencedAssemblySubCategory {
    
    public static final int WIDTH = 25;
    
    private final AnimatedManaSpout spout = new AnimatedManaSpout();
    
    public JEIManaSpoutSubcategory() {
        super(WIDTH);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {
        ManaSpoutRecipe r = (ManaSpoutRecipe) recipe.getAsAssemblyRecipe();
        if (r.getRecipeCatalyst() == null) return;
        
        IRecipeSlotBuilder slot = builder
            .addSlot(RecipeIngredientRole.INPUT, x + 4, 15)
            .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
            .addIngredients(Ingredient.of(r.getRecipeCatalyst().getDisplayedStacks().toArray(new ItemStack[0])));
    }
    
    public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
        PoseStack ms = graphics.pose();
        spout.offset = index;
        ms.pushPose();
        ms.translate(-7, 50, 0);
        ms.scale(.75f, .75f, .75f);
        ms.popPose();
        spout.draw(graphics);
    }
}
