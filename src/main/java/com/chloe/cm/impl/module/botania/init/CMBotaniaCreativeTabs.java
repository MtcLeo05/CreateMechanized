package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CMBotaniaCreativeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CMConstants.MODID);
    
    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("botania", () ->
        CreativeModeTab.builder()
            .title(Component.translatable(CMConstants.MODID + ".itemGroup.botania"))
            .icon(() -> CMBotaniaBlocks.MANA_SPOUT.get().asItem().getDefaultInstance())
            .displayItems((idp, output) -> {
                output.accept(CMBotaniaBlocks.MANA_SPOUT.get());
                output.accept(CMBotaniaItems.RUNIC_TEMPLATE.get());
                output.accept(CMBotaniaBlocks.MANA_PEDESTAL.get());
                output.accept(CMBotaniaBlocks.AUTO_TERRA_PLATE.get());
                output.accept(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get());
                output.accept(CMBotaniaBlocks.TRADING_RIFT.get());
            })
            .build()
    );
    
}
