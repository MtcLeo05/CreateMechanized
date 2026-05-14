package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstruct.ponder.BlazeBurnerMelterPonder;
import com.chloe.cm.impl.module.tconstruct.ponder.SearedBurnerPonder;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
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
        
        PonderSceneRegistrationHelper<ItemObject<?>> rHelperIO = helper.withKeyFunction(ItemObject::getId);
        
        rHelperIO
            .forComponents(TinkerSmeltery.searedMelter)
            .addStoryBoard("tconstruct/melter_blaze_burner", BlazeBurnerMelterPonder::ponder);
        
        PonderSceneRegistrationHelper<RegistryObject<?>> rHelperRO = helper.withKeyFunction(RegistryObject::getId);
        
        rHelperRO
            .forComponents(CMTinkersConstructBlocks.SEARED_BURNER)
            .addStoryBoard("tconstruct/seared_burner", SearedBurnerPonder::ponder);
    }
    
}
