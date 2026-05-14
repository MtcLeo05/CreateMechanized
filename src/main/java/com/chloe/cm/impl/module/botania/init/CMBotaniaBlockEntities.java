package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.blockentity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CMBotaniaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CMConstants.MODID);
    
    public static final RegistryObject<BlockEntityType<ManaSpoutBlockEntity>> MANA_SPOUT = BLOCK_ENTITIES.register("mana_spout",
        () -> BlockEntityType.Builder.of(
                ManaSpoutBlockEntity::new,
                CMBotaniaBlocks.MANA_SPOUT.get()
            )
            .build(null));
    
    public static final RegistryObject<BlockEntityType<ManaPedestalBlockEntity>> MANA_PEDESTAL = BLOCK_ENTITIES.register("mana_pedestal",
        () -> BlockEntityType.Builder.of(
                ManaPedestalBlockEntity::new,
                CMBotaniaBlocks.MANA_PEDESTAL.get()
            )
            .build(null));
    
    public static final RegistryObject<BlockEntityType<TradeManaPedestalBlockEntity>> TRADE_MANA_PEDESTAL = BLOCK_ENTITIES.register("trade_mana_pedestal",
        () -> BlockEntityType.Builder.of(
                TradeManaPedestalBlockEntity::new,
                CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get()
            )
            .build(null));
    
    public static final RegistryObject<BlockEntityType<AutoTerraPlateBlockEntity>> AUTO_TERRA_PLATE = BLOCK_ENTITIES.register("auto_terra_plate",
        () -> BlockEntityType.Builder.of(
                AutoTerraPlateBlockEntity::new,
                CMBotaniaBlocks.AUTO_TERRA_PLATE.get()
            )
            .build(null));
    
    public static final RegistryObject<BlockEntityType<TradingRiftBlockEntity>> TRADING_RIFT = BLOCK_ENTITIES.register("trading_rift",
        () -> BlockEntityType.Builder.of(
                TradingRiftBlockEntity::new,
                CMBotaniaBlocks.TRADING_RIFT.get()
            )
            .build(null));
}
