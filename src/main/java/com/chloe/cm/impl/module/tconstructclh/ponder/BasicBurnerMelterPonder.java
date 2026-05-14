package com.chloe.cm.impl.module.tconstructclh.ponder;

import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;
import zeh.createlowheated.AllBlocks;
import zeh.createlowheated.content.processing.basicburner.BasicBurnerBlock;

public class BasicBurnerMelterPonder {
    
    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("tconstruct/melter_basic_burner", "Heat the Seared Melter, the Create: Low Heated way.");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .text("You can use Basic Burners to heat your melter.");
        
        scene.idle(50);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .text("The efficiency will change based on the Burner's Heat level.");
        
        scene.idle(50);
        
        scene.world().setBlock(
            new BlockPos(3, 1, 3), AllBlocks.BASIC_BURNER.getDefaultState()
                .setValue(BasicBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SMOULDERING)
                .setValue(BasicBurnerBlock.LIT, true),
            false
        );
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Smouldering and Fading Basic Burners will provide little to no heat, they can't even melt most basic metals.");
        
        scene.idle(50);
        
        scene.world().setBlock(
            new BlockPos(3, 1, 3), AllBlocks.BASIC_BURNER.getDefaultState()
                .setValue(BasicBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED)
                .setValue(BasicBurnerBlock.LIT, true),
            false
        );
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Kindled Basic Burners will provide as much heat as a bucket of Lava while active.");
        
        scene.idle(50);
        
        scene.world().setBlock(
            new BlockPos(3, 1, 3), AllBlocks.BASIC_BURNER.getDefaultState()
                .setValue(BasicBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING)
                .setValue(BasicBurnerBlock.LIT, true),
            false
        );
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Seething Basic Burners will provide 2.2x the heat a bucket of Lava while active.");
        
        scene.idle(50);
        
        scene.world().hideSection(util.select().position(3, 2, 3), Direction.UP);
        
        scene.idle(10);
        
        scene.world().setBlock(
            new BlockPos(2, 1, 3),
            com.simibubi.create.AllBlocks.MECHANICAL_PUMP.getDefaultState()
                .setValue(PumpBlock.FACING, Direction.WEST),
            false
        );
        
        scene.world().setBlock(
            new BlockPos(1, 1, 3),
            com.simibubi.create.AllBlocks.GLASS_FLUID_PIPE.getDefaultState()
                .setValue(GlassFluidPipeBlock.AXIS, Direction.Axis.X),
            false
        );
        
        scene.world().setBlock(
            new BlockPos(0, 1, 3),
            com.simibubi.create.AllBlocks.FLUID_TANK.getDefaultState(),
            false
        );
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("All Heat level are considered fluids, and as such can interact with all Fluid based constructs.");
        
        scene.idle(50);
        
        scene.world().setKineticSpeed(util.select().position(2, 1, 3), 64);
        scene.world().propagatePipeChange(new BlockPos(2, 1, 3));
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Burners are theoretically infinite, as long as they are powered.");
        
        scene.idle(50);
    }
}