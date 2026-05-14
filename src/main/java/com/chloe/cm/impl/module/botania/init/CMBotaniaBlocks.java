package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.block.*;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.item.KineticStats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.function.Supplier;

public class CMBotaniaBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CMConstants.MODID);

    public static final RegistryObject<ManaSpoutBlock> MANA_SPOUT = registerAssemblyOperatorBlockItem("mana_spout", () -> new ManaSpoutBlock(
        BlockBehaviour.Properties
            .copy(BotaniaBlocks.livingrock)
            .noOcclusion())
    );
    
    public static final RegistryObject<ManaPedestalBlock> MANA_PEDESTAL = registerAssemblyOperatorBlockItem("mana_pedestal", () -> new ManaPedestalBlock(
        BlockBehaviour.Properties
            .copy(BotaniaBlocks.manaPylon)
            .noOcclusion())
    );
    
    public static final RegistryObject<AutoTerraPlateBlock> AUTO_TERRA_PLATE = registerAssemblyOperatorBlockItem("auto_terra_plate", () -> new AutoTerraPlateBlock(
        BlockBehaviour.Properties
            .copy(BotaniaBlocks.livingrock)
            .noOcclusion())
    );
    
    public static final RegistryObject<TradingRiftBlock> TRADING_RIFT = registerAssemblyOperatorBlockItem("trading_rift", () -> new TradingRiftBlock(
        BlockBehaviour.Properties
            .copy(BotaniaBlocks.livingwood)
            .noOcclusion())
    );
    
    public static final RegistryObject<TradeManaPedestalBlock> TRADE_MANA_PEDESTAL = registerAssemblyOperatorBlockItem("trade_mana_pedestal", () -> new TradeManaPedestalBlock(
        BlockBehaviour.Properties
            .copy(BotaniaBlocks.naturaPylon)
            .noOcclusion())
    );
    
    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    
    public static <T extends Block> RegistryObject<T> registerAssemblyOperatorBlockItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerAssemblyOperatorBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        CMBotaniaItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    private static <T extends Block> void registerAssemblyOperatorBlockItem(String name, RegistryObject<T> block) {
        CMBotaniaItems.ITEMS.register(name, () -> new AssemblyOperatorBlockItem(block.get(), new Item.Properties()));
    }
}
