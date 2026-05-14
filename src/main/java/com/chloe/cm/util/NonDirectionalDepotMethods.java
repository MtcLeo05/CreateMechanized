package com.chloe.cm.util;

import com.chloe.cm.mixin.DepotBehaviourAccessor;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.SharedDepotBlockMethods;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;

public class NonDirectionalDepotMethods {
    
    public static DepotBehaviour get(BlockGetter worldIn, BlockPos pos) {
        return BlockEntityBehaviour.get(worldIn, pos, DepotBehaviour.TYPE);
    }
    
    public static InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult ray) {
        
        if (world.isClientSide) return InteractionResult.SUCCESS;
        
        DepotBehaviour behaviour = get(world, pos);
        if (behaviour == null) return InteractionResult.PASS;
        
        DepotBehaviourAccessor accessor = (DepotBehaviourAccessor) behaviour;
        
        if (!accessor.canAcceptItems().get()) return InteractionResult.SUCCESS;
        
        ItemStack heldItem = player.getItemInHand(hand);
        boolean wasEmptyHanded = heldItem.isEmpty();
        boolean shouldntPlaceItem = AllBlocks.MECHANICAL_ARM.isIn(heldItem);
        
        ItemStack mainItemStack = behaviour.getHeldItemStack();
        if (!mainItemStack.isEmpty()) {
            player.getInventory()
                .placeItemBackInInventory(mainItemStack);
            behaviour.removeHeldItem();
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                1f + Create.RANDOM.nextFloat());
        }
        ItemStackHandler outputs = accessor.processingOutputBuffer();
        for (int i = 0; i < outputs.getSlots(); i++)
            player.getInventory()
                .placeItemBackInInventory(outputs.extractItem(i, 64, false));
        
        if (!wasEmptyHanded && !shouldntPlaceItem) {
            TransportedItemStack transported = new TransportedItemStack(heldItem);
            transported.insertedFrom = player.getDirection();
            transported.prevBeltPosition = .25f;
            transported.beltPosition = .25f;
            behaviour.setHeldItem(transported);
            player.setItemInHand(hand, ItemStack.EMPTY);
            AllSoundEvents.DEPOT_SLIDE.playOnServer(world, pos);
        }
        
        behaviour.blockEntity.notifyUpdate();
        return InteractionResult.SUCCESS;
    }
    
}
