package com.chloe.cm.impl.module.botania.server.behaviour;

import com.chloe.cm.impl.module.botania.server.blockentity.TradeManaPedestalBlockEntity;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TradeManaPedestalBehaviour extends DepotBehaviour {
    
    public TradeManaPedestalBehaviour(SmartBlockEntity be) {
        super(be);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        TradeManaPedestalBlockEntity be = getBE();
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
                    if (blockEntity.getLevel().getBlockEntity(offset) instanceof TradingRiftBlockEntity) {
                        toRet.add(offset);
                    }
                }
            }
        }
        
        return toRet;
    }
    
    private TradeManaPedestalBlockEntity getBE() {
        return ((TradeManaPedestalBlockEntity) blockEntity);
    }
}
