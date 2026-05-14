package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CMConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        
        generator.addProvider(event.includeClient(), new CMBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new CMItemModelProvider(packOutput, existingFileHelper));
        
        generator.addProvider(event.includeClient(), CMLootTableProvider.create(packOutput));
        
        CMBlockTagGenerator blockTag = new CMBlockTagGenerator(packOutput, provider, existingFileHelper);
        generator.addProvider(event.includeClient(), blockTag);
        generator.addProvider(event.includeClient(), new CMItemTagGenerator(packOutput, provider, blockTag.contentsGetter(), existingFileHelper));
        
        generator.addProvider(event.includeClient(), new CMRecipeProvider(packOutput));
        
        generator.addProvider(event.includeClient(), new CMLanguageProvider(packOutput, "en_us"));
        
        generator.addProvider(event.includeClient(), new CMFluidBucketModelProvider(packOutput));
        generator.addProvider(event.includeClient(), new CMFluidTextureProvider(packOutput));
    }
}
