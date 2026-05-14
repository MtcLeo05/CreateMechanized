package com.chloe.cm.impl.event.custom;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.Event;

public class AfterLoadResourceEvent extends Event {
    
    private final RegistryAccess access;
    private final RecipeManager manager;
    
    public AfterLoadResourceEvent(RegistryAccess access, RecipeManager manager) {
        this.access = access;
        this.manager = manager;
    }
    
    public RegistryAccess getRegistryAccess() {
        return access;
    }
    
    public RecipeManager getRecipeManager() {
        return manager;
    }
}
