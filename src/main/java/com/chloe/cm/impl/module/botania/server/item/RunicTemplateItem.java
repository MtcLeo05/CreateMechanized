package com.chloe.cm.impl.module.botania.server.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicTemplateItem extends Item {
    public static final String RUNE_KEY = "rune_id";
    
    public RunicTemplateItem(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Component getName(ItemStack pStack) {
        if(!pStack.hasTag()) return super.getName(pStack);
        
        CompoundTag tag = pStack.getTag();
        
        if(!tag.contains(RUNE_KEY)) return super.getName(pStack);
        
        Item rune = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(tag.getString(RUNE_KEY)));
        
        if(rune == null) return super.getName(pStack);
        
        return super.getName(pStack).copy().append(": ").append(rune.getName(rune.getDefaultInstance()));
    }
}
