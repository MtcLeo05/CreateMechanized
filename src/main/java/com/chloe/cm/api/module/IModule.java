package com.chloe.cm.api.module;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;
import java.util.function.Supplier;

public interface IModule {
    String modId();
    boolean enabled();
    
    void load(IEventBus bus);
    
    //Generic jank my beloved, not
    List<? extends Supplier<? extends Block>> blocks();
}
