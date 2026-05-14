package com.chloe.cm.impl.module.botania.server.block;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaSpoutBlockEntity;
import com.chloe.cm.util.NonDirectionalDepotMethods;
import com.chloe.cm.util.ShapeUtil;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.depot.SharedDepotBlockMethods;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class ManaPedestalBlock extends Block implements IWrenchable, IBE<ManaPedestalBlockEntity> {
    public ManaPedestalBlock(Properties pProperties) {
        super(pProperties);
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(
            ShapeUtil.shapeFromDimension(2, 0, 2, 12, 16, 12)
        );
    }
    
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }
    
    @Override
    public Class<ManaPedestalBlockEntity> getBlockEntityClass() {
        return ManaPedestalBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<ManaPedestalBlockEntity> getBlockEntityType() {
        return CMBotaniaBlockEntities.MANA_PEDESTAL.get();
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        return NonDirectionalDepotMethods.onUse(state, world, pos, player, hand, ray);
    }
    
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    
    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        SharedDepotBlockMethods.onLanded(worldIn, entityIn);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return SharedDepotBlockMethods.getComparatorInputOverride(blockState, worldIn, pos);
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
