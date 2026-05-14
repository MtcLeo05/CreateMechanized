package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

public class CMItemTagGenerator extends ItemTagsProvider {
    public CMItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CMConstants.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        botania();
        tconstruct();
    }
    
    private void botania() {
    
    }
    
    private void tconstruct() {
        this.tag(TinkerTags.Items.SMELTERY)
            .add(CMTinkersConstructBlocks.SEARED_BURNER.get().asItem())
        ;
    }
}
