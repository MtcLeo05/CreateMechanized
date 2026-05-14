package com.chloe.cm.mixin.createlowheated;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import zeh.createlowheated.content.processing.basicburner.BasicBurnerBlockEntity;

@Mixin(value = BasicBurnerBlockEntity.class, remap = false)
public abstract class BasicBurnerBlockEntityMixin extends SmartBlockEntity implements IHaveGoggleInformation {
    
    public BasicBurnerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Redirect(method = "getCapability", at = @At(value = "INVOKE", target = "Lzeh/createlowheated/content/processing/basicburner/BasicBurnerBlockEntity;isItemHandlerCap(Lnet/minecraftforge/common/capabilities/Capability;)Z"))
    private boolean cm$getCapability(BasicBurnerBlockEntity instance, Capability capability) {
        if(capability != ForgeCapabilities.ITEM_HANDLER) return false;
        
        Level level = getLevel();
        BlockPos possibleTank = getBlockPos().above();
        
        return !level.getBlockState(possibleTank).is(TinkerSmeltery.searedMelter.get());
    }
    
}
