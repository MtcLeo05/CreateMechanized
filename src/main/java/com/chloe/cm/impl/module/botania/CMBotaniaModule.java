package com.chloe.cm.impl.module.botania;

import com.chloe.cm.CMConstants;
import com.chloe.cm.api.module.IModule;
import com.chloe.cm.impl.config.Config;
import com.chloe.cm.impl.module.botania.client.model.block.AutoTerraPlateModel;
import com.chloe.cm.impl.module.botania.client.model.block.ManaPedestalModel;
import com.chloe.cm.impl.module.botania.client.model.block.TradeManaPedestalModel;
import com.chloe.cm.impl.module.botania.client.model.block.TradingBaseModel;
import com.chloe.cm.impl.module.botania.client.render.be.*;
import com.chloe.cm.impl.module.botania.init.*;
import com.chloe.cm.impl.module.botania.server.item.RunicTemplateItem;
import com.chloe.cm.impl.module.botania.server.recipe.*;
import com.chloe.cm.impl.recipe.RecipeInjector;
import com.chloe.cm.mixin.ProcessingRecipeBuilderAccessor;
import com.chloe.cm.mixin.ProcessingRecipeParamsAccessor;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.recipe.*;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.item.material.RuneItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class CMBotaniaModule implements IModule {
    
    public static CMBotaniaModule INSTANCE = new CMBotaniaModule();
    
    private CMBotaniaModule() {
    }
    
    @Override
    public String modId() {
        return "botania";
    }
    
    @Override
    public boolean enabled() {
        return ModList.get().isLoaded(modId()) && Config.INSTANCE.enableBotania;
    }
    
    @Override
    public void load(IEventBus bus) {
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.PETAL_TYPE, this::transmutePetal);
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.MANA_INFUSION_TYPE, this::transmuteInfusion);
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.RUNE_TYPE, this::transmuteRunic);
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.RUNE_TYPE, this::transmuteRunicP2);
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.TERRA_PLATE_TYPE, this::transmuteTerra);
        RecipeInjector.INSTANCE.transmuteRecipe(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, this::transmuteElven);
        
        CMBotaniaBlockEntities.BLOCK_ENTITIES.register(bus);
        CMBotaniaBlocks.BLOCKS.register(bus);
        CMBotaniaItems.ITEMS.register(bus);
        CMBotaniaRecipes.SERIALIZERS.register(bus);
        CMBotaniaRecipes.TYPES.register(bus);
        CMBotaniaCreativeTabs.CREATIVE_MODE_TABS.register(bus);
        CMBotaniaInteractionPointType.TYPES.register(bus);
        
        bus.addListener(CMBotaniaModule::clientInit);
        bus.addListener(CMBotaniaModule::registerBER);
        bus.addListener(CMBotaniaModule::registerModel);
        bus.addListener(CMBotaniaModule::registerLayerDefinition);
    }
    
    @Override
    public List<? extends Supplier<? extends Block>> blocks() {
        return List.of(
            CMBotaniaBlocks.MANA_SPOUT,
            CMBotaniaBlocks.AUTO_TERRA_PLATE,
            CMBotaniaBlocks.TRADING_RIFT,
            CMBotaniaBlocks.MANA_PEDESTAL,
            CMBotaniaBlocks.TRADE_MANA_PEDESTAL
        );
    }
    
    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CMBotaniaPonders());
        TooltipModifier.REGISTRY.register(CMBotaniaBlocks.AUTO_TERRA_PLATE.get().asItem(), new KineticStats(CMBotaniaBlocks.AUTO_TERRA_PLATE.get()));
        TooltipModifier.REGISTRY.register(CMBotaniaBlocks.TRADING_RIFT.get().asItem(), new KineticStats(CMBotaniaBlocks.TRADING_RIFT.get()));
    }
    
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CMBotaniaBlockEntities.MANA_SPOUT.get(), ManaSpoutBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CMBotaniaBlockEntities.MANA_PEDESTAL.get(), ManaPedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CMBotaniaBlockEntities.AUTO_TERRA_PLATE.get(), AutoTerraPlateBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CMBotaniaBlockEntities.TRADE_MANA_PEDESTAL.get(), TradeManaPedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CMBotaniaBlockEntities.TRADING_RIFT.get(), TradingRiftBlockEntityRenderer::new);
    }
    
    public static void registerModel(ModelEvent.RegisterAdditional event) {
        event.register(CMConstants.modLoc("block/spout/top"));
        event.register(CMConstants.modLoc("block/spout/middle"));
        event.register(CMConstants.modLoc("block/spout/bottom"));
        event.register(CMConstants.modLoc("block/shaft_tiny"));
        event.register(CMConstants.modLoc("block/trading_rift"));
    }
    
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ManaPedestalModel.LAYER_LOCATION, ManaPedestalModel::createBodyLayer);
        event.registerLayerDefinition(AutoTerraPlateModel.LAYER_LOCATION, AutoTerraPlateModel::createBodyLayer);
        event.registerLayerDefinition(TradeManaPedestalModel.LAYER_LOCATION, TradeManaPedestalModel::createBodyLayer);
        event.registerLayerDefinition(TradingBaseModel.LAYER_LOCATION, TradingBaseModel::createBodyLayer);
    }
    
    private MixingRecipe transmutePetal(PetalApothecaryRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/mixing/" + originalId.getPath());
        
        ProcessingRecipeBuilder<MixingRecipe> builder = new ProcessingRecipeBuilder<>(MixingRecipe::new, newId);
        
        builder.withFluidIngredients(FluidIngredient.fromTag(FluidTags.WATER, 250));
        
        ArrayList<Ingredient> ingredients = new ArrayList<>(oldRecipe.getIngredients());
        ingredients.add(oldRecipe.getReagent());
        
        builder.withItemIngredients(ingredients.toArray(Ingredient[]::new));
        
        builder.withSingleItemOutput(oldRecipe.getResultItem(registryAccess));
        return builder.build();
    }
    
    private ManaSpoutRecipe transmuteInfusion(ManaInfusionRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/mana_spout/" + originalId.getPath());
        
        ManaSpoutRecipeBuilder builder = new ManaSpoutRecipeBuilder(ManaSpoutRecipe::new, newId);
        
        ArrayList<Ingredient> ingredients = new ArrayList<>(oldRecipe.getIngredients());
        
        builder.withItemIngredients(ingredients.toArray(Ingredient[]::new));
        
        builder.manaRequired(oldRecipe.getManaToConsume());
        builder.catalyst(oldRecipe.getRecipeCatalyst());
        
        builder.withSingleItemOutput(oldRecipe.getResultItem(registryAccess));
        return builder.build();
    }
    
    private MixingRecipe transmuteRunic(RunicAltarRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/mixing/" + originalId.getPath());
        
        ProcessingRecipeBuilder<MixingRecipe> builder = new ProcessingRecipeBuilder<>(MixingRecipe::new, newId);
        
        ArrayList<Ingredient> ingredients = new ArrayList<>(oldRecipe.getIngredients());
        
        builder.withItemIngredients(ingredients.toArray(Ingredient[]::new));
        
        ItemStack output = new ItemStack(CMBotaniaItems.RUNIC_TEMPLATE.get());
        
        CompoundTag tag = output.getOrCreateTag();
        tag.putString(RunicTemplateItem.RUNE_KEY, ForgeRegistries.ITEMS.getKey(oldRecipe.getResultItem(registryAccess).getItem()).toString());
        output.setTag(tag);
        
        List<ProcessingOutput> outputs = new ArrayList<>();
        
        outputs.add(new ProcessingOutput(output, 1));
        
        for (Ingredient ing : ingredients) {
            Arrays.stream(ing.getItems()).filter(i -> i.getItem() instanceof RuneItem).map(i -> new ProcessingOutput(i, 1)).findFirst().ifPresent(outputs::add);
        }
        
        builder.withItemOutputs(outputs.toArray(new ProcessingOutput[]{}));
        return builder.build();
    }
    
    private SequencedAssemblyRecipe transmuteRunicP2(RunicAltarRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/sequenced_assembly/" + originalId.getPath());
        
        SequencedAssemblyRecipeBuilder builder = new SequencedAssemblyRecipeBuilder(newId);
        
        ItemStack rune = new ItemStack(CMBotaniaItems.RUNIC_TEMPLATE.get());
        
        CompoundTag tag = rune.getOrCreateTag();
        tag.putString(RunicTemplateItem.RUNE_KEY, ForgeRegistries.ITEMS.getKey(oldRecipe.getResultItem(registryAccess).getItem()).toString());
        rune.setTag(tag);
        
        builder.transitionTo(CMBotaniaItems.UNETCHED_RUNE.get())
            .require(StrictNBTIngredient.of(rune))
            .loops(1)
            .addOutput(oldRecipe.getResultItem(registryAccess).copy(), 1)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Ingredient.of(BotaniaBlocks.livingrock)))
            .addStep(ManaSpoutRecipe::new, rb -> {
                ManaSpoutRecipeBuilder b = new ManaSpoutRecipeBuilder(ManaSpoutRecipe::new, ResourceLocation.withDefaultNamespace("dummy"));
                
                ProcessingRecipeParamsAccessor params = ((ProcessingRecipeParamsAccessor) ((ProcessingRecipeBuilderAccessor) rb).params());
                
                b.manaRequired(oldRecipe.getManaUsage());
                b.withItemIngredients(params.ingredients());
                b.withItemOutputs(params.results());
                b.withFluidIngredients(params.fluidIngredients());
                b.withFluidOutputs(params.fluidResults());
                
                return b;
            });
        
        return builder.build();
    }
    
    private AutoTerraPlateRecipe transmuteTerra(TerrestrialAgglomerationRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/auto_terra_plate/" + originalId.getPath());
        
        AutoTerraPlateRecipeBuilder builder = new AutoTerraPlateRecipeBuilder(AutoTerraPlateRecipe::new, newId);
        
        ArrayList<Ingredient> ingredients = new ArrayList<>(oldRecipe.getIngredients());
        
        builder.withItemIngredients(ingredients.toArray(Ingredient[]::new));
        
        builder.manaRequired(oldRecipe.getMana());
        
        builder.withSingleItemOutput(oldRecipe.getResultItem(registryAccess).copy());
        return builder.build();
    }
    
    private TradingRiftRecipe transmuteElven(ElvenTradeRecipe oldRecipe, RegistryAccess registryAccess) {
        ResourceLocation originalId = oldRecipe.getId();
        
        ResourceLocation newId = CMConstants.modLoc(originalId.getNamespace() + "/trading_rift/" + originalId.getPath());
        
        TradingRiftRecipeBuilder builder = new TradingRiftRecipeBuilder(TradingRiftRecipe::new, newId);
        
        ArrayList<Ingredient> ingredients = new ArrayList<>(oldRecipe.getIngredients());
        
        builder.withItemIngredients(ingredients.toArray(Ingredient[]::new));
        
        builder.manaRequired(AlfheimPortalBlockEntity.MANA_COST);
        
        builder.withItemOutputs(oldRecipe.getOutputs().stream().map(r -> new ProcessingOutput(r, 1)).toList().toArray(new ProcessingOutput[0]));
        return builder.build();
    }
}
