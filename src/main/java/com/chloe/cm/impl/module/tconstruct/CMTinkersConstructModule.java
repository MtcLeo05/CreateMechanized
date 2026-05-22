package com.chloe.cm.impl.module.tconstruct;

import com.chloe.cm.CMConstants;
import com.chloe.cm.api.module.IModule;
import com.chloe.cm.impl.config.Config;
import com.chloe.cm.impl.module.tconstruct.client.render.be.SearedBurnerBlockEntityRenderer;
import com.chloe.cm.impl.module.tconstruct.handler.BlazeBurnerHeatFluidHandler;
import com.chloe.cm.impl.module.tconstruct.init.*;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
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

import java.util.List;
import java.util.function.Supplier;

public class CMTinkersConstructModule implements IModule {
    
    public static CMTinkersConstructModule INSTANCE = new CMTinkersConstructModule();
    
    private CMTinkersConstructModule() {
    }
    
    @Override
    public String modId() {
        return "tconstruct";
    }
    
    @Override
    public boolean enabled() {
        return ModList.get().isLoaded(modId()) && Config.INSTANCE.enableTinkersConstruct;
    }
    
    @Override
    public void load(IEventBus bus) {
        CMTinkersConstructBlockEntities.BLOCK_ENTITIES.register(bus);
        CMTinkersConstructBlocks.BLOCKS.register(bus);
        CMTinkersConstructItems.ITEMS.register(bus);
        CMTinkersConstructFluids.FLUIDS.register(bus);
        CMTinkersConstructCreativeTabs.CREATIVE_MODE_TABS.register(bus);
        
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, CMTinkersConstructModule::attachCapabilities);
        bus.addListener(CMTinkersConstructModule::clientInit);
        bus.addListener(CMTinkersConstructModule::registerBER);
        bus.addListener(CMTinkersConstructModule::registerModel);
    }
    
    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CMTinkersConstructPonder());
    }
    
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CMTinkersConstructBlockEntities.SEARED_BURNER.get(), SearedBurnerBlockEntityRenderer::new);
    }
    
    public static void registerModel(ModelEvent.RegisterAdditional event) {
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/bricks"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/cobble"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/cracked_bricks"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/fancy_bricks"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/paver"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/stone"));
        event.register(CMConstants.modLoc("block/tconstruct/seared_burner/triangle_bricks"));
    }
    
    @Override
    public List<? extends Supplier<? extends Block>> blocks() {
        return List.of(
            CMTinkersConstructBlocks.SEARED_BURNER
        );
    }
    
    public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();
        
        if(!(blockEntity instanceof BlazeBurnerBlockEntity burner)) return;
        
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CMConstants.MODID, "blaze_burner_liquid");
        
        LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() ->
            new BlazeBurnerHeatFluidHandler(burner));
        
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
    
    public static boolean isLow(BlazeBurnerBlock.HeatLevel heat) {
        if(ModList.get().isLoaded("createlowheated")) return heat == BlazeBurnerBlock.HeatLevel.valueOf("LOW");
        
        return false;
    }
}
