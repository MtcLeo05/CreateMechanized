package com.chloe.cm.impl.module.botania.server.behaviour;

import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManaPedestalBehaviour extends DepotBehaviour {
    
    public ManaPedestalBehaviour(SmartBlockEntity be) {
        super(be);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        ManaPedestalBlockEntity be = getBE();
        if(!be.isVirtual()) be.setOverloaded(nearbyPlates().size() > 1);
        be.notifyUpdate();
    }
    
    private List<BlockPos> nearbyPlates() {
        BlockPos center = blockEntity.getBlockPos();
        
        int minX = -2, minY = -2, minZ = -2;
        int maxX = 3, maxY = 3, maxZ = 3;
        
        List<BlockPos> toRet = new ArrayList<>();
        
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    BlockPos offset = center.south(x).above(y).east(z);
                    BlockEntity be = blockEntity.getLevel().getBlockEntity(offset);
                    if (be instanceof AutoTerraPlateBlockEntity || be instanceof TradingRiftBlockEntity) {
                        toRet.add(offset);
                    }
                }
            }
        }
        
        return toRet;
    }
    
    private ManaPedestalBlockEntity getBE() {
        return ((ManaPedestalBlockEntity) blockEntity);
    }
}
