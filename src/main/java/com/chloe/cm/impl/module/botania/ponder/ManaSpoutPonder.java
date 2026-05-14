package com.chloe.cm.impl.module.botania.ponder;

import com.chloe.cm.impl.module.botania.server.blockentity.ManaSpoutBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

public class ManaSpoutPonder {
    
    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.configureBasePlate(0, 0, 5);
        scene.title("botania/mana_manipulators/mana_spout", "Infusing items with mana using a Mana Spout");
        scene.showBasePlate();
        
        scene.idle(30);
        
        scene.world().showSection(util.select().column(3, 2), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 3.5, 2.5))
            .placeNearTarget()
            .text("The Mana Spout can handle all recipes a Mana Pool can.");
        
        scene.idle(30);
        
        BlockPos manaPool = new BlockPos(1, 1, 2);
        
        scene.world().showSection(util.select().position(manaPool), Direction.DOWN);
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(1000000);
        });
        
        scene.idle(30);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 3.5, 2.5))
            .placeNearTarget()
            .text("Simply fill it with mana, using a spreader or anything else");
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(-900000);
        });
        
        BlockPos spout = new BlockPos(3, 3, 2);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(100000);
        });
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 1.5, 2.5))
            .placeNearTarget()
            .text("Then put the item you want to infuse in below it.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 1.5, 2.5))
            .placeNearTarget()
            .text("You need either a belt or a depot.");
        
        scene.idle(40);
        
        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
        BlockPos depot = new BlockPos(3, 1, 2);
        
        scene.world().createItemOnBeltLike(depot, Direction.NORTH, bottle);
        
        scene.idle(10);
        
        scene.world().modifyBlockEntityNBT(util.select().position(3, 3, 2), ManaSpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
        
        scene.idle(20);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(-50000);
        });
        
        scene.world().removeItemsFromBelt(depot);
        
        ItemStack manaBottle = new ItemStack(BotaniaItems.manaBottle);
        
        scene.world().createItemOnBeltLike(depot, Direction.UP, manaBottle);
        
        scene.idle(20);
        
        scene.world().removeItemsFromBelt(depot);
        
        scene.world().hideSection(util.select().position(0, 1, 1), Direction.UP);
        
        scene.world().showSection(util.select().column(1, 3), Direction.DOWN);
        
        scene.world().hideSection(util.select().column(3, 2), Direction.UP);
        scene.world().hideSection(util.select().position(manaPool), Direction.UP);
        
        scene.idle(40);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(-100000);
        });
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(1000000);
        });
        
        scene.world().showSection(util.select().column(3, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(manaPool), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(1.5, 2.5, 2.5))
            .placeNearTarget()
            .attachKeyFrame()
            .text("If you have a pool with a spark on it.");
        
        scene.idle(40);
        
        scene.world().showSection(util.select().position(3, 4, 3), Direction.DOWN);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 4.5, 2.5))
            .placeNearTarget()
            .text("And the Mana Spout also has one.");
        
        scene.idle(40);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 3.5, 2.5))
            .placeNearTarget()
            .text("Then the spout will automatically refill when out of mana, if a recipe is in progress.");
        
        scene.idle(40);
        
        scene.world().createItemOnBeltLike(depot, Direction.NORTH, bottle);
        
        scene.idle(5);
        
        scene.world().modifyBlockEntity(manaPool, ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(-900000);
        });
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(100000);
        });
        
        scene.idle(10);
        
        scene.world().modifyBlockEntityNBT(util.select().position(spout), ManaSpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
        
        scene.idle(20);
        
        scene.world().removeItemsFromBelt(depot);
        
        scene.world().createItemOnBeltLike(depot, Direction.UP, manaBottle);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(-50000);
        });
        
        scene.idle(30);
    }
    
    public static void ponderCatalyst(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        
        scene.configureBasePlate(0, 0, 5);
        scene.title("botania/mana_manipulators/mana_spout_catalyst", "Handling infusion catalysts");
        scene.showBasePlate();
        
        scene.idle(30);
        
        scene.world().showSection(util.select().column(3, 2), Direction.DOWN);
        scene.world().showSection(util.select().column(0, 4), Direction.DOWN);
        
        scene.world().modifyBlockEntity(new BlockPos(0, 1, 4), ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(1000000);
        });
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 3.5, 2.5))
            .placeNearTarget()
            .text("Some recipes might need a block under the pool to work.");
        
        scene.idle(40);
        
        ItemStack redstone = new ItemStack(Items.REDSTONE);
        BlockPos depot = new BlockPos(3, 1, 2);
        
        scene.world().createItemOnBeltLike(depot, Direction.NORTH, redstone);
        
        scene.idle(20);
        
        scene.overlay().showText(30)
            .pointAt(new Vec3(3.5, 3.5, 2.5))
            .placeNearTarget()
            .text("You can achieve the same behaviour by replacing the block under the depot / belt.");
        
        scene.idle(40);
        
        scene.world().replaceBlocks(util.select().position(3, 0, 2), BotaniaBlocks.alchemyCatalyst.defaultBlockState(), true);
        
        scene.idle(10);
        
        scene.world().modifyBlockEntity(new BlockPos(0, 1, 4), ManaPoolBlockEntity.class, pool -> {
            pool.receiveMana(-900000);
        });
        
        BlockPos spout = new BlockPos(3, 3, 2);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(100000);
        });
        
        scene.world().modifyBlockEntityNBT(util.select().position(3, 3, 2), ManaSpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
        
        scene.idle(20);
        
        scene.world().modifyBlockEntity(spout, ManaSpoutBlockEntity.class, s -> {
            s.receiveMana(-50000);
        });
        
        scene.world().removeItemsFromBelt(depot);
        
        ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
        
        scene.world().createItemOnBeltLike(depot, Direction.UP, glowstone);
        
        scene.idle(20);
    }
}