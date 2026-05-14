package com.chloe.cm;

import com.chloe.cm.impl.config.Config;
import com.chloe.cm.impl.init.CMModules;
import com.chloe.cm.impl.module.botania.CMBotaniaModule;
import com.chloe.cm.impl.module.manafluid.CMBotaniaManaFluidModule;
import com.chloe.cm.impl.module.tconstruct.CMTinkersConstructModule;
import com.chloe.cm.impl.module.tconstructclh.CMTinkersConstructCreateLowHeatedModule;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CMConstants.MODID)
public class CreateMechanized {
    
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public CreateMechanized() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        Config.initialize();
        
        //Can't dynamically load them, as java won't load the class unless referenced T_T
        CMModules.MODULES.put(CMConstants.modLoc(CMBotaniaModule.INSTANCE.modId()), CMBotaniaModule.INSTANCE);
        CMModules.MODULES.put(CMConstants.modLoc(CMBotaniaManaFluidModule.INSTANCE.modId()), CMBotaniaManaFluidModule.INSTANCE);
        CMModules.MODULES.put(CMConstants.modLoc(CMTinkersConstructModule.INSTANCE.modId()), CMTinkersConstructModule.INSTANCE);
        CMModules.MODULES.put(CMConstants.modLoc(CMTinkersConstructCreateLowHeatedModule.INSTANCE.modId() + "_tconstruct"), CMTinkersConstructCreateLowHeatedModule.INSTANCE);
        
        CMModules.MODULES.forEach(
            (id, mod) -> {
                if (mod.enabled()) mod.load(modEventBus);
            }
        );
    }
}
