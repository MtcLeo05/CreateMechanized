package com.chloe.cm.impl.module.botania.server.behaviour;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlockEntities;
import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradeManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.chloe.cm.impl.module.botania.server.recipe.TradingRiftRecipe;
import com.chloe.cm.mixin.botania.PylonBlockEntityAccessor;
import com.google.common.collect.Queues;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;

import java.util.*;

public class TradingRiftBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<TradingRiftBehaviour> TYPE = new BehaviourType<>();
    
    private final List<BlockPos> cachedPylonPositions = new ArrayList<>();
    
    public TradingRiftBehaviour(SmartBlockEntity be) {
        super(be);
    }
    
    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
    
    private Optional<TradingRiftRecipe> getRecipe() {
        if (blockEntity.getLevel() == null) return Optional.empty();
        
        TradingRiftBlockEntity be = getBE();
        Level level = be.getLevel();
        
        if (be.recipe != null) {
            return (Optional<TradingRiftRecipe>) be.getLevel().getRecipeManager().byKey(be.recipe);
        }
        
        List<Recipe<?>> allRecipes = new ArrayList<>(level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.TRADING_RIFT_TYPE.getType()));
        
        if (allRecipes.isEmpty()) return Optional.empty();
        
        List<BlockPos> pedestals = nearbyPedestals();
        
        List<ItemStack> items = new ArrayList<>();
        
        pedestals.forEach(p -> {
                if (blockEntity.getLevel().getBlockEntity(p) instanceof ManaPedestalBlockEntity ped) {
                    for (int count = ped.getItem().getCount(); count > 0; count--) {
                        items.add(
                            ped.getItem().copyWithCount(1)
                        );
                    }
                }
            }
        );
        
        SimpleContainer cont = new SimpleContainer(items.toArray(new ItemStack[]{}));
        
        for (Recipe<?> r : allRecipes) {
            TradingRiftRecipe recipe = ((TradingRiftRecipe) r);
            
            if (items.size() < recipe.getIngredients().size()) continue;
            
            int outputs = recipe.getRollableResultsAsItemStacks().size();
            
            if (recipe.matches(cont, level)) {
                for (BlockPos ped : nearbyTradePedestals()) {
                    Optional<TradeManaPedestalBlockEntity> en = blockEntity.getLevel().getBlockEntity(ped, CMBotaniaBlockEntities.TRADE_MANA_PEDESTAL.get());
                    
                    if (en.isEmpty()) continue;
                    
                    TradeManaPedestalBlockEntity tPed = en.get();
                    
                    ItemStack currentItem = tPed.getItem();
                    
                    if (currentItem.isEmpty()) {
                        if (!be.tradePeds.contains(ped)) be.tradePeds.add(ped);
                        outputs--;
                    }
                    
                    ItemStack result = recipe.getResultItem(level.registryAccess());
                    
                    if (!ItemStack.isSameItemSameTags(currentItem, result)) continue;
                    
                    if (currentItem.getCount() + result.getCount() > currentItem.getMaxStackSize()) continue;
                    
                    if (!be.tradePeds.contains(ped)) be.tradePeds.add(ped);
                    outputs--;
                }
            }
            
            if (outputs <= 0) return Optional.of(recipe);
        }
        
        
        return Optional.empty();
    }
    
    @Override
    public void tick() {
        if (blockEntity.getLevel().isClientSide) return;
        TradingRiftBlockEntity be = getBE();
        
        if (be.getTransitional() > 0) {
            be.tickTransitional();
            be.notifyUpdate();
        }
        
        boolean hasManaToOpen = consumeMana(locatePylons(true), AlfheimPortalBlockEntity.MANA_COST_OPENING, true);
        
        if (be.active) {
            if (!be.isSpeedRequirementFulfilled() || !hasManaToOpen) {
                be.active = false;
                be.startTransition(40f);
                be.notifyUpdate();
                return;
            }
        }
        
        if (!be.active) {
            if (be.isSpeedRequirementFulfilled() && hasManaToOpen) {
                be.active = true;
                be.startTransition(40f);
                be.notifyUpdate();
                return;
            }
        }
        
        if (be.active && be.getTransitional() == 1) {
            consumeMana(locatePylons(true), AlfheimPortalBlockEntity.MANA_COST_OPENING, false);
        }
        
        if (!be.active || be.getTransitional() > 0) return;
        
        Optional<TradingRiftRecipe> rOpt = getRecipe();
        if (rOpt.isEmpty()) return;
        
        TradingRiftRecipe recipe = rOpt.get();
        
        if (be.processingTicks == -1) {
            if (!consumeMana(locatePylons(true), recipe.getManaRequired(), true)) return;
            
            be.processingTicks = 40;
            
            List<BlockPos> pos = nearbyPedestals();
            consumeMana(locatePylons(true), recipe.getManaRequired(), true);
            
            be.inputPeds.clear();
            
            Queue<Ingredient> objects = Queues.newArrayDeque(recipe.getIngredients());
            
            first:
            while (!objects.isEmpty()) {
                Ingredient peek = objects.peek();
                
                for (BlockPos p : pos) {
                    ManaPedestalBlockEntity pedestal = (ManaPedestalBlockEntity) blockEntity.getLevel().getBlockEntity(p);
                    
                    if (peek.test(pedestal.getItem())) {
                        pedestal.getItem().shrink(1);
                        pedestal.notifyUpdate();
                        objects.remove(peek);
                        be.inputPeds.add(p);
                        continue first;
                    }
                }
            }
            
            be.recipe = recipe.getId();
            
            be.notifyUpdate();
            return;
        }
        
        if (be.tradePeds.isEmpty()) return;
        
        be.processingTicks -= Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue();
        be.notifyUpdate();
        
        if (be.processingTicks > 0) return;
        
        ArrayDeque<ItemStack> outputs = Queues.newArrayDeque(recipe.getRollableResultsAsItemStacks());
        
        while (!outputs.isEmpty()) {
            ItemStack out = outputs.peek();
            
            for (BlockPos p : be.tradePeds) {
                TradeManaPedestalBlockEntity tPed = blockEntity.getLevel().getBlockEntity(p, CMBotaniaBlockEntities.TRADE_MANA_PEDESTAL.get()).get();
                
                ItemStack item = tPed.getItem();
                
                if (item.isEmpty()) {
                    tPed.getBehaviour(DepotBehaviour.TYPE).setHeldItem(new TransportedItemStack(out.copy()));
                    outputs.remove(out);
                }
                
                if (!ItemStack.isSameItemSameTags(item, out)) continue;
                
                if (item.getCount() + out.getCount() > item.getMaxStackSize()) continue;
                
                tPed.getBehaviour(DepotBehaviour.TYPE).setHeldItem(new TransportedItemStack(out.copyWithCount(item.getCount() + out.getCount())));
                outputs.remove(out);
            }
        }
        
        be.processingTicks = -1;
        be.recipe = null;
        be.tradePeds.clear();
        be.inputPeds.clear();
        blockEntity.notifyUpdate(); // (This one was already here, confirming the end of the craft)
    }
    
    public List<BlockPos> nearbyPedestals() {
        BlockPos center = blockEntity.getBlockPos();
        
        int minX = -2, minY = -2, minZ = -2;
        int maxX = 3, maxY = 3, maxZ = 3;
        
        List<BlockPos> toRet = new ArrayList<>();
        
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    BlockPos offset = center.south(x).above(y).east(z);
                    if (blockEntity.getLevel().getBlockEntity(offset) instanceof ManaPedestalBlockEntity e) {
                        if (e.isOverloaded()) continue;
                        toRet.add(offset);
                    }
                }
            }
        }
        
        return toRet;
    }
    
    public List<BlockPos> nearbyTradePedestals() {
        BlockPos center = blockEntity.getBlockPos();
        
        int minX = -2, minY = -2, minZ = -2;
        int maxX = 3, maxY = 3, maxZ = 3;
        
        List<BlockPos> toRet = new ArrayList<>();
        
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    BlockPos offset = center.south(x).above(y).east(z);
                    if (blockEntity.getLevel().getBlockEntity(offset) instanceof TradeManaPedestalBlockEntity e) {
                        if (e.isOverloaded()) continue;
                        toRet.add(offset);
                    }
                }
            }
        }
        
        return toRet;
    }
    
    private TradingRiftBlockEntity getBE() {
        return ((TradingRiftBlockEntity) blockEntity);
    }
    
    public List<BlockPos> locatePylons(boolean rescanNow) {
        if (!rescanNow && !this.cachedPylonPositions.isEmpty()) {
            List<BlockPos> cachedResult = new ArrayList<>();
            
            for (BlockPos pos : this.cachedPylonPositions) {
                if (this.isValidPylonPosition(pos)) {
                    cachedResult.add(pos);
                }
            }
            
            if (!cachedResult.isEmpty()) {
                return cachedResult;
            }
        }
        
        int range = 5;
        List<BlockPos> result = new ArrayList<>();
        
        for (BlockPos pos : BlockPos.betweenClosed(blockEntity.getBlockPos().offset(-range, -range, -range), blockEntity.getBlockPos().offset(range, range, range))) {
            if (this.isValidPylonPosition(pos)) {
                result.add(pos.immutable());
            }
        }
        
        this.cachedPylonPositions.clear();
        this.cachedPylonPositions.addAll(result);
        return result;
    }
    
    private boolean isValidPylonPosition(BlockPos pos) {
        return blockEntity.getLevel().hasChunkAt(pos) && blockEntity.getLevel().getBlockState(pos).is(BotaniaBlocks.naturaPylon) && blockEntity.getLevel().getBlockState(pos.below()).getBlock() instanceof ManaPoolBlock;
    }
    
    public boolean consumeMana(List<BlockPos> pylons, int totalCost, boolean simulate) {
        List<ManaPoolBlockEntity> consumePools = new ArrayList<>();
        int consumed = 0;
        if (pylons.isEmpty()) {
            return false;
        }
        
        int costPer = Math.max(1, totalCost / pylons.size());
        int expectedConsumption = costPer * pylons.size();
        Level level = this.blockEntity.getLevel();
        
        for (BlockPos pos : pylons) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof PylonBlockEntity e && !simulate) {
                PylonBlockEntityAccessor pylon = (PylonBlockEntityAccessor) tile;
                pylon.setActivated(true);
                pylon.setCenterPos(blockEntity.getBlockPos());
                e.setChanged();
                level.sendBlockUpdated(e.getBlockPos(), e.getBlockState(), e.getBlockState(), 3);
            }
            
            tile = level.getBlockEntity(pos.below());
            if (tile instanceof ManaPoolBlockEntity) {
                ManaPoolBlockEntity pool = (ManaPoolBlockEntity) tile;
                if (pool.getCurrentMana() < costPer) {
                    return false;
                }
                
                if (!this.blockEntity.getLevel().isClientSide) {
                    consumePools.add(pool);
                    consumed += costPer;
                }
            }
        }
        
        if (consumed < expectedConsumption) {
            return false;
        }
        
        if (simulate) return true;
        
        for (ManaPoolBlockEntity pool : consumePools) {
            pool.receiveMana(-costPer);
            pool.craftingEffect(false);
        }
        
        return true;
    }
}