package com.chloe.cm.impl.module.tconstruct.ponder;

import com.chloe.cm.impl.module.tconstruct.server.blockentity.SearedBurnerBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class SearedBurnerPonder {
    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("tconstruct/seared_burner", "Heat the Seared Smeltery, the Create way.");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 1.5))
            .placeNearTarget()
            .text("You can use a Seared Burner to heat your smeltery.");
        
        scene.idle(50);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(2.5, 1.5, 1.5))
            .placeNearTarget()
            .text("The efficiency will change based on the Burner's Heat level.");
        
        scene.idle(50);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(2.5, 1.5, 1.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("The Seared Burner takes the place of the tank, it not suggested having any other tank / burner with it.");
        
        scene.idle(50);

        scene.overlay().showText(40)
            .pointAt(new Vec3(2.5, 1.5, 1.5))
            .placeNearTarget()
            .text("It takes any fuel a Blazing Burner can take, including super-heating ones.");
        
        scene.idle(50);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(2.5, 1.5, 1.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("You can right click it with a wrench to cycle the seared style, to fit it with your smeltery.");
        
        scene.idle(50);
        
        scene.overlay().showControls(new Vec3(2.5, 2.5, 1.5), Pointing.DOWN, 20)
            .rightClick()
            .withItem(AllItems.WRENCH.asStack());
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 1), SearedBurnerBlockEntity.class, SearedBurnerBlockEntity::increaseMode);
        
        scene.idle(50);
        
        scene.overlay().showControls(new Vec3(2.5, 2.5, 1.5), Pointing.DOWN, 20)
            .rightClick()
            .withItem(AllItems.WRENCH.asStack());
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 1), SearedBurnerBlockEntity.class, SearedBurnerBlockEntity::increaseMode);
        
        scene.idle(50);
    }
}