package com.chloe.cm.mixin.botania;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(PylonBlockEntity.class)
public class PylonBlockEntityMixin {
    
    @Redirect(method = "commonTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean cm$isVariant(BlockState instance, Block block) {
        return instance.is(block) || instance.is(CMBotaniaBlocks.TRADING_RIFT.get());
    }
    
}
