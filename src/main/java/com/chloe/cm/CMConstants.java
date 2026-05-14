package com.chloe.cm;

import net.minecraft.resources.ResourceLocation;

public class CMConstants {
    
    public static final String MODID = "create_mechanized";
    
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    
}
