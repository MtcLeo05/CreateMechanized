package com.chloe.cm.impl.module.botania.server.behaviour;

import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.recipe.AutoTerraPlateRecipe;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.google.common.collect.Queues;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class AutoTerraPlateBehaviour extends DepotBehaviour {
    
    private final List<BlockPos> inputPeds = new ArrayList<>();
    
    public AutoTerraPlateBehaviour(SmartBlockEntity be) {
        super(be);
    }
    
    public List<BlockPos> getUsedPeds() {
        return inputPeds;
    }
    
    private Optional<AutoTerraPlateRecipe> getRecipe() {
        if (blockEntity.getLevel() == null) return Optional.empty();
        
        AutoTerraPlateBlockEntity be = getBE();
        Level level = be.getLevel();
        
        if(be.recipe != null) {
            return (Optional<AutoTerraPlateRecipe>) be.getLevel().getRecipeManager().byKey(be.recipe);
        }
        
        List<Recipe<?>> allRecipes = new ArrayList<>(level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.AUTO_TERRA_PLATE_TYPE.getType()));
        
        if (allRecipes.isEmpty()) return Optional.empty();
        
        List<BlockPos> pedestals = nearbyPedestals();
        
        List<ItemStack> items = new ArrayList<>();
        
        pedestals.forEach(p -> {
                if (blockEntity.getLevel().getBlockEntity(p) instanceof ManaPedestalBlockEntity ped) {
                    items.add(
                        ped.getItem()
                    );
                }
            }
        );
        
        SimpleContainer cont = new SimpleContainer(items.toArray(new ItemStack[]{}));
        
        for (Recipe<?> r : allRecipes) {
            AutoTerraPlateRecipe recipe = ((AutoTerraPlateRecipe) r);
            
            if (pedestals.size() < recipe.getIngredients().size()) continue;
            
            if (recipe.matches(cont, level)) {
                ItemStack currentItem = getHeldItemStack();
                
                if(currentItem.isEmpty()) return Optional.of(recipe);
                
                ItemStack result = recipe.getResultItem(level.registryAccess());
                
                if(!ItemStack.isSameItemSameTags(currentItem, result)) return Optional.empty();
                
                if(currentItem.getCount() + result.getCount() > currentItem.getMaxStackSize()) return Optional.empty();
                
                return Optional.of(recipe);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public void tick() {
        if (blockEntity.getLevel().isClientSide) return;
        
        AutoTerraPlateBlockEntity be = getBE();
        if (be.isSpeedRequirementFulfilled() != be.active) {
            be.active = be.isSpeedRequirementFulfilled();
            be.startTransition(20f);
        }
        
        if (be.getTransitional() > 0) {
            be.tickTransitional();
            be.notifyUpdate();
        }
        
        if (!be.active || be.getTransitional() > 0) return;
        
        Optional<AutoTerraPlateRecipe> rOpt = getRecipe();
        if (rOpt.isEmpty()) return;
        
        AutoTerraPlateRecipe recipe = rOpt.get();
        
        if (be.processingTicks == -1) {
            if (recipe.getManaRequired() > be.getCurrentMana()) {
                ManaSpark spark = be.getAttachedSpark();
                BlockPos worldPos = be.getBlockPos();
                
                if (spark != null) {
                    for (ManaSpark otherSpark : SparkHelper.getSparksAround(be.getLevel(), (double) worldPos.getX() + (double) 0.5F, (double) worldPos.getY() + (double) 0.5F, (double) worldPos.getZ() + (double) 0.5F, spark.getNetwork())) {
                        if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
                            otherSpark.registerTransfer(spark);
                        }
                    }
                }
            }
            
            if (recipe.getManaRequired() > be.getCurrentMana()) return;
            
            be.processingTicks = 40;
            
            List<BlockPos> pos = nearbyPedestals();
            be.decreaseMana(recipe.getManaRequired());
            
            inputPeds.clear();
            
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
                        inputPeds.add(p);
                        continue first;
                    }
                }
            }
            
            be.recipe = recipe.getId();
            be.notifyUpdate();
            
            return;
        }
        
        be.processingTicks -= Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue();
        be.notifyUpdate();
        
        if (be.processingTicks > 0) return;
        
        int old = getHeldItemStack().getCount();
        
        setHeldItem(
            new TransportedItemStack(
                recipe.getResultItem(be.getLevel().registryAccess()).copyWithCount(old + 1)
            )
        );
        
        be.processingTicks = -1;
        be.recipe = null;
        inputPeds.clear();
        blockEntity.notifyUpdate();
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
    
    private AutoTerraPlateBlockEntity getBE() {
        return ((AutoTerraPlateBlockEntity) blockEntity);
    }
    
    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        
        ListTag tag = new ListTag();
        for (BlockPos p : inputPeds) {
            tag.add(LongTag.valueOf(p.asLong()));
        }
        nbt.put("InputPedestals", tag);
    }
    
    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        
        inputPeds.clear();
        if (nbt.contains("InputPedestals")) {
            ListTag tag = (ListTag) nbt.get("InputPedestals");
            tag.forEach(t -> inputPeds.add(BlockPos.of(((LongTag) t).getAsLong())));
        }
    }
}
