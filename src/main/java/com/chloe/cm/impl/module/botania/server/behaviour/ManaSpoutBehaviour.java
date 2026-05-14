package com.chloe.cm.impl.module.botania.server.behaviour;

import com.chloe.cm.impl.module.botania.init.CMBotaniaRecipes;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaSpoutBlockEntity;
import com.chloe.cm.impl.module.botania.server.recipe.ManaSpoutRecipe;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManaSpoutBehaviour extends BeltProcessingBehaviour {
    public ManaSpoutBehaviour(SmartBlockEntity be) {
        super(be);
        
        whenItemEnters(this::itemEnters);
        whileItemHeld(this::itemHeld);
    }
    
    protected ProcessingResult itemEnters(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        if (handler.blockEntity.isVirtual()) {
            return ProcessingResult.PASS;
        }
        
        Optional<ManaSpoutRecipe> recipe = this.getRecipe(transported.stack);
        
        if (recipe.isEmpty()) {
            return ProcessingResult.PASS;
        }
        
        ManaSpoutBlockEntity be = getBE();
        
        if (be.getCurrentMana() >= recipe.get().getManaRequired()) {
            return ProcessingResult.HOLD;
        }
        
        ManaSpark spark = be.getAttachedSpark();
        
        if (spark != null) {
            BlockPos worldPos = be.getBlockPos();
            for (ManaSpark otherSpark : SparkHelper.getSparksAround(be.getLevel(), (double) worldPos.getX() + (double) 0.5F, (double) worldPos.getY() + (double) 0.5F, (double) worldPos.getZ() + (double) 0.5F, spark.getNetwork())) {
                if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
                    otherSpark.registerTransfer(spark);
                }
            }
        }
        
        return ProcessingResult.HOLD;
    }
    
    protected ProcessingResult itemHeld(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        ManaSpoutBlockEntity be = getBE();
        if (be.processingTicks != -1 && be.processingTicks != 5) {
            return ProcessingResult.HOLD;
        }
        
        Optional<ManaSpoutRecipe> recipeOpt = this.getRecipe(transported.stack);
        
        if (recipeOpt.isEmpty()) {
            return ProcessingResult.PASS;
        }
        
        ManaSpoutRecipe recipe = recipeOpt.get();
        int requiredMana = recipe.getManaRequired();
        
        Level level = be.getLevel();
        
        if (requiredMana > be.getCurrentMana()) {
            ManaSpark spark = be.getAttachedSpark();
            BlockPos worldPos = be.getBlockPos();
            
            if (spark != null) {
                for (ManaSpark otherSpark : SparkHelper.getSparksAround(level, (double) worldPos.getX() + (double) 0.5F, (double) worldPos.getY() + (double) 0.5F, (double) worldPos.getZ() + (double) 0.5F, spark.getNetwork())) {
                    if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
                        otherSpark.registerTransfer(spark);
                    }
                }
            }
        }
        
        if (requiredMana > be.getCurrentMana()) {
            return ProcessingResult.HOLD;
        }
        
        if (be.processingTicks == -1) {
            be.processingTicks = 20;
            be.notifyUpdate();
            return ProcessingResult.HOLD;
        }
        
        ItemStack out = recipe.rollResults().get(0).copy();
        
        if (!out.isEmpty()) {
            transported.clearFanProcessingData();
            List<TransportedItemStack> outList = new ArrayList<>();
            TransportedItemStack held = null;
            TransportedItemStack result = transported.copy();
            result.stack = out;
            
            ItemStack remaining = transported.stack.copy();
            remaining.shrink(1);
            
            if (!remaining.isEmpty()) {
                held = transported.copy();
                held.stack = remaining;
            }
            
            outList.add(result);
            handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(outList, held));
        }
        
        be.decreaseMana(requiredMana);
        be.sendSplash = true;
        be.notifyUpdate();
        return ProcessingResult.HOLD;
    }
    
    private Optional<ManaSpoutRecipe> getRecipe(ItemStack item) {
        if (blockEntity.getLevel() == null) return Optional.empty();
        
        SimpleContainer cont = new SimpleContainer(item);
        Level level = getBE().getLevel();
        
        Optional<ManaSpoutRecipe> seqRecipe = SequencedAssemblyRecipe.getRecipe(level, cont, CMBotaniaRecipes.MANA_SPOUT_TYPE.getType(), ManaSpoutRecipe.class);
        
        if(seqRecipe.isPresent()) return seqRecipe;
        
        List<Recipe<?>> allRecipes = new ArrayList<>(level.getRecipeManager().getAllRecipesFor(CMBotaniaRecipes.MANA_SPOUT_TYPE.getType()));
        
        if(allRecipes.isEmpty()) return Optional.empty();
        
        for (Recipe<?> r : allRecipes) {
            ManaSpoutRecipe recipe = ((ManaSpoutRecipe) r);
            
            if (!recipe.matches(cont, level)) {
                continue;
            }
            
            if(recipe.getRecipeCatalyst() == null) return Optional.of(recipe);
            
            if (recipe.getRecipeCatalyst().test(level.getBlockState(getBE().getBlockPos().below(3)))) {
                return Optional.of(recipe);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getBE().processingTicks >= 0) {
            --getBE().processingTicks;
        }
        
        if (getBE().processingTicks == 5 && !getBE().getLevel().isClientSide) {
            spawnProcessingParticles();
        }
    }
    
    protected void spawnProcessingParticles() {
        if (getBE().isVirtual())
            return;
        
        Vec3 vec = VecHelper.getCenterOf(getBE().getBlockPos());
        vec = vec.subtract(0, 8 / 16f, 0);
        
        ManaBurstEntity burst = getBurst();
        Level level = getBE().getLevel();
        
        level.addFreshEntity(burst);
        level.playSound(null, vec.x, vec.y, vec.z, BotaniaSounds.spreaderFire, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    
    protected ManaSpoutBlockEntity getBE() {
        return ((ManaSpoutBlockEntity) blockEntity);
    }
    
    public ManaBurstEntity getBurst() {
        ManaBurstEntity burst = new ManaBurstEntity(getBE().getLevel(), getBE().getBlockPos(), 0, 0, true);
        burst.setColor(0x22DCFF);
        burst.setMana(20);
        burst.setStartingMana(20);
        burst.setMinManaLoss(1);
        burst.setManaLossPerTick(1);
        burst.setGravity(0.0F);
        burst.setDeltaMovement(new Vec3(0, -1, 0));
        burst.setSourceLens(ItemStack.EMPTY);
        return burst;
    }
}
