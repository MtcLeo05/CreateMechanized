package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlocks;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

public class CMBlockTagGenerator extends BlockTagsProvider {
    public CMBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CMConstants.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        botania();
        tconstruct();
    }
    
    private void botania() {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addOptional(CMBotaniaBlocks.MANA_SPOUT.getId())
            .addOptional(CMBotaniaBlocks.MANA_PEDESTAL.getId())
            .addOptional(CMBotaniaBlocks.AUTO_TERRA_PLATE.getId())
            .addOptional(CMBotaniaBlocks.TRADING_RIFT.getId())
            .addOptional(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.getId())
        ;
    }
    
    private void tconstruct() {
        this.tag(TinkerTags.Blocks.FUEL_TANKS)
            .addOptional(AllBlocks.BLAZE_BURNER.getId())
            .addOptional(AllBlocks.LIT_BLAZE_BURNER.getId())
            .addOptional(zeh.createlowheated.AllBlocks.BASIC_BURNER.getId())
        ;
        
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addOptional(CMTinkersConstructBlocks.SEARED_BURNER.getId())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY_WALL)
            .addOptional(CMTinkersConstructBlocks.SEARED_BURNER.getId())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY)
            .addOptional(CMTinkersConstructBlocks.SEARED_BURNER.getId())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY_TANKS)
            .addOptional(CMTinkersConstructBlocks.SEARED_BURNER.getId())
        ;
    }
}
