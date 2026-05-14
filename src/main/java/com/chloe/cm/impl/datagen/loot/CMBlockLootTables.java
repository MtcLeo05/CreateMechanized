package com.chloe.cm.impl.datagen.loot;

import com.chloe.cm.impl.init.CMModules;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class CMBlockLootTables extends BlockLootSubProvider {
    public CMBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (Block b : getKnownBlocks()) {
            this.dropSelf(b);
        }
        
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        
        CMModules.MODULES.forEach(
            (key, mod) -> {
                if(mod.enabled()) blocks.addAll(mod.blocks().stream().map(Supplier::get).toList());
            }
        );
        
        return blocks;
    }
}