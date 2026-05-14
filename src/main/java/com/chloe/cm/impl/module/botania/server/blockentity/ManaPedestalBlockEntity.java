package com.chloe.cm.impl.module.botania.server.blockentity;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.server.behaviour.ManaPedestalBehaviour;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

import java.util.List;
import java.util.Optional;

public class ManaPedestalBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    
    private ManaPedestalBehaviour behaviour;
    private boolean overloaded = false;
    
    public ManaPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(CMBotaniaBlockEntities.MANA_PEDESTAL.get(), pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(behaviour = new ManaPedestalBehaviour(this));
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return behaviour.getItemCapability(cap, side);
        return super.getCapability(cap, side);
    }
    
    public ItemStack getItem() {
        return behaviour.getHeldItemStack();
    }
    
    public void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }
    
    public boolean isOverloaded() {
        return overloaded;
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("Overloaded", this.overloaded);
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.overloaded = compound.getBoolean("Overloaded");
    }
    
    private boolean containedManaTooltip(List<Component> tooltip) {
        if(!isOverloaded()) return false;
        
        CreateLang.translate("gui.goggles.mana_pedestal.angry.main")
            .style(ChatFormatting.DARK_RED)
            .forGoggles(tooltip);
        
        CreateLang.translate("gui.goggles.mana_pedestal.angry.sub")
            .style(ChatFormatting.DARK_RED)
            .forGoggles(tooltip);
        
        return true;
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedManaTooltip(tooltip);
    }
}
