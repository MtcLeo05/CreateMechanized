package com.chloe.cm.impl.module.tconstructclh;

import com.chloe.cm.CMConstants;
import com.chloe.cm.api.module.IModule;
import com.chloe.cm.impl.config.Config;
import com.chloe.cm.impl.module.tconstruct.CMTinkersConstructModule;
import com.chloe.cm.impl.module.tconstructclh.handler.BasicBurnerHeatFluidHandler;
import com.chloe.cm.impl.module.tconstructclh.init.CMTinkersConstructCLHPonder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import zeh.createlowheated.content.processing.basicburner.BasicBurnerBlockEntity;

import java.util.List;
import java.util.function.Supplier;

public class CMTinkersConstructCreateLowHeatedModule implements IModule {
    
    public static CMTinkersConstructCreateLowHeatedModule INSTANCE = new CMTinkersConstructCreateLowHeatedModule();
    
    private CMTinkersConstructCreateLowHeatedModule() {
    }
    
    @Override
    public String modId() {
        return "createlowheated";
    }
    
    @Override
    public boolean enabled() {
        return ModList.get().isLoaded(modId()) && Config.INSTANCE.enableTCCreateLowHeated && CMTinkersConstructModule.INSTANCE.enabled();
    }
    
    @Override
    public void load(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, CMTinkersConstructCreateLowHeatedModule::attachCapabilities);
        bus.addListener(CMTinkersConstructCreateLowHeatedModule::clientInit);
    }
    
    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CMTinkersConstructCLHPonder());
    }
    
    @Override
    public List<? extends Supplier<? extends Block>> blocks() {
        return List.of();
    }
    
    public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();
        
        if(!(blockEntity instanceof BasicBurnerBlockEntity burner)) return;
        
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CMConstants.MODID, "basic_burner_liquid");
        
        LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() ->
            new BasicBurnerHeatFluidHandler(burner));
        
        ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<>() {
            
            @Override
            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
                if (cap == ForgeCapabilities.FLUID_HANDLER) {
                    return fluidHandler.cast();
                }
                return LazyOptional.empty();
            }
            
            @Override
            public CompoundTag serializeNBT() {
                return new CompoundTag();
            }
            
            @Override
            public void deserializeNBT(CompoundTag nbt) {
            }
        };
        
        event.addCapability(id, provider);
        event.addListener(fluidHandler::invalidate);
    }
}
