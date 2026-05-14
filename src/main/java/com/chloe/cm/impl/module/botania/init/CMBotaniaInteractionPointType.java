package com.chloe.cm.impl.module.botania.init;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.interaction.AutoTerraPlateArmInteractionPointType;
import com.chloe.cm.impl.module.botania.server.interaction.ManaPedestalArmInteractionPointType;
import com.chloe.cm.impl.module.botania.server.interaction.TradeManaPedestalArmInteractionPointType;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CMBotaniaInteractionPointType {
    public static final DeferredRegister<ArmInteractionPointType> TYPES = DeferredRegister.create(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE.key(), CMConstants.MODID);
    
    public static final RegistryObject<ArmInteractionPointType> MANA_PEDESTAL = TYPES.register("mana_pedestal_type", ManaPedestalArmInteractionPointType::new);
    public static final RegistryObject<ArmInteractionPointType> AUTO_TERRA_PLATE = TYPES.register("auto_terra_plate_type", AutoTerraPlateArmInteractionPointType::new);
    public static final RegistryObject<ArmInteractionPointType> TRADE_MANA_PEDESTAL = TYPES.register("trade_mana_pedestal_type", TradeManaPedestalArmInteractionPointType::new);
    
}
