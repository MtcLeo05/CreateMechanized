package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CMTinkersConstructCreativeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CMConstants.MODID);
    
    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("tconstruct", () ->
        CreativeModeTab.builder()
            .title(Component.translatable(CMConstants.MODID + ".itemGroup.tconstruct"))
            .icon(() -> CMTinkersConstructFluids.LIQUID_HEAT_FLUID.getBucket().getDefaultInstance())
            .displayItems((idp, output) -> {
                output.accept(CMTinkersConstructBlocks.SEARED_BURNER.get());
                
                output.accept(CMTinkersConstructFluids.LIQUID_LOW_HEAT_FLUID.getBucket());
                output.accept(CMTinkersConstructFluids.LIQUID_HEAT_FLUID.getBucket());
                output.accept(CMTinkersConstructFluids.LIQUID_SUPER_HEAT_FLUID.getBucket());
            })
            .build()
    );
    
}
