package com.chloe.cm.impl.module.botania.server.blockentity;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.mana.ManaReceiverWithCapacity;
import com.chloe.cm.impl.module.botania.server.behaviour.ManaSpoutBehaviour;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;

import java.util.List;
import java.util.Optional;

public class ManaSpoutBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, ManaReceiverWithCapacity, SparkAttachable {
    
    public static final int MAX_MANA = 100000;
    
    private int mana;
    protected ManaSpoutBehaviour behaviour;
    public int processingTicks = -1;
    public boolean sendSplash;
    
    private final LazyOptional<ManaReceiver> manaReceiver;
    private final LazyOptional<SparkAttachable> sparkAttachable;
    
    public ManaSpoutBlockEntity(BlockPos pos, BlockState state) {
        super(CMBotaniaBlockEntities.MANA_SPOUT.get(), pos, state);
        manaReceiver = LazyOptional.of(() -> this);
        sparkAttachable = LazyOptional.of(() -> this);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
        this.behaviour = new ManaSpoutBehaviour(this);
        list.add(this.behaviour);
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("ProcessingTicks", this.processingTicks);
        compound.putInt("Mana", this.mana);
        if (this.sendSplash && clientPacket) {
            compound.putBoolean("Splash", true);
            this.sendSplash = false;
        }
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.processingTicks = compound.getInt("ProcessingTicks");
        this.mana = compound.getInt("Mana");
    }
    
    @Override
    public Level getManaReceiverLevel() {
        return getLevel();
    }
    
    @Override
    public BlockPos getManaReceiverPos() {
        return getBlockPos();
    }
    
    @Override
    public int getCurrentMana() {
        return mana;
    }
    
    @Override
    public boolean isFull() {
        return mana >= getMaxMana();
    }
    
    @Override
    public void receiveMana(int mana) {
        this.mana = Math.min(this.mana + mana, getMaxMana());
        setChanged();
        notifyUpdate();
    }
    
    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
    
    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getAvailableSpaceForMana() {
        return Math.max(0, getMaxMana() - mana);
    }
    
    @Override
    public ManaSpark getAttachedSpark() {
        if (level == null) return null;
        List<Entity> sparks = level.getEntities((Entity) null, new AABB(worldPosition.above(), worldPosition.above().offset(1, 1, 1)), e -> e instanceof ManaSpark);
        if (sparks.size() == 1) {
            return (ManaSpark) sparks.get(0);
        }
        return null;
    }
    
    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            return manaReceiver.cast();
        }
        if (cap == BotaniaForgeCapabilities.SPARK_ATTACHABLE) {
            return sparkAttachable.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        manaReceiver.invalidate();
        sparkAttachable.invalidate();
    }
    
    private boolean containedManaTooltip(List<Component> tooltip, LazyOptional<ManaReceiver> handler) {
        Optional<ManaReceiver> resolve = handler.resolve();
        if (resolve.isEmpty())
            return false;
        
        ManaReceiver receiver = resolve.get();
        
        CreateLang.translate("gui.goggles.mana_container")
            .forGoggles(tooltip);
        
        if (receiver.getCurrentMana() == 0) {
            CreateLang.translate("gui.goggles.mana_container.capacity")
                .add(
                    CreateLang.number(getMaxMana())
                        .style(ChatFormatting.BLUE)
                )
                .forGoggles(tooltip, 1);
            
            return true;
        }
        
        CreateLang.translate("gui.goggles.mana_container.mana")
            .add(
                CreateLang.number(receiver.getCurrentMana())
                    .style(ChatFormatting.AQUA)
            )
            .text(ChatFormatting.GRAY, " / ")
            .add(
                CreateLang.number(getMaxMana())
                    .style(ChatFormatting.BLUE)
            ).forGoggles(tooltip, 1);
        
        return true;
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedManaTooltip(tooltip, getCapability(BotaniaForgeCapabilities.MANA_RECEIVER));
    }
    
    @Override
    public int getMaxMana() {
        return MAX_MANA;
    }
    
    public void decreaseMana(int requiredMana) {
        this.mana -= requiredMana;
    }
}
