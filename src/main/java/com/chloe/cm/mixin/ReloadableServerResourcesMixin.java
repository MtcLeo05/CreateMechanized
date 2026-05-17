package com.chloe.cm.mixin;

import com.chloe.cm.CreateMechanized;
import com.chloe.cm.impl.event.custom.AfterLoadResourceEvent;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ReloadableServerResources.class, priority = 500)
public abstract class ReloadableServerResourcesMixin {
    
    @Inject(method = "loadResources", at = @At(value = "RETURN"))
    private static void cm$injectAfterLoad(ResourceManager pResourceManager, RegistryAccess.Frozen pRegistryAccess, FeatureFlagSet pEnabledFeatures, Commands.CommandSelection pCommandSelection, int pFunctionCompilationLevel, Executor pBackgroundExecutor, Executor pGameExecutor, CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
        CreateMechanized.LOGGER.info("[Create: Mechanized]: Injecting AfterLoadResourceEvent");
        cir.getReturnValue().thenAccept(r -> {
            CreateMechanized.LOGGER.info("[Create: Mechanized]: Injected AfterLoadResourceEvent");
            MinecraftForge.EVENT_BUS.post(new AfterLoadResourceEvent(pRegistryAccess, r.getRecipeManager()));
        });
    }
    
}
