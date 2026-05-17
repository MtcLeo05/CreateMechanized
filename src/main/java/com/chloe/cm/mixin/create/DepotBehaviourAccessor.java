package com.chloe.cm.mixin.create;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(value = DepotBehaviour.class, remap = false)
public interface DepotBehaviourAccessor {
    
    @Accessor(value = "heldItem", remap = false)
    TransportedItemStack heldItem();
    
    @Accessor(value = "canAcceptItems", remap = false)
    Supplier<Boolean> canAcceptItems();
    
    @Accessor(value = "processingOutputBuffer", remap = false)
    ItemStackHandler processingOutputBuffer();
}
