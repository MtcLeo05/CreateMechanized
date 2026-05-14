package com.chloe.cm.impl.module.botania.server.block;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaSpoutBlockEntity;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaSpoutBlock extends Block implements IWrenchable, IBE<ManaSpoutBlockEntity> {
    public ManaSpoutBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.SPOUT;
    }
    
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }
    
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    @Override
    public Class<ManaSpoutBlockEntity> getBlockEntityClass() {
        return ManaSpoutBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<ManaSpoutBlockEntity> getBlockEntityType() {
        return CMBotaniaBlockEntities.MANA_SPOUT.get();
    }
    
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return getBlockEntityOptional(pLevel, pPos).map(be -> Mth.floor(be.getCurrentMana() / (float) be.getMaxMana() * 14.0F) + (be.getCurrentMana() > 0 ? 1 : 0)).orElse(0);
    }
}
