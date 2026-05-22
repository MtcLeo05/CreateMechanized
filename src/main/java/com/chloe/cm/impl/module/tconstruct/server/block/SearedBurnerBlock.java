package com.chloe.cm.impl.module.tconstruct.server.block;

import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlockEntities;
import com.chloe.cm.impl.module.tconstruct.server.blockentity.SearedBurnerBlockEntity;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.mantle.util.BlockEntityHelper;

import javax.annotation.Nullable;

public class SearedBurnerBlock extends HorizontalDirectionalBlock implements IBE<SearedBurnerBlockEntity>, IWrenchable {
    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);
    public static final BooleanProperty IN_STRUCTURE = BooleanProperty.create("in_structure");
    
    public SearedBurnerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SMOULDERING).setValue(IN_STRUCTURE, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HEAT_LEVEL, FACING, IN_STRUCTURE);
    }
    
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        SearedBurnerBlockEntity.updateNeighbors(world, pos, state);
    }
    
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        if (world.isClientSide)
            return;
        BlockEntity blockEntity = world.getBlockEntity(pos.above());
        if (!(blockEntity instanceof BasinBlockEntity basin))
            return;
        basin.notifyChangeOfContents();
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        BlockEntityHelper.get(SearedBurnerBlockEntity.class, pLevel, pPos).ifPresent((te) -> te.notifyMasterOfChange(pPos, pNewState));
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
    
    @Override
    public Class<SearedBurnerBlockEntity> getBlockEntityClass() {
        return SearedBurnerBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<SearedBurnerBlockEntity> getBlockEntityType() {
        return CMTinkersConstructBlockEntities.SEARED_BURNER.get();
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult blockRayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        
        if (AllItems.GOGGLES.isIn(heldItem))
            return onBlockEntityUse(world, pos, bbte -> {
                if (bbte.goggles)
                    return InteractionResult.PASS;
                bbte.goggles = true;
                bbte.notifyUpdate();
                return InteractionResult.SUCCESS;
            });
        
        if (heldItem.isEmpty())
            return onBlockEntityUse(world, pos, bbte -> {
                if (!bbte.goggles)
                    return InteractionResult.PASS;
                bbte.goggles = false;
                bbte.notifyUpdate();
                return InteractionResult.SUCCESS;
            });
        
        boolean doNotConsume = player.isCreative();
        boolean forceOverflow = !(player instanceof FakePlayer);
        
        if(heldItem.is(AllTags.AllItemTags.WRENCH.tag)) {
            BlockEntityHelper.get(SearedBurnerBlockEntity.class, world, pos).ifPresent(SearedBurnerBlockEntity::increaseMode);
        }
        
        InteractionResultHolder<ItemStack> res =
            tryInsert(state, world, pos, heldItem, doNotConsume, forceOverflow, false);
        ItemStack leftover = res.getObject();
        
        if (!world.isClientSide && !doNotConsume && !leftover.isEmpty()) {
            if (heldItem.isEmpty()) {
                player.setItemInHand(hand, leftover);
            } else if (!player.getInventory()
                .add(leftover)) {
                player.drop(leftover, false);
            }
        }
        
        BlockEntityHelper.get(SearedBurnerBlockEntity.class, world, pos).ifPresent((te) -> te.notifyMasterOfChange(pos, state));
        return res.getResult() == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
    
    public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos,
                                                               ItemStack stack, boolean doNotConsume, boolean forceOverflow, boolean simulate) {
        if (!state.hasBlockEntity())
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof SearedBurnerBlockEntity burnerBE))
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        
        if (burnerBE.isCreativeFuel(stack)) {
            if (!simulate)
                burnerBE.applyCreativeFuel();
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
        if (!burnerBE.tryUpdateFuel(stack, forceOverflow, simulate))
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        
        if (!doNotConsume) {
            ItemStack container = stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem() : ItemStack.EMPTY;
            if (!world.isClientSide) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(container);
        }
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        Item item = stack.getItem();
        BlockState defaultState = defaultBlockState();
        if (!(item instanceof BlazeBurnerBlockItem))
            return defaultState;
        
        return defaultState.setValue(HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SMOULDERING)
            .setValue(FACING, context.getHorizontalDirection()
                .getOpposite());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return AllShapes.HEATER_BLOCK_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext p_220071_4_) {
        if (p_220071_4_ == CollisionContext.empty())
            return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE;
        return getShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
        return Math.max(0, state.getValue(HEAT_LEVEL)
            .ordinal() - 1);
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0)
            return;
        if (!state.getValue(HEAT_LEVEL)
            .isAtLeast(BlazeBurnerBlock.HeatLevel.SMOULDERING))
            return;
        world.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F,
            pos.getZ() + 0.5F, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
            0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
    }
    
    public static int getLight(BlockState state) {
        BlazeBurnerBlock.HeatLevel level = state.getValue(HEAT_LEVEL);
        return switch (level) {
            case SMOULDERING -> 8;
            default -> 15;
        };
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
