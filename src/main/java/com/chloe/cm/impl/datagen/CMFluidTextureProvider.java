package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstruct.init.CMTinkersConstructFluids;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.data.PackOutput;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.tconstruct.TConstruct;

public class CMFluidTextureProvider extends AbstractFluidTextureProvider {
    public CMFluidTextureProvider(PackOutput packOutput) {
        super(packOutput, CMConstants.MODID);
    }
    
    @Override
    public void addTextures() {
        this.texture(CMTinkersConstructFluids.LIQUID_HEAT_FLUID)
            .root(TConstruct.getResource("fluid/molten/"))
            .still()
            .flowing()
            .camera()
            .calculateFogColor(true)
            .fog(FogShape.SPHERE, 0.25F, 2.0F)
            .color(0xffbc5400)
            .build();
        
        this.texture(CMTinkersConstructFluids.LIQUID_SUPER_HEAT_FLUID)
            .root(TConstruct.getResource("fluid/molten/"))
            .still()
            .flowing()
            .camera()
            .calculateFogColor(true)
            .fog(FogShape.SPHERE, 0.25F, 2.0F)
            .color(0xff008488)
            .build();
        
        this.texture(CMTinkersConstructFluids.LIQUID_LOW_HEAT_FLUID)
            .root(TConstruct.getResource("fluid/molten/"))
            .still()
            .flowing()
            .camera()
            .calculateFogColor(true)
            .fog(FogShape.SPHERE, 0.25F, 2.0F)
            .color(0xffedc846)
            .build();
    }
    
    @Override
    public String getName() {
        return "Create Mechanized Fluid Provider";
    }
}
