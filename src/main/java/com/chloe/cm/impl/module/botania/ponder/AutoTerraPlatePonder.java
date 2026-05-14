package com.chloe.cm.impl.module.botania.ponder;

import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

public class AutoTerraPlatePonder {
    
    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("botania/mana_manipulators/auto_terra_plate", "Automatic Agglomeration Plate");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().column(2, 2), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The Rotational Agglomeration Complex (R.A.C) can handle all your terrasteel needs.");
        
        scene.idle(50);
        
        scene.world().showSection(util.select().fromTo(0, 1, 0, 0, 1, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(0, 1, 4, 3, 1, 4), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 1, 4), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 0, 1, 1, 0), Direction.DOWN);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(0.5, 1.5, 2.5))
            .placeNearTarget()
            .text("Simply place 1 Terrestrial Item Encapsulator (T.I.E) per item required in range of the R.A.C.");
        
        scene.idle(50);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(1.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The R.A.C. has a 2 block \"radius\", including vertically.");
        
        scene.idle(50);
        
        scene.world().hideSection(util.select().position(0, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().position(4, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().fromTo(0, 1, 4, 3, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().fromTo(0, 1, 0, 0, 1, 3), Direction.UP);
        scene.world().hideSection(util.select().fromTo(4, 1, 0, 1, 1, 0), Direction.UP);
        
        scene.idle(20);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(4.5, 1.5, 2.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Then put the needed items inside the T.I.E.");
        
        scene.idle(40);
        
        ItemStack pearl = new ItemStack(BotaniaItems.manaPearl);
        ItemStack diam = new ItemStack(BotaniaItems.manaDiamond);
        ItemStack ing = new ItemStack(BotaniaItems.manaSteel);
        
        BlockPos pearlPed = new BlockPos(4, 1, 3);
        BlockPos diamPed = new BlockPos(4, 1, 2);
        BlockPos ingPed = new BlockPos(4, 1, 1);
        
        scene.overlay().showControls(util.vector().topOf(diamPed), Pointing.DOWN, 20).rightClick();
        
        scene.world().createItemOnBeltLike(pearlPed, Direction.NORTH, pearl);
        scene.world().createItemOnBeltLike(diamPed, Direction.NORTH, diam);
        scene.world().createItemOnBeltLike(ingPed, Direction.NORTH, ing);
        
        scene.idle(30);
        
        BlockPos manaPool = new BlockPos(3, 1, 3);
        
        scene.world().showSection(util.select().position(manaPool), Direction.DOWN);
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(1000000);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .text("Make sure you have a mana source linked to the R.A.C,");
        
        scene.idle(20);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), AutoTerraPlateBlockEntity.class, plate -> {
            plate.receiveMana(1000000);
        });
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(-1000000);
        });
        
        scene.idle(40);
        
        scene.world().replaceBlocks(util.select().fromTo(1, 0, 1, 1, 0, 3), Blocks.AIR.defaultBlockState(), true);
        scene.world().replaceBlocks(util.select().position(2, 0, 1), Blocks.AIR.defaultBlockState(), true);
        scene.world().replaceBlocks(util.select().position( 2, 0, 3), Blocks.AIR.defaultBlockState(), true);
        
        scene.world().replaceBlocks(util.select().position(2, 0, 2), AllBlocks.LARGE_COGWHEEL.getDefaultState().setValue(CogWheelBlock.AXIS, Direction.Axis.Y), false);
        
        scene.idle(10);
        
        scene.world().setKineticSpeed(util.select().fromTo(2, 0, 2, 2, 1, 2), 100);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("and that the R.A.C is spinning at least at FAST speed.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("If it's working, you'll know.");
        
        scene.idle(10);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), AutoTerraPlateBlockEntity.class, plate -> {
            plate.active = true;
        });
        
        scene.idle(30);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(4.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The items will then be consumed.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("A little animation will play.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("And terrasteel will appear in the R.A.C.");
        
        scene.idle(40);
        
        scene.overlay().showText(60)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("Animation and craft time will scale with rotation speed (higher speed, lower time). If rotation is interrupted mid-crafting recipe progress will be saved.");
        
        scene.idle(70);
        
        scene.overlay().showText(70)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("Be aware that there's a delay between first activation (reaching >FAST speed) and craft start, so you should avoid turning it off and on frequently.");
        
        scene.idle(80);
        
        scene.overlay().showText(70)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("Both T.I.E. and R.A.C. can be connected to Mechanical Arms.");
        
        scene.idle(80);
    }
    
    public static void ponderOverload(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("botania/mana_manipulators/auto_terra_plate_overload", "Beware of Encapsulator Overload!");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), ManaPedestalBlockEntity.class, ped -> {
            ped.setOverloaded(false);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(0.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The R.A.C. will search for T.I.E. regardless of line of sight.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("This means that a T.I.E. might be in range of more R.A.C / T.R.S. simultaneously.");
        
        scene.idle(40);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), ManaPedestalBlockEntity.class, ped -> {
            ped.setOverloaded(true);
        });
        
        scene.overlay().showText(50)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("If that happens, the T.I.E. will become overloaded, and won't be able to provide crafting materials.");
        
        scene.idle(60);
        
        scene.overlay().showText(50)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("If a T.I.E. becomes overloaded mid-recipe, the active one will be finished, but the next won't start.");
        
        scene.idle(60);
    }
}