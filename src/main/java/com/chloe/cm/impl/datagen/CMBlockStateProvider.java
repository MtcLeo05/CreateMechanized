package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CMBlockStateProvider extends BlockStateProvider {

    ExistingFileHelper existingFileHelper;

    public CMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CMConstants.MODID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CMBotaniaBlocks.MANA_SPOUT.get(), model(CMBotaniaBlocks.MANA_SPOUT));
        simpleBlock(CMBotaniaBlocks.MANA_PEDESTAL.get(), model(CMBotaniaBlocks.MANA_PEDESTAL));
        simpleBlock(CMBotaniaBlocks.AUTO_TERRA_PLATE.get(), model(CMBotaniaBlocks.AUTO_TERRA_PLATE));
        simpleBlock(CMBotaniaBlocks.TRADING_RIFT.get(), model(CMBotaniaBlocks.TRADING_RIFT));
        simpleBlock(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get(), model(CMBotaniaBlocks.TRADING_RIFT));
        
        simpleBlock(CMTinkersConstructBlocks.SEARED_BURNER.get(), model(CMTinkersConstructBlocks.SEARED_BURNER));
    }

    private static ResourceLocation texture(RegistryObject<? extends Block> block) {
        return texture(block.getId().getPath());
    }

    private static ResourceLocation texture(String name) {
        return CMConstants.modLoc("block/" + name);
    }

    private static ModelFile model(RegistryObject<? extends Block> block) {
        return model(texture(block));
    }

    private static ModelFile model(ResourceLocation model) {
        return new ModelFile.UncheckedModelFile(model);
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
