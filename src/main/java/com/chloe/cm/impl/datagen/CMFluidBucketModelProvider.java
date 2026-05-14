package com.chloe.cm.impl.datagen;

import com.chloe.cm.CMConstants;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;

public class CMFluidBucketModelProvider extends FluidBucketModelProvider {
    public CMFluidBucketModelProvider(PackOutput packOutput) {
        super(packOutput, CMConstants.MODID);
    }
}
