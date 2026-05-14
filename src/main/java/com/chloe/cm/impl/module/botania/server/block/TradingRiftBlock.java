package com.chloe.cm.impl.module.botania.server.block;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.chloe.cm.util.NonDirectionalDepotMethods;
import com.chloe.cm.util.ShapeUtil;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.logistics.depot.SharedDepotBlockMethods;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TradingRiftBlock extends KineticBlock implements IBE<TradingRiftBlockEntity> {
    public TradingRiftBlock(Properties pProperties) {
        super(pProperties);
        BlockStressValues.IMPACTS.register(this, () -> 4);
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(
            ShapeUtil.shapeFromDimension(0, 0, 0, 16, 4, 16)
        );
    }
    
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }
    
    @Override
    public Class<TradingRiftBlockEntity> getBlockEntityClass() {
        return TradingRiftBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<TradingRiftBlockEntity> getBlockEntityType() {
        return CMBotaniaBlockEntities.TRADING_RIFT.get();
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
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == face.getAxis();
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
    
    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }
}
