package com.chloe.cm.impl.module.botania.server.blockentity;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.server.behaviour.AutoTerraPlateBehaviour;
import com.chloe.cm.impl.module.botania.server.behaviour.TradingRiftBehaviour;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradingRiftBlockEntity extends KineticBlockEntity {
    public float processingTicks = -1;
    
    private TradingRiftBehaviour behaviour;
    
    public ResourceLocation recipe = null;
    public boolean active;
    protected float transitional;
    
    public final List<BlockPos> tradePeds = new ArrayList<>();
    public final List<BlockPos> inputPeds = new ArrayList<>();
    
    public TradingRiftBlockEntity(BlockPos pos, BlockState state) {
        super(CMBotaniaBlockEntities.TRADING_RIFT.get(), pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(behaviour = new TradingRiftBehaviour(this));
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("ProcessingTicks", this.processingTicks);
        compound.putBoolean("Active", this.active);
        compound.putFloat("Transitional", this.transitional);
        
        if(recipe != null) compound.putString("Recipe", this.recipe.toString());
        
        ListTag tradeTag = new ListTag();
        for (BlockPos p : tradePeds) {
            tradeTag.add(LongTag.valueOf(p.asLong()));
        }
        compound.put("TradePedestals", tradeTag);
        
        ListTag inputTag = new ListTag();
        for (BlockPos p : inputPeds) {
            inputTag.add(LongTag.valueOf(p.asLong()));
        }
        compound.put("InputPedestals", inputTag);
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.processingTicks = compound.getFloat("ProcessingTicks");
        this.active = compound.getBoolean("Active");
        this.transitional = compound.getFloat("Transitional");
        
        if(compound.contains("Recipe")) recipe = ResourceLocation.parse(compound.getString("Recipe"));
        
        tradePeds.clear();
        if (compound.contains("TradePedestals")) {
            ListTag tag = (ListTag) compound.get("TradePedestals");
            tag.forEach(t -> tradePeds.add(BlockPos.of(((LongTag) t).getAsLong())));
        }
        
        inputPeds.clear();
        if (compound.contains("InputPedestals")) {
            ListTag tag = (ListTag) compound.get("InputPedestals");
            tag.forEach(t -> inputPeds.add(BlockPos.of(((LongTag) t).getAsLong())));
        }
    }
    
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
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
