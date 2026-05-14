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
            .add(CMBotaniaBlocks.MANA_SPOUT.get())
            .add(CMBotaniaBlocks.MANA_PEDESTAL.get())
            .add(CMBotaniaBlocks.AUTO_TERRA_PLATE.get())
            .add(CMBotaniaBlocks.TRADING_RIFT.get())
            .add(CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get())
        ;
    }
    
    private void tconstruct() {
        this.tag(TinkerTags.Blocks.FUEL_TANKS)
            .add(AllBlocks.BLAZE_BURNER.get())
            .add(AllBlocks.LIT_BLAZE_BURNER.get())
            .add(zeh.createlowheated.AllBlocks.BASIC_BURNER.get())
        ;
        
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(CMTinkersConstructBlocks.SEARED_BURNER.get())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY_WALL)
            .add(CMTinkersConstructBlocks.SEARED_BURNER.get())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY)
            .add(CMTinkersConstructBlocks.SEARED_BURNER.get())
        ;
        
        this.tag(TinkerTags.Blocks.SMELTERY_TANKS)
            .add(CMTinkersConstructBlocks.SEARED_BURNER.get())
        ;
    }
}
