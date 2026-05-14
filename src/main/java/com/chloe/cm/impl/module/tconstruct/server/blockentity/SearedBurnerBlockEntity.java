package com.chloe.cm.impl.module.tconstruct.server.blockentity;

import com.chloe.cm.impl.module.tconstruct.handler.BlazeBurnerHeatFluidHandler;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlockEntities;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.common.multiblock.IMasterLogic;
import slimeknights.tconstruct.common.multiblock.IServantLogic;
import slimeknights.tconstruct.library.utils.TagUtil;

public class SearedBurnerBlockEntity extends BlazeBurnerBlockEntity implements IServantLogic {
    @Nullable
    private BlockPos masterPos;
    @Nullable
    private Block masterBlock;
    
    private int mode = 0;
    
    public SearedBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(CMTinkersConstructBlockEntities.SEARED_BURNER.get(), pos, state);
    }
    
    LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new BlazeBurnerHeatFluidHandler(this));
    
    public static void updateNeighbors(Level world, BlockPos pos, BlockState state) {
        for(Direction direction : Direction.values()) {
            BlockEntity tileEntity = world.getBlockEntity(pos.relative(direction));
            if (tileEntity instanceof IMasterLogic master) {
                master.notifyChange(pos, state);
                break;
            }
            
            if (tileEntity instanceof SearedBurnerBlockEntity component) {
                if (component.hasMaster()) {
                    component.notifyMasterOfChange(pos, state);
                    break;
                }
            }
        }
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        fluidHandler.invalidate();
    }
    
    @Override
    public void applyCreativeFuel() {
        super.applyCreativeFuel();
    }
    
    @Override
    public boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate) {
        return super.tryUpdateFuel(itemStack, forceOverflow, simulate);
    }
    
    public LerpedFloat getHeadAngle() {
        return this.headAngle;
    }
    
    public boolean hasMaster() {
        return this.masterPos != null;
    }
    
    protected void setMaster(@Nullable BlockPos master, @Nullable Block block) {
        this.masterPos = master;
        this.masterBlock = block;
        this.notifyUpdate();
    }
    
    protected boolean validateMaster() {
        if (this.masterPos == null) {
            return false;
        } else {
            assert this.level != null;
            
            if (this.level.getBlockState(this.masterPos).getBlock() == this.masterBlock) {
                return true;
            } else {
                this.setMaster(null, null);
                return false;
            }
        }
    }
    
    public boolean isValidMaster(IMasterLogic master) {
        return !this.validateMaster() || master.getMasterPos().equals(this.masterPos);
    }
    
    @Override
    public @Nullable BlockPos getMasterPos() {
        return masterPos;
    }
    
    public void notifyMasterOfChange(BlockPos pos, BlockState state) {
        if (this.validateMaster()) {
            assert this.masterPos != null;
            
            BlockEntityHelper.get(IMasterLogic.class, this.level, this.masterPos).ifPresent((te) -> te.notifyChange(pos, state));
        }
    }
    
    public void setPotentialMaster(IMasterLogic master) {
        BlockPos newMaster = master.getMasterPos();
        if (newMaster.equals(this.masterPos)) {
            this.masterBlock = master.getMasterBlock().getBlock();
            this.notifyUpdate();
        } else if (!this.validateMaster()) {
            this.setMaster(newMaster, master.getMasterBlock().getBlock());
        }
    }
    
    public void removeMaster(IMasterLogic master) {
        if (this.masterPos != null && this.masterPos.equals(master.getMasterPos())) {
            this.setMaster(null, null);
        }
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        readMaster(compound);
        if(compound.contains("Mode")) this.mode = compound.getInt("Mode");
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        writeMaster(compound);
        compound.putInt("Mode", this.mode);
    
    }
    
    public int getMode() {
        return mode;
    }
    
    protected void readMaster(CompoundTag tags) {
        BlockPos masterPos = TagUtil.readOptionalPos(tags, "masterOffset", this.worldPosition);
        Block masterBlock = null;
        if (masterPos != null && tags.contains("masterBlock", 8)) {
            ResourceLocation masterBlockName = ResourceLocation.tryParse(tags.getString("masterBlock"));
            if (masterBlockName != null && ForgeRegistries.BLOCKS.containsKey(masterBlockName)) {
                masterBlock = ForgeRegistries.BLOCKS.getValue(masterBlockName);
            }
        }
        
        if (masterBlock != null) {
            this.masterPos = masterPos;
            this.masterBlock = masterBlock;
        }
        
    }
    
    protected CompoundTag writeMaster(CompoundTag tags) {
        if (this.masterPos != null && this.masterBlock != null) {
            tags.put("masterOffset", NbtUtils.writeBlockPos(this.masterPos.subtract(this.worldPosition)));
            tags.putString("masterBlock", BuiltInRegistries.BLOCK.getKey(this.masterBlock).toString());
        }
        
        return tags;
    }
    
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
        notifyMasterOfChange(getBlockPos(), getBlockState());
    }
    
    public void increaseMode() {
        mode = (mode + 1) % 7;
        notifyUpdate();
    }
}

