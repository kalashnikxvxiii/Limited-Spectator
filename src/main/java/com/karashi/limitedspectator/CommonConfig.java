package com.karashi.limitedspectator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader-agnostic configuration system for Limited Spectator.
 * Provides TOML-like configuration parsing for Fabric and Quilt.
 * 
 * This class reads from config/limitedspectator-common.toml and provides
 * configuration values to all loaders without loader-specific dependencies.
 */
public class CommonConfig {
    
    private static final String CONFIG_FILENAME = "limitedspectator-common.toml";
    private static CommonConfig instance;
    
    // Movement Restrictions
    public double maxDistance = 75.0;
    public boolean allowDimensionTravel = false;
    public boolean teleportBackOnExceed = true;
    public boolean resetPositionOnLogout = true;
    
    // Player Abilities
    public boolean enableInvulnerability = true;
    public boolean enableFlight = true;
    public String spectatorGamemode = "ADVENTURE";
    
    // Interactions
    public boolean allowPvp = false;
    public boolean allowItemDrop = false;
    public boolean allowItemPickup = false;
    public boolean allowInventoryCrafting = false;
    public List<String> interactableBlocks = new ArrayList<>();
    
    // Commands
    public int spectatorCommandPermissionLevel = 0;
    public int survivalCommandPermissionLevel = 0;
    public boolean requireOpForSpectator = false;
    
    // Messages
    public boolean useActionBarMessages = true;
    public boolean showDistanceWarnings = true;
    
    private CommonConfig() {
        loadDefaults();
    }
    
    public static CommonConfig getInstance() {
        if (instance == null) {
            instance = new CommonConfig();
        }
        return instance;
    }
    
    /**
     * Load configuration from file. Falls back to defaults if file doesn't exist.
     */
    public void loadFromFile(Path configDir) {
        try {
            Path configFile = configDir.resolve(CONFIG_FILENAME);
            if (Files.exists(configFile)) {
                String content = Files.readString(configFile);
                parseToml(content);
            } else {
                // File doesn't exist - use defaults
                loadDefaults();
            }
        } catch (IOException e) {
            // Error reading file - use defaults
            loadDefaults();
        }
    }
    
    /**
     * Parse TOML-like configuration format.
     * Supports basic key=value pairs and sections [section].
     */
    private void parseToml(String content) {
        String[] lines = content.split("\n");
        String currentSection = "";
        
        for (String line : lines) {
            line = line.trim();
            
            // Skip comments and empty lines
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            // Parse section headers
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                continue;
            }
            
            // Parse key=value pairs
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim().replaceAll("[\"']", "");
                
                parseConfigValue(currentSection, key, value);
            }
        }
    }
    
    /**
     * Parse individual configuration value based on section and key.
     */
    private void parseConfigValue(String section, String key, String value) {
        try {
            switch (section) {
                case "movement":
                    switch (key) {
                        case "max_distance":
                            maxDistance = Double.parseDouble(value);
                            break;
                        case "allow_dimension_travel":
                            allowDimensionTravel = parseBoolean(value);
                            break;
                        case "teleport_back_on_exceed":
                            teleportBackOnExceed = parseBoolean(value);
                            break;
                        case "reset_position_on_logout":
                            resetPositionOnLogout = parseBoolean(value);
                            break;
                    }
                    break;
                    
                case "player_abilities":
                    switch (key) {
                        case "enable_invulnerability":
                            enableInvulnerability = parseBoolean(value);
                            break;
                        case "enable_flight":
                            enableFlight = parseBoolean(value);
                            break;
                        case "spectator_gamemode":
                            spectatorGamemode = value;
                            break;
                    }
                    break;
                    
                case "interactions":
                    switch (key) {
                        case "allow_pvp":
                            allowPvp = parseBoolean(value);
                            break;
                        case "allow_item_drop":
                            allowItemDrop = parseBoolean(value);
                            break;
                        case "allow_item_pickup":
                            allowItemPickup = parseBoolean(value);
                            break;
                        case "allow_inventory_crafting":
                            allowInventoryCrafting = parseBoolean(value);
                            break;
                        case "interactable_blocks":
                            // Parse array format: ["block1", "block2"]
                            parseBlockArray(value);
                            break;
                    }
                    break;
                    
                case "commands":
                    switch (key) {
                        case "spectator_command_permission_level":
                            spectatorCommandPermissionLevel = Integer.parseInt(value);
                            break;
                        case "survival_command_permission_level":
                            survivalCommandPermissionLevel = Integer.parseInt(value);
                            break;
                        case "require_op_for_spectator":
                            requireOpForSpectator = parseBoolean(value);
                            break;
                    }
                    break;
                    
                case "messages":
                    switch (key) {
                        case "use_action_bar_messages":
                            useActionBarMessages = parseBoolean(value);
                            break;
                        case "show_distance_warnings":
                            showDistanceWarnings = parseBoolean(value);
                            break;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            // Invalid value - use default
        }
    }
    
    /**
     * Parse boolean value from string.
     */
    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true");
    }
    
    /**
     * Parse block array from TOML format: ["block1", "block2", ...]
     */
    private void parseBlockArray(String value) {
        if (!value.startsWith("[") || !value.endsWith("]")) {
            return;
        }
        
        String content = value.substring(1, value.length() - 1);
        String[] blocks = content.split(",");
        
        interactableBlocks.clear();
        for (String block : blocks) {
            String trimmed = block.trim().replaceAll("[\"']", "");
            if (!trimmed.isEmpty()) {
                interactableBlocks.add(trimmed);
            }
        }
    }
    
    /**
     * Load default configuration values.
     */
    private void loadDefaults() {
        maxDistance = 75.0;
        allowDimensionTravel = false;
        teleportBackOnExceed = true;
        resetPositionOnLogout = true;
        
        enableInvulnerability = true;
        enableFlight = true;
        spectatorGamemode = "ADVENTURE";
        
        allowPvp = false;
        allowItemDrop = false;
        allowItemPickup = false;
        allowInventoryCrafting = false;
        
        interactableBlocks.clear();
        interactableBlocks.add("minecraft:oak_door");
        interactableBlocks.add("minecraft:iron_door");
        interactableBlocks.add("minecraft:oak_trapdoor");
        interactableBlocks.add("minecraft:iron_trapdoor");
        interactableBlocks.add("minecraft:oak_fence_gate");
        interactableBlocks.add("minecraft:spruce_fence_gate");
        interactableBlocks.add("minecraft:birch_fence_gate");
        interactableBlocks.add("minecraft:jungle_fence_gate");
        interactableBlocks.add("minecraft:acacia_fence_gate");
        interactableBlocks.add("minecraft:dark_oak_fence_gate");
        interactableBlocks.add("minecraft:mangrove_fence_gate");
        interactableBlocks.add("minecraft:cherry_fence_gate");
        interactableBlocks.add("minecraft:bamboo_fence_gate");
        interactableBlocks.add("minecraft:crimson_fence_gate");
        interactableBlocks.add("minecraft:warped_fence_gate");
        
        spectatorCommandPermissionLevel = 0;
        survivalCommandPermissionLevel = 0;
        requireOpForSpectator = false;
        
        useActionBarMessages = true;
        showDistanceWarnings = true;
    }
}
