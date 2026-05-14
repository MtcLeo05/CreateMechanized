package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.init.CMBotaniaItems;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlocks;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class CMLanguageProvider extends LanguageProvider {
    
    public CMLanguageProvider(PackOutput output, String locale) {
        super(output, CMConstants.MODID, locale);
    }
    
    @Override
    protected void addTranslations() {
        botania();
        tconstruct();
    }
    
    private void botania() {
        this.add("create.gui.goggles.mana_container", "Mana Container Info:    ");
        this.add("create.gui.goggles.mana_container.capacity", "Capacity: ");
        this.add("create.gui.goggles.mana_container.mana", "Mana: ");
        
        this.add("create.gui.goggles.mana_pedestal.angry.main", "This Encapsulator is overloaded!");
        this.add("create.gui.goggles.mana_pedestal.angry.sub", "Make sure only 1 Complex / Rift is in radius!");
        
        this.add("create.gui.goggles.trade_mana_pedestal.angry.main", "This Encapsulator is overloaded!");
        this.add("create.gui.goggles.trade_mana_pedestal.angry.sub", "Make sure only 1 Rift is in radius!");
        
        this.add(CMConstants.MODID + ".recipe.assembly.mana_spout", "Spout Mana");
        this.add("category." + CMConstants.MODID + ".mana_spout", "Mana Filling");
        this.add("category." + CMConstants.MODID + ".auto_terra_plate", "Rotational Agglomeration Complex");
        this.add("category." + CMConstants.MODID + ".trading_rift", "Trading Rift");
        
        this.add(CMBotaniaBlocks.MANA_SPOUT.get(), "Mana Spout");
        this.add(CMBotaniaBlocks.MANA_PEDESTAL.get(), "Terrestrial Item Encapsulator");
        this.add(CMBotaniaBlocks.AUTO_TERRA_PLATE.get(), "Rotational Agglomeration Complex");
        this.add(CMBotaniaBlocks.TRADING_RIFT.get(), "Trading Rift Stabilizer");
        this.add(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get(), "Dimensionally Stable Item Encapsulator");
        
        this.add(CMBotaniaItems.RUNIC_TEMPLATE.get(), "Runic Template");
        this.add(CMBotaniaItems.UNETCHED_RUNE.get(), "Unetched Rune");
        
        this.add(CMConstants.MODID + ".itemGroup.botania", "Create Mechanized: Botania");
        this.add("tooltip." + CMConstants.MODID + ".mana_package.mana", "Mana: %d / %d");
        
        this.add(CMConstants.MODID + ".ponder.tag.botania/mana_manipulators", "Mana Manipulators");
        this.add(CMConstants.MODID + ".ponder.tag.botania/mana_manipulators.description", "Components that interact with Botania's mana");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.header", "Infusing items with mana using a Mana Spout");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_1", "The Mana Spout can handle all recipes a Mana Pool can.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_2", "Simply fill it with mana, using a spreader or anything else");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_3", "Then put the item you want to infuse in below it.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_4", "You need either a belt or a depot.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_5", "If you have a pool with a spark on it.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_6", "And the Mana Spout also has one.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout.text_7", "Then the spout will automatically refill when out of mana, if a recipe is in progress.");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout_catalyst.header", "Handling infusion catalysts");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout_catalyst.text_1", "Some recipes might need a block under the pool to work.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/mana_spout_catalyst.text_2", "You can achieve the same behaviour by replacing the block under the depot / belt.");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.header", "Automatic Agglomeration Plate");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_1", "The Rotational Agglomeration Complex (R.A.C) can handle all your terrasteel needs.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_2", "Simply place 1 Terrestrial Item Encapsulator (T.I.E) per item required in range of the R.A.C.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_3", "The R.A.C. has a 2 block \"radius\", including vertically.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_4", "Then put the needed items inside the T.I.E.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_5", "Make sure you have a mana source linked to the R.A.C,");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_6", "and that the R.A.C is spinning at least at FAST speed.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_7", "If it's working, you'll know.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_8", "The items will then be consumed.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_9", "A little animation will play.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_10", "And terrasteel will appear in the R.A.C.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_11", "Animation and craft time will scale with rotation speed (higher speed, lower time). If rotation is interrupted mid-crafting recipe progress will be saved.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_12", "Be aware that there's a delay between first activation (reaching >FAST speed) and craft start, so you should avoid turning it off and on frequently.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate.text_13", "Both T.I.E. and R.A.C. can be connected to Mechanical Arms.");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate_overload.header", "Beware of Encapsulator Overload!");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate_overload.text_1", "The R.A.C. will search for T.I.E. regardless of line of sight.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate_overload.text_2", "This means that a T.I.E. might be in range of more R.A.C / T.R.S. simultaneously.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate_overload.text_3", "If that happens, the T.I.E. will become overloaded, and won't be able to provide crafting materials.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/auto_terra_plate_overload.text_4", "If a T.I.E. becomes overloaded mid-recipe, the active one will be finished, but the next won't start.");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.header", "Industrial Elven Trade");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_1", "The Trading Rift Stabilizer (T.R.S.) allows you to automate trading with Elves");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_2", "Simply place 1 Terrestrial Item Encapsulator (T.I.E) per item required in range of the T.R.S.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_3", "And also place 1 Dimensionally Stable Item Encapsulator (D.S.I.E) per output in range of the T.R.S.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_4", "Then put the needed items inside the T.I.E.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_5", "Make sure you have a mana pool nearby,");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_6", "With a natura pylon above, (botania and ponder don't like each other, trust that there is one)");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_7", "and that the T.R.S. is spinning at least at FAST speed.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_8", "If it's working, you'll know.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_9", "The items will then be consumed.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_10", "A little animation will play.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_11", "And all outputs with appear on the relative D.S.I.E.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_12", "Same rules of the R.A.C. apply, those being craft speed, recipe saving, and start up delay.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_13", "Be aware that every time the rift shuts down and re-starts, it will consume mana, just like an alfheim portal would.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift.text_14", "Both D.S.I.E., T.I.E. and T.R.S. can be connected to Mechanical Arms.");
        
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift_overload.header", "Beware of Encapsulator Overload!");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift_overload.text_1", "The T.R.S will search for T.I.E. and D.S.I.E. regardless of line of sight.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift_overload.text_2", "This means that a D.S.I.E. / T.I.E. might be in range of more R.A.C / T.R.S. simultaneously.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift_overload.text_3", "If that happens, the D.S.I.E. / T.I.E. will become overloaded, and won't be able to provide crafting materials.");
        this.add(CMConstants.MODID + ".ponder.botania/mana_manipulators/trading_rift_overload.text_4", "If a D.S.I.E. / T.I.E. becomes overloaded mid-recipe, the active one will be finished, but the next won't start.");
        
        new PonderLocalization().provideLang(CMConstants.MODID, this::add);
    }
    
    private void tconstruct() {
        this.add("fluid." + CMConstants.MODID + ".liquid_heat", "Liquid Heat");
        this.add("fluid." + CMConstants.MODID + ".liquid_super_heat", "Liquid Super Heat");
        this.add("fluid." + CMConstants.MODID + ".liquid_low_heat", "Liquid Low Heat");
        
        this.add("item." + CMConstants.MODID + ".liquid_heat_bucket", "Liquid Heat Bucket");
        this.add("item." + CMConstants.MODID + ".liquid_super_heat_bucket", "Liquid Super Heat Bucket");
        this.add("item." + CMConstants.MODID + ".liquid_low_heat_bucket", "Liquid Low Heat Bucket");
        
        this.add(CMTinkersConstructBlocks.SEARED_BURNER.get(), "Seared Burner");
        
        this.add(CMConstants.MODID + ".itemGroup.tconstruct", "Create Mechanized: Tinkers' Construct");
        
        this.add(CMConstants.MODID + ".ponder.tag.tconstruct", "Tinkers' Construct");
        this.add(CMConstants.MODID + ".ponder.tag.tconstruct.description", "Components that interact with Tinkers' Construct");
        
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.header", "Heat the Seared Melter, the Create way.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_1", "You can use Blaze Burners to heat your melter.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_2", "The efficiency will change based on the Burner's Heat level.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_3", "Smouldering and Fading Blaze Burners will provide little to no heat, they can't even melt most basic metals.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_4", "Kindled Blaze Burners will provide as much heat as a bucket of Lava while active.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_5", "Seething Blaze Burners will provide 2.2x the heat a bucket of Lava while active.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_6", "All Heat level are considered fluids, and as such can interact with all Fluid based constructs.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_blaze_burner.text_7", "Burners are theoretically infinite, as long as they are powered.");
        
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.header", "Heat the Seared Melter, the Create Low-Heated way.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_1", "You can use Basic Burners to heat your melter.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_2", "The efficiency will change based on the Burner's Heat level.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_3", "Smouldering and Fading Basic Burners will provide little to no heat, they can't even melt most basic metals.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_4", "Kindled Basic Burners will provide as much heat as a bucket of Lava while active.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_5", "Seething Basic Burners will provide 2.2x the heat a bucket of Lava while active.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_6", "All Heat level are considered fluids, and as such can interact with all Fluid based constructs.");
        this.add(CMConstants.MODID + ".ponder.tconstruct/melter_basic_burner.text_7", "Burners are theoretically infinite, as long as they are powered.");
        
        new PonderLocalization().provideLang(CMConstants.MODID, this::add);
    }
}
