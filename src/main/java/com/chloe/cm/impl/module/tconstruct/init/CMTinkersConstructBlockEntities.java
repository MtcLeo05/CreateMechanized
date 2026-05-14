package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.blockentity.*;
import com.chloe.cm.impl.module.tconstruct.server.blockentity.SearedBurnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CMTinkersConstructBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CMConstants.MODID);
    
    public static final RegistryObject<BlockEntityType<SearedBurnerBlockEntity>> SEARED_BURNER = BLOCK_ENTITIES.register("seared_burner",
        () -> BlockEntityType.Builder.of(
                SearedBurnerBlockEntity::new,
                CMTinkersConstructBlocks.SEARED_BURNER.get()
            )
            .build(null));
}
