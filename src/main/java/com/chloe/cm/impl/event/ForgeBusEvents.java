package com.chloe.cm.impl.event;

import com.chloe.cm.CMConstants;
import com.chloe.cm.CreateMechanized;
import com.chloe.cm.impl.event.custom.AfterLoadResourceEvent;
import com.chloe.cm.impl.recipe.RecipeInjector;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CMConstants.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEvents {
    
    @SubscribeEvent
    public static void onReload(AfterLoadResourceEvent event) {
        CreateMechanized.LOGGER.info("[Create: Mechanized]: Injecting Recipes");
        RecipeInjector.INSTANCE.injectRecipes(event.getRecipeManager(), event.getRegistryAccess());
        CreateMechanized.LOGGER.info("[Create: Mechanized]: Injected Recipes");
    }
}
