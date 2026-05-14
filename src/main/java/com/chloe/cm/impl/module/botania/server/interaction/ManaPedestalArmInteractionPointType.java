package com.chloe.cm.impl.module.botania.server.interaction;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ManaPedestalArmInteractionPointType extends ArmInteractionPointType {
    @Override
    public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
        return state.is(CMBotaniaBlocks.MANA_PEDESTAL.get());
    }
    
    @Override
    public @Nullable ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
        return new AllArmInteractionPointTypes.DepotPoint(this, level, pos, state);
    }
}
