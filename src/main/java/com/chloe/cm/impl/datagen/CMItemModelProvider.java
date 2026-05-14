package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class CMItemModelProvider extends ItemModelProvider {
    public CMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CMConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    
        simpleItem(CMBotaniaItems.RUNIC_TEMPLATE, "botania");
        simpleItem(CMBotaniaItems.UNETCHED_RUNE, "botania");
        
    }

    private void simpleItem(RegistryObject<? extends Item> item, String nms) {
        simpleItem(item.getId().getPath(), nms);
    }

    private void simpleItem(String name, String nms) {
        withExistingParent(name,
            ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
            CMConstants.modLoc("item/" + nms + "/" + name));
    }
}
