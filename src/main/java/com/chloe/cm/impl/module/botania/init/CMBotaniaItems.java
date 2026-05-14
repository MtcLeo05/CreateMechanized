package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.item.RunicTemplateItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CMBotaniaItems {
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CMConstants.MODID);
    
    public static final RegistryObject<Item> RUNIC_TEMPLATE = ITEMS.register("runic_template", () ->
        new RunicTemplateItem(new Item.Properties())
    );
    
    public static final RegistryObject<Item> UNETCHED_RUNE = ITEMS.register("unetched_rune", () ->
        new SequencedAssemblyItem(new Item.Properties())
    );
}
