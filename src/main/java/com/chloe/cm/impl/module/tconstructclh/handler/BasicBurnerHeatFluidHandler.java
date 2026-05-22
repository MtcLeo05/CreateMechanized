package com.chloe.cm.impl.module.tconstructclh.handler;

import com.chloe.cm.impl.module.tconstruct.CMTinkersConstructModule;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructFluids;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import zeh.createlowheated.content.processing.basicburner.BasicBurnerBlockEntity;

public class BasicBurnerHeatFluidHandler implements IFluidHandler {
    
    private final BasicBurnerBlockEntity burner;
    private double mb;
    
    public BasicBurnerHeatFluidHandler(BasicBurnerBlockEntity burner) {
        this.burner = burner;
        resyncFromContainer();
    }
    
    @Override
    public int getTanks() {
        return 1;
    }
    
    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            return FluidStack.EMPTY;
        }
        
        resyncFromContainer();
        
        int wholeMB = (int) Math.floor(mb);
        return getFluid() != null? new FluidStack(getFluid(), wholeMB): FluidStack.EMPTY;
    }
    
    @Override
    public int getTankCapacity(int tank) {
        return (tank == 0) ? 1 : 0;
    }
    
    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if (tank != 0) return false;
        return stack.getFluid().equals(getFluid());
    }
    
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return -1;
    }
    
    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.getFluid().equals(getFluid())) {
            return FluidStack.EMPTY;
        }
        
        return drain(resource.getAmount(), action);
    }
    
    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        resyncFromContainer();
        
        double oldMB = mb;
        double newMB = Math.max(oldMB - maxDrain, 0);
        int wholeDelta = (int) Math.floor(oldMB) - (int) Math.floor(newMB);
        
        return getFluid() != null? new FluidStack(getFluid(), wholeDelta): FluidStack.EMPTY;
    }

    private void resyncFromContainer() {
        BlazeBurnerBlock.HeatLevel heat = burner.getHeatLevelFromBlock();
        
        if (heat == BlazeBurnerBlock.HeatLevel.valueOf("SMOULDERING") ||
            heat == BlazeBurnerBlock.HeatLevel.valueOf("FADING") ||
            CMTinkersConstructModule.isLow(heat)) {
            mb = 1;
            return;
        }
        
        if(heat == BlazeBurnerBlock.HeatLevel.valueOf("KINDLED")) {
            mb = 1;
            return;
        }
        
        if(heat == BlazeBurnerBlock.HeatLevel.valueOf("SEETHING")) {
            mb = 1;
            return;
        }
        
        mb = 0;
    }
    
    private Fluid getFluid() {
        BlazeBurnerBlock.HeatLevel heat = burner.getHeatLevelFromBlock();
        
        if (heat == BlazeBurnerBlock.HeatLevel.valueOf("SMOULDERING") ||
            heat == BlazeBurnerBlock.HeatLevel.valueOf("FADING") ||
            CMTinkersConstructModule.isLow(heat)) {
            return CMTinkersConstructFluids.LIQUID_LOW_HEAT_FLUID.get();
        }
        
        if(heat == BlazeBurnerBlock.HeatLevel.valueOf("KINDLED")) {
            return burner.getEmpoweredFromBlock()? CMTinkersConstructFluids.LIQUID_HEAT_FLUID.get(): CMTinkersConstructFluids.LIQUID_SUPER_HEAT_FLUID.get();
        }
        
        if(heat == BlazeBurnerBlock.HeatLevel.valueOf("SEETHING")) {
            return CMTinkersConstructFluids.LIQUID_SUPER_HEAT_FLUID.get();
        }
        
        return null;
    }
}
