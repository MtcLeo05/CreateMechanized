package com.chloe.cm.mixin.botania;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(value = PylonBlockEntity.class, remap = false)
public interface PylonBlockEntityAccessor {
    
    @Accessor(value = "activated", remap = false)
    void setActivated(boolean act);
    
    @Accessor(value = "centerPos", remap = false)
    void setCenterPos(BlockPos pos);
}
