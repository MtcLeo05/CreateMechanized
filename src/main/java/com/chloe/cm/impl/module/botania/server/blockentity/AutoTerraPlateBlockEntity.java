package com.chloe.cm.impl.module.botania.server.blockentity;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.mana.ManaReceiverWithCapacity;
import com.chloe.cm.impl.module.botania.server.behaviour.AutoTerraPlateBehaviour;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;

import java.util.List;
import java.util.Optional;

public class AutoTerraPlateBlockEntity extends KineticBlockEntity implements ManaReceiverWithCapacity, SparkAttachable {
    
    public static final int MAX_MANA = 1000000;
    
    private int mana;
    public float processingTicks = -1;
    
    private final LazyOptional<ManaReceiver> manaReceiver;
    private final LazyOptional<SparkAttachable> sparkAttachable;
    private AutoTerraPlateBehaviour behaviour;
    
    public ResourceLocation recipe = null;
    public boolean active;
    protected float transitional;
    
    public AutoTerraPlateBlockEntity(BlockPos pos, BlockState state) {
        super(CMBotaniaBlockEntities.AUTO_TERRA_PLATE.get(), pos, state);
        manaReceiver = LazyOptional.of(() -> this);
        sparkAttachable = LazyOptional.of(() -> this);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(behaviour = new AutoTerraPlateBehaviour(this));
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("ProcessingTicks", this.processingTicks);
        compound.putInt("Mana", this.mana);
        compound.putBoolean("Active", this.active);
        compound.putFloat("Transitional", this.transitional);
        
        if(recipe != null) compound.putString("Recipe", this.recipe.toString());
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.processingTicks = compound.getFloat("ProcessingTicks");
        this.mana = compound.getInt("Mana");
        this.active = compound.getBoolean("Active");
        this.transitional = compound.getFloat("Transitional");
        
        if(compound.contains("Recipe")) recipe = ResourceLocation.parse(compound.getString("Recipe"));
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
        
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return behaviour.getItemCapability(cap, side);
        }
        
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        manaReceiver.invalidate();
        sparkAttachable.invalidate();
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        
        return containedManaTooltip(tooltip, getCapability(BotaniaForgeCapabilities.MANA_RECEIVER));
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
    public int getMaxMana() {
        return MAX_MANA;
    }
    
    public void decreaseMana(int requiredMana) {
        this.mana -= requiredMana;
    }
    
    public void startTransition(float maxTransitional) {
        this.transitional = this.transitional > 0 ? maxTransitional - this.transitional : maxTransitional;
    }
    
    public float getTransitional() {
        return this.transitional;
    }
    
    public void tickTransitional() {
        if (this.transitional > 0) {
            this.transitional -= IRotate.SpeedLevel.FAST.getSpeedValue() / 100f;
            if (this.transitional < 0) this.transitional = 0;
        }
    }
}
