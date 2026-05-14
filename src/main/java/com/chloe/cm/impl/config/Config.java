package com.chloe.cm.impl.config;

import com.chloe.cm.CreateMechanized;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static Config INSTANCE;
    
    @Expose
    public boolean enableBotania = true;
    
    @Expose
    public boolean enableManaFluid = true;
    
    @Expose
    public boolean enableTinkersConstruct = true;
    
    @Expose
    public boolean enableTCCreateLowHeated = true;
    
    @Expose
    public boolean enableTCCreateAddition = true;
    
    public static void initialize() {
        Config config = new Config();
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();
        
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("CreateMechanized.json");
        
        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                config = gson.fromJson(reader, Config.class);
            } catch (IOException e) {
                
                System.err.println("Failed to read config, using defaults: " + e.getMessage());
            }
        } else {
            try {
                Files.createDirectories(configPath.getParent());
                CreateMechanized.LOGGER.info("Config file not found. Creating a new one with default values.");
            } catch (IOException e) {
                CreateMechanized.LOGGER.error("Failed to create config directory: {}", e.getMessage());
            }
        }
        
        if (config == null) {
            config = new Config();
        }
        
        INSTANCE = config;
        
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            gson.toJson(INSTANCE, writer);
            CreateMechanized.LOGGER.info("Configuration saved to: {}", configPath);
        } catch (IOException e) {
            CreateMechanized.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }
}
