package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlocks;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructFluids;
import com.simibubi.create.AllBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

public class CMRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public CMRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }
    
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        botania(pWriter);
        tconstruct(pWriter);
    }
    
    private void botania(Consumer<FinishedRecipe> pWriter) {
        ConditionalRecipe.builder()
            .addCondition(modLoaded("botania"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBotaniaBlocks.MANA_SPOUT.get())
                    .unlockedBy("hasManaPool", has(BotaniaBlocks.manaPool))
                    .define('G', BotaniaBlocks.manaGlass)
                    .define('P', BotaniaBlocks.manaPool)
                    .define('L', BotaniaBlocks.livingrock)
                    .define('p', BotaniaBlocks.livingwoodPlanks)
                    .pattern(" L ")
                    .pattern("GPG")
                    .pattern(" p ")
                    .save(c, CMConstants.modLoc("botania/mana_spout"))
            )
            .build(pWriter, CMConstants.modLoc("botania/mana_spout"));
        
        ConditionalRecipe.builder()
            .addCondition(modLoaded("botania"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBotaniaBlocks.MANA_PEDESTAL.get(), 2)
                    .unlockedBy("hasManaPylon", has(BotaniaBlocks.manaPylon))
                    .define('P', BotaniaBlocks.manaPylon)
                    .define('D', AllBlocks.DEPOT)
                    .pattern("P")
                    .pattern("D")
                    .save(c, CMConstants.modLoc("botania/mana_pedestal"))
            )
            .build(pWriter, CMConstants.modLoc("botania/mana_pedestal"));
        
        ConditionalRecipe.builder()
            .addCondition(modLoaded("botania"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBotaniaBlocks.AUTO_TERRA_PLATE.get())
                    .unlockedBy("hasTerrasteelIngot", has(BotaniaItems.terrasteel))
                    .define('P', BotaniaBlocks.terraPlate)
                    .define('R', BotaniaBlocks.livingrock)
                    .define('L', Blocks.LAPIS_BLOCK)
                    .define('T', BotaniaItems.terrasteel)
                    .pattern("RTR")
                    .pattern("LPL")
                    .pattern("RLR")
                    .save(c, CMConstants.modLoc("botania/auto_terra_plate"))
            )
            .build(pWriter, CMConstants.modLoc("botania/auto_terra_plate"));
        
        ConditionalRecipe.builder()
            .addCondition(modLoaded("botania"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get(), 2)
                    .unlockedBy("hasNaturaPylon", has(BotaniaBlocks.naturaPylon))
                    .define('P', BotaniaBlocks.naturaPylon)
                    .define('D', AllBlocks.DEPOT)
                    .pattern("P")
                    .pattern("D")
                    .save(c, CMConstants.modLoc("botania/trade_mana_pedestal"))
            )
            .build(pWriter, CMConstants.modLoc("botania/trade_mana_pedestal"));
        
        ConditionalRecipe.builder()
            .addCondition(modLoaded("botania"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMBotaniaBlocks.TRADING_RIFT.get())
                    .unlockedBy("hasDragonstone", has(BotaniaBlocks.dragonstoneBlock))
                    .define('P', BotaniaBlocks.alfPortal)
                    .define('R', BotaniaBlocks.livingrock)
                    .define('E', BotaniaBlocks.elementiumBlock)
                    .define('D', BotaniaBlocks.dragonstoneBlock)
                    .define('T', BotaniaItems.pixieDust)
                    .pattern("RTR")
                    .pattern("EPE")
                    .pattern("RDR")
                    .save(c, CMConstants.modLoc("botania/trading_rift"))
            )
            .build(pWriter, CMConstants.modLoc("botania/trading_rift"));
    }
    
    private void tconstruct(Consumer<FinishedRecipe> pWriter) {
        MeltingFuelBuilder.fuel(new FluidStack(CMTinkersConstructFluids.LIQUID_HEAT_FLUID.get(), 1), 1)
            .save(pWriter, CMConstants.modLoc("tconstruct/liquid_heat_fuel"));
        
        MeltingFuelBuilder.fuel(new FluidStack(CMTinkersConstructFluids.LIQUID_SUPER_HEAT_FLUID.get(), 1), 1)
            .save(pWriter, CMConstants.modLoc("tconstruct/liquid_super_heat_fuel"));
        
        MeltingFuelBuilder.fuel(new FluidStack(CMTinkersConstructFluids.LIQUID_LOW_HEAT_FLUID.get(), 1), 1)
            .save(pWriter, CMConstants.modLoc("tconstruct/liquid_low_heat_fuel"));
        
        
        ConditionalRecipe.builder()
            .addCondition(modLoaded("tconstruct"))
            .addRecipe(
                c -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMTinkersConstructBlocks.SEARED_BURNER.get())
                    .unlockedBy("hasBlazeBurner", has(AllBlocks.BLAZE_BURNER))
                    .define('S', TinkerSmeltery.searedBrick)
                    .define('B', AllBlocks.BLAZE_BURNER.get())
                    .pattern("SSS")
                    .pattern("SBS")
                    .pattern("SSS")
                    .save(c, CMConstants.modLoc("tconstruct/seared_burner"))
            )
            .build(pWriter, CMConstants.modLoc("tconstruct/seared_burner"));
    }
}
