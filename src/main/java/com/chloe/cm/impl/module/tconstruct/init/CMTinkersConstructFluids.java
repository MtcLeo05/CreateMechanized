package com.chloe.cm.impl.module.tconstruct.init;

import com.chloe.cm.CMConstants;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.registration.FluidDeferredRegisterExtension;
import slimeknights.tconstruct.fluids.block.BurningLiquidBlock;

public class CMTinkersConstructFluids {
    
    public static final FluidDeferredRegisterExtension FLUIDS = new FluidDeferredRegisterExtension(CMConstants.MODID);
    
    public static final FlowingFluidObject<ForgeFlowingFluid> LIQUID_HEAT_FLUID = FLUIDS.register("liquid_heat")
        .type(
            FluidType.Properties
                .create()
                .viscosity(10000)
                .descriptionId("fluid." + CMConstants.MODID + ".liquid_heat")
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .motionScale(0.0023)
                .canSwim(false)
                .canDrown(false)
                .pathType(BlockPathTypes.LAVA)
                .adjacentPathType(null)
                .temperature(1300)
                .lightLevel(15)
                .density(3500)
        )
        .block(BurningLiquidBlock.createBurning(MapColor.COLOR_ORANGE, 15, 15, 5.0F))
        .bucket()
        .flowing();
    
    public static final FlowingFluidObject<ForgeFlowingFluid> LIQUID_SUPER_HEAT_FLUID = FLUIDS.register("liquid_super_heat")
        .type(
            FluidType.Properties
                .create()
                .viscosity(10000)
                .descriptionId("fluid." + CMConstants.MODID + ".liquid_super_heat")
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .motionScale(0.0023)
                .canSwim(false)
                .canDrown(false)
                .pathType(BlockPathTypes.LAVA)
                .adjacentPathType(null)
                .temperature(2500)
                .lightLevel(15)
                .density(3500)
        )
        .block(BurningLiquidBlock.createBurning(MapColor.COLOR_LIGHT_BLUE, 15, 15, 10.0F))
        .bucket()
        .flowing();
    
    public static final FlowingFluidObject<ForgeFlowingFluid> LIQUID_LOW_HEAT_FLUID = FLUIDS.register("liquid_low_heat")
        .type(
            FluidType.Properties
                .create()
                .viscosity(10000)
                .descriptionId("fluid." + CMConstants.MODID + ".liquid_low_heat")
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .motionScale(0.0023)
                .canSwim(false)
                .canDrown(false)
                .pathType(BlockPathTypes.LAVA)
                .adjacentPathType(null)
                .temperature(800)
                .lightLevel(15)
                .density(3500)
        )
        .block(BurningLiquidBlock.createBurning(MapColor.COLOR_YELLOW, 15, 15, 2.5F))
        .bucket()
        .flowing();
    
}
