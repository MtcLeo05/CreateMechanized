package com.chloe.cm.impl.module.manafluid.handler;

import com.chloe.cm.impl.module.botania.mana.ManaReceiverWithCapacity;
import com.halex.manafluid.index.FluidRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.mana.ManaReceiver;

public class GenericManaFluidHandler<T extends BlockEntity & ManaReceiverWithCapacity> implements IFluidHandler {
    
    private final T container;
    private double mb;
    
    public GenericManaFluidHandler(T container) {
        this.container = container;
        resyncFromContainer();
    }
    
    @Override
    public int getTanks() {
        return 1;
    }
    
    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            return new FluidStack(FluidRegistry.MANA_FLUID.get(), 0);
        }
        
        resyncFromContainer();
        
        int wholeMB = (int) Math.floor(mb);
        return new FluidStack(FluidRegistry.MANA_FLUID.get(), wholeMB);
    }
    
    @Override
    public int getTankCapacity(int tank) {
        return (tank == 0) ? container.getMaxMana() / 100 : 0;
    }
    
    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if (tank != 0) return false;
        return stack.getFluid().equals(FluidRegistry.MANA_FLUID.get());
    }
    
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.getFluid().equals(FluidRegistry.MANA_FLUID.get())) {
            return 0;
        }
        resyncFromContainer();
        
        int requestedMB = resource.getAmount();
        double oldMB = mb;
        double capacity = container.getMaxMana() / 100.0;
        double newMB = Math.min(oldMB + requestedMB, capacity);
        
        int wholeDelta = (int) Math.floor(newMB) - (int) Math.floor(oldMB);
        
        if (action == FluidAction.EXECUTE) {
            if (wholeDelta > 0) {
                container.receiveMana(wholeDelta * 100);
                container.setChanged();
            }
            mb = newMB;
        }
        return wholeDelta;
    }
    
    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.getFluid().equals(FluidRegistry.MANA_FLUID.get())) {
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
        
        if (action == FluidAction.EXECUTE) {
            if (wholeDelta > 0) {
                container.receiveMana(-wholeDelta * 100);
                container.setChanged();
            }
            if (newMB < 1) {
                newMB = 0;
            }
            mb = newMB;
        }
        
        return new FluidStack(FluidRegistry.MANA_FLUID.get(), wholeDelta);
    }
    
    private void resyncFromContainer() {
        mb = container.getCurrentMana() / 100.0;
    }
}
