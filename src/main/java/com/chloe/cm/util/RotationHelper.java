package com.chloe.cm.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import vazkii.botania.common.helper.VecHelper;

public class RotationHelper {
    
    public static Quaternionf rotateAxis(Vec3 axis, float degrees) {
        Vec3 n = axis.normalize();
        return (new Quaternionf()).rotateAxis(VecHelper.toRadians(degrees), (float)n.x, (float)n.y, (float)n.z);
    }
    
}
