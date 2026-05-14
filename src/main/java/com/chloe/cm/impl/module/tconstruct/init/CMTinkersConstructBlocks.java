package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.init.CMBotaniaItems;
import com.chloe.cm.impl.module.tconstruct.server.block.SearedBurnerBlock;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.smeltery.block.component.SearedBlock;

import java.util.function.Supplier;

public class CMTinkersConstructBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CMConstants.MODID);

    public static final RegistryObject<SearedBurnerBlock> SEARED_BURNER = registerAssemblyOperatorBlockItem("seared_burner", () -> new SearedBurnerBlock(
        searedSolidProps(1)
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
    
    private static BlockBehaviour.Properties searedSolidProps(int factor) {
        return structureProps(MapColor.COLOR_GRAY, SoundType.METAL).strength(3F * factor, 9F * factor);
    }
    
    private static BlockBehaviour.Properties structureProps(MapColor color, SoundType sound) {
        return builder(color, sound).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().isValidSpawn(SearedBlock.VALID_SPAWN);
    }
    
    protected static BlockBehaviour.Properties builder(MapColor color, SoundType soundType) {
        return builder(soundType).mapColor(color);
    }
    
    protected static BlockBehaviour.Properties builder(SoundType soundType) {
        return BlockBehaviour.Properties.of().sound(soundType);
    }
}
