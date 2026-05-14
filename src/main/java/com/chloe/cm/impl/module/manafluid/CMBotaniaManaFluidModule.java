package com.chloe.cm.impl.module.manafluid;

import com.chloe.cm.api.module.IModule;
import com.chloe.cm.impl.config.Config;
import com.chloe.cm.impl.module.botania.CMBotaniaModule;
import com.chloe.cm.impl.module.botania.mana.ManaReceiverWithCapacity;
import com.chloe.cm.impl.module.manafluid.handler.GenericManaFluidHandler;
import com.halex.manafluid.ModMain;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class CMBotaniaManaFluidModule implements IModule {
    
    public static CMBotaniaManaFluidModule INSTANCE = new CMBotaniaManaFluidModule();
    
    private CMBotaniaManaFluidModule(){
    }
    
    @Override
    public String modId() {
        return "manafluid";
    }
    
    @Override
    public boolean enabled() {
        return ModList.get().isLoaded(modId()) && Config.INSTANCE.enableManaFluid && CMBotaniaModule.INSTANCE.enabled();
    }
    
    @Override
    public void load(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, CMBotaniaManaFluidModule::attachCapabilities);
    }
    
    @Override
    public List<? extends Supplier<? extends Block>> blocks() {
        return List.of();
    }
    
    public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (!(event.getObject() instanceof ManaReceiverWithCapacity)) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        var blockEntity = (BlockEntity & ManaReceiverWithCapacity) event.getObject();
        
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModMain.MODID, "mana_fluid");
        
        LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() ->
            new GenericManaFluidHandler<>(blockEntity));
        
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
