package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaItems;
import com.chloe.cm.impl.module.botania.server.block.*;
import com.chloe.cm.impl.module.tconstruct.server.block.SearedBurnerBlock;
import com.chloe.cm.impl.module.tconstruct.server.blockentity.SearedBurnerBlockEntity;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.function.Supplier;

public class CMTinkersConstructBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CMConstants.MODID);

    public static final RegistryObject<SearedBurnerBlock> SEARED_BURNER = registerAssemblyOperatorBlockItem("seared_burner", () -> new SearedBurnerBlock(
        BlockBehaviour.Properties
            .copy(TinkerSmeltery.searedBricks.get())
            .lightLevel(SearedBurnerBlock::getLight)
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
