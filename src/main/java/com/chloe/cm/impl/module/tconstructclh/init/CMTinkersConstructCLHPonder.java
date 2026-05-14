package com.chloe.cm.impl.module.tconstructclh.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstructclh.ponder.BasicBurnerMelterPonder;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import zeh.createlowheated.AllBlocks;

public class CMTinkersConstructCLHPonder implements PonderPlugin {
    @Override
    public String getModId() {
        return CMConstants.MODID;
    }
    
    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        register(helper);
    }
    
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<BlockEntry<?>> rHelperBE = helper.withKeyFunction(BlockEntry::getId);
        
        rHelperBE
            .forComponents(AllBlocks.BASIC_BURNER)
            .addStoryBoard("tconstruct/melter_basic_burner", BasicBurnerMelterPonder::ponder);
        
        PonderSceneRegistrationHelper<ItemObject<?>> rHelper = helper.withKeyFunction(ItemObject::getId);
        
        rHelper
            .forComponents(TinkerSmeltery.searedMelter)
            .addStoryBoard("tconstruct/melter_basic_burner", BasicBurnerMelterPonder::ponder);
    }
}
