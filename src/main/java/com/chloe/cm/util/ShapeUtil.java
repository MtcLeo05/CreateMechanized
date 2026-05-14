package com.chloe.cm.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtil {
    
    public static VoxelShape shapeFromDimension(int x1, int y1, int z1, int x2, int y2, int z2){
        return Block.box(x1, y1, z1, x1 + x2, y1 + y2 , z1 + z2);
    }
    
    private  static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        
        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        
        return buffer[0];
    }
    
    public static VoxelShape clockwise(VoxelShape shape){
        return ShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, shape);
    }
    
    public static VoxelShape counterClockwise(VoxelShape shape){
        return ShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, shape);
    }
    
    public static VoxelShape rotateTwice(VoxelShape shape){
        return ShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, shape);
    }
    
}
