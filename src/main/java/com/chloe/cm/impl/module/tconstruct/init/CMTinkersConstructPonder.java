package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstruct.ponder.BlazeBurnerMelterPonder;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class CMTinkersConstructPonder implements PonderPlugin {
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
            .forComponents(AllBlocks.BLAZE_BURNER)
            .addStoryBoard("tconstruct/melter_blaze_burner", BlazeBurnerMelterPonder::ponder);
        
        PonderSceneRegistrationHelper<ItemObject<?>> rHelper = helper.withKeyFunction(ItemObject::getId);
        
        rHelper
            .forComponents(TinkerSmeltery.searedMelter)
            .addStoryBoard("tconstruct/melter_blaze_burner", BlazeBurnerMelterPonder::ponder);
    }
    
}
