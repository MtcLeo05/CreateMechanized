package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.ponder.AutoTerraPlatePonder;
import com.chloe.cm.impl.module.botania.ponder.ManaSpoutPonder;
import com.chloe.cm.impl.module.botania.ponder.TradingRiftPonder;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class CMBotaniaPonders implements PonderPlugin {
    @Override
    public String getModId() {
        return CMConstants.MODID;
    }
    
    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        register(helper);
    }
    
    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        register(helper);
    }
    
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<RegistryObject<?>> rHelper = helper.withKeyFunction(RegistryObject::getId);
        
        rHelper
            .forComponents(CMBotaniaBlocks.MANA_SPOUT)
            .addStoryBoard("botania/mana_manipulators/mana_spout", ManaSpoutPonder::ponder, MANA_MANIPULATORS)
            .addStoryBoard("botania/mana_manipulators/mana_spout_catalyst", ManaSpoutPonder::ponderCatalyst, MANA_MANIPULATORS);
        
        rHelper
            .forComponents(CMBotaniaBlocks.AUTO_TERRA_PLATE, CMBotaniaBlocks.MANA_PEDESTAL)
            .addStoryBoard("botania/mana_manipulators/auto_terra_plate", AutoTerraPlatePonder::ponder, MANA_MANIPULATORS)
            .addStoryBoard("botania/mana_manipulators/auto_terra_plate_overload", AutoTerraPlatePonder::ponderOverload, MANA_MANIPULATORS);
        
        rHelper
            .forComponents(CMBotaniaBlocks.TRADING_RIFT, CMBotaniaBlocks.TRADE_MANA_PEDESTAL, CMBotaniaBlocks.MANA_PEDESTAL)
            .addStoryBoard("botania/mana_manipulators/trading_rift", TradingRiftPonder::ponder, MANA_MANIPULATORS)
            .addStoryBoard("botania/mana_manipulators/trading_rift_overload", TradingRiftPonder::ponderOverload, MANA_MANIPULATORS);
    }
    
    private static final ResourceLocation MANA_MANIPULATORS = CMConstants.modLoc("botania/mana_manipulators");
    
    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryObject<?>> HELPER = helper.withKeyFunction(RegistryObject::getId);
        
        HELPER.registerTag(MANA_MANIPULATORS)
            .item(CMBotaniaBlocks.MANA_SPOUT.get(), true, true)
            .title("Mana Manipulators")
            .description("Components that interact with Botania's mana")
            .addToIndex()
            .register();
        
        HELPER.addToTag(MANA_MANIPULATORS)
            .add(CMBotaniaBlocks.MANA_SPOUT)
            .add(CMBotaniaBlocks.MANA_PEDESTAL)
            .add(CMBotaniaBlocks.AUTO_TERRA_PLATE)
            .add(CMBotaniaBlocks.TRADING_RIFT)
            .add(CMBotaniaBlocks.TRADE_MANA_PEDESTAL);
    }
    
}
