package com.chloe.cm.impl.module.botania.ponder;

import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradeManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

public class TradingRiftPonder {
    
    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("botania/mana_manipulators/trading_rift", "Industrial Elven Trade");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().column(2, 2), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The Trading Rift Stabilizer (T.R.S.) allows you to automate trading with Elves");
        
        scene.idle(50);
        
        scene.world().showSection(util.select().fromTo(0, 1, 0, 0, 1, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(0, 1, 4, 3, 1, 4), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 1, 4), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 0, 1, 1, 0), Direction.DOWN);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(0.5, 1.5, 2.5))
            .placeNearTarget()
            .text("Simply place 1 Terrestrial Item Encapsulator (T.I.E) per item required in range of the T.R.S.");
        
        scene.idle(50);
        
        scene.world().showSection(util.select().fromTo(1, 1, 1, 1, 1, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(1, 1, 3, 3, 1, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(3, 1, 1, 3, 1, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(3, 1, 1, 1, 1, 1), Direction.DOWN);
        
        scene.world().setBlock(new BlockPos(3, 1, 3), CMBotaniaBlocks.TRADE_MANA_PEDESTAL.get().defaultBlockState(), false);
        
        scene.overlay().showText(40)
            .pointAt(new Vec3(1.5, 1.5, 2.5))
            .placeNearTarget()
            .text("And also place 1 Dimensionally Stable Item Encapsulator (D.S.I.E) per output in range of the T.R.S.");
        
        scene.idle(50);
        
        scene.world().hideSection(util.select().position(0, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().position(4, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().position(0, 1, 3), Direction.UP);
        scene.world().hideSection(util.select().position(3, 1, 3), Direction.UP);
        
        scene.world().hideSection(util.select().fromTo(0, 1, 4, 3, 1, 4), Direction.UP);
        scene.world().hideSection(util.select().fromTo(0, 1, 0, 0, 1, 3), Direction.UP);
        scene.world().hideSection(util.select().fromTo(4, 1, 0, 1, 1, 0), Direction.UP);
        
        scene.world().hideSection(util.select().fromTo(1, 1, 1, 3, 1, 1), Direction.UP);
        scene.world().hideSection(util.select().fromTo(1, 1, 3, 3, 1, 3), Direction.UP);
        scene.world().hideSection(util.select().position(3, 1, 2), Direction.UP);
        
        scene.idle(20);
        
        
        BlockPos manaPool = new BlockPos(3, 1, 3);
        
        scene.world().setBlock(manaPool, BotaniaBlocks.manaPool.defaultBlockState(), false);
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(1000000);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(4.5, 1.5, 2.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("Then put the needed items inside the T.I.E.");
        
        scene.idle(40);
        
        scene.world().showSection(util.select().position(manaPool), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 1.5, 3.5))
            .placeNearTarget()
            .text("Make sure you have a mana pool nearby,");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 2.5, 3.5))
            .placeNearTarget()
            .text("With a natura pylon above, (botania and ponder don't like each other, trust that there is one)");
        
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
            .text("and that the T.R.S. is spinning at least at FAST speed.");
        
        scene.idle(40);
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(-AlfheimPortalBlockEntity.MANA_COST_OPENING);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("If it's working, you'll know.");
        
        scene.idle(10);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), TradingRiftBlockEntity.class, plate -> {
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
            .pointAt(new Vec3(1.5, 1.5, 2.5))
            .placeNearTarget()
            .text("And all outputs with appear on the relative D.S.I.E.");
        
        scene.idle(40);
        
        scene.overlay().showText(60)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("Same rules of the R.A.C. apply, those being craft speed, recipe saving, and start up delay.");
        
        scene.idle(70);
        
        scene.overlay().showText(70)
            .pointAt(new Vec3(2.5, 0.5, 2.5))
            .placeNearTarget()
            .text("Be aware that every time the rift shuts down and re-starts, it will consume mana, just like an alfheim portal would.");
        
        scene.idle(80);
        
        scene.overlay().showText(70)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("Both D.S.I.E., T.I.E. and T.R.S. can be connected to Mechanical Arms.");
        
        scene.idle(80);
    }
    
    public static void ponderOverload(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.title("botania/mana_manipulators/trading_rift_overload", "Beware of Encapsulator Overload!");
        scene.showBasePlate();
        
        scene.idle(10);
        
        scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), TradeManaPedestalBlockEntity.class, ped -> {
            ped.setOverloaded(false);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(0.5, 1.5, 2.5))
            .placeNearTarget()
            .text("The T.R.S will search for T.I.E. and D.S.I.E. regardless of line of sight.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("This means that a D.S.I.E. / T.I.E. might be in range of more R.A.C / T.R.S. simultaneously.");
        
        scene.idle(40);
        
        scene.world().modifyBlockEntity(new BlockPos(2, 1, 2), TradeManaPedestalBlockEntity.class, ped -> {
            ped.setOverloaded(true);
        });
        
        scene.overlay().showText(50)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("If that happens, the D.S.I.E. / T.I.E. will become overloaded, and won't be able to provide crafting materials.");
        
        scene.idle(60);
        
        scene.overlay().showText(50)
            .pointAt(new Vec3(2.5, 1.5, 2.5))
            .placeNearTarget()
            .text("If a D.S.I.E. / T.I.E. becomes overloaded mid-recipe, the active one will be finished, but the next won't start.");
        
        scene.idle(60);
    }
}