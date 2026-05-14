package com.chloe.cm.impl.datagen;

import com.chloe.cm.impl.datagen.loot.CMBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class CMLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(CMBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
