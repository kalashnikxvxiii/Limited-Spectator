package com.karashi.limitedspectator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommonConfig.
 * Tests TOML parsing, default values, and configuration validation.
 */
class CommonConfigTest {

    private CommonConfig config;

    @BeforeEach
    void setUp() {
        config = CommonConfig.getInstance();
        // Reset to defaults before each test
        resetConfigToDefaults();
    }
    
    private void resetConfigToDefaults() {
        config.maxDistance = 75.0;
        config.allowDimensionTravel = false;
        config.teleportBackOnExceed = true;
        config.resetPositionOnLogout = true;
        config.enableInvulnerability = true;
        config.enableFlight = true;
        config.spectatorGamemode = "ADVENTURE";
        config.allowPvp = false;
        config.allowItemDrop = false;
        config.allowItemPickup = false;
        config.allowInventoryCrafting = false;
        config.spectatorCommandPermissionLevel = 0;
        config.survivalCommandPermissionLevel = 0;
        config.requireOpForSpectator = false;
        config.useActionBarMessages = true;
        config.showDistanceWarnings = true;
        
        // Use ArrayList (mutable) instead of List.of() (immutable)
        config.interactableBlocks = new ArrayList<>(List.of(
            "minecraft:oak_door", "minecraft:iron_door",
            "minecraft:oak_trapdoor", "minecraft:iron_trapdoor",
            "minecraft:oak_fence_gate", "minecraft:spruce_fence_gate",
            "minecraft:birch_fence_gate", "minecraft:jungle_fence_gate",
            "minecraft:acacia_fence_gate", "minecraft:dark_oak_fence_gate",
            "minecraft:mangrove_fence_gate", "minecraft:cherry_fence_gate",
            "minecraft:bamboo_fence_gate", "minecraft:crimson_fence_gate",
            "minecraft:warped_fence_gate"
        ));
    }

    @Test
    void testDefaultValues() {
        // Movement restrictions
        assertEquals(75.0, config.maxDistance, "Default max distance should be 75.0");
        assertFalse(config.allowDimensionTravel, "Default dimension travel should be false");
        assertTrue(config.teleportBackOnExceed, "Default teleport back should be true");
        assertTrue(config.resetPositionOnLogout, "Default reset position should be true");

        // Player abilities
        assertTrue(config.enableInvulnerability, "Default invulnerability should be true");
        assertTrue(config.enableFlight, "Default flight should be true");
        assertEquals("ADVENTURE", config.spectatorGamemode, "Default gamemode should be ADVENTURE");

        // Interactions
        assertFalse(config.allowPvp, "Default PvP should be false");
        assertFalse(config.allowItemDrop, "Default item drop should be false");
        assertFalse(config.allowItemPickup, "Default item pickup should be false");
        assertFalse(config.allowInventoryCrafting, "Default inventory crafting should be false");

        // Permissions
        assertEquals(0, config.spectatorCommandPermissionLevel, "Default spectator permission should be 0");
        assertEquals(0, config.survivalCommandPermissionLevel, "Default survival permission should be 0");
        assertFalse(config.requireOpForSpectator, "Default OP requirement should be false");

        // Messages
        assertTrue(config.useActionBarMessages, "Default action bar messages should be true");
        assertTrue(config.showDistanceWarnings, "Default distance warnings should be true");
    }

    @Test
    void testInteractableBlocksDefaults() {
        assertNotNull(config.interactableBlocks, "Interactable blocks should not be null");
        assertFalse(config.interactableBlocks.isEmpty(), "Interactable blocks should not be empty");
        
        // Check for default blocks
        assertTrue(config.interactableBlocks.contains("minecraft:oak_door"), 
            "Should contain oak_door");
        assertTrue(config.interactableBlocks.contains("minecraft:iron_door"), 
            "Should contain iron_door");
        assertTrue(config.interactableBlocks.contains("minecraft:oak_trapdoor"), 
            "Should contain oak_trapdoor");
        assertTrue(config.interactableBlocks.contains("minecraft:oak_fence_gate"), 
            "Should contain oak_fence_gate");
    }

    @Test
    void testLoadFromValidTomlFile(@TempDir Path tempDir) throws IOException {
        // Create test config file
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [movement_restrictions]
              max_distance = 100.0
              allow_dimension_travel = true
              teleport_back_on_exceed = false
              reset_position_on_logout = false
            
            [player_abilities]
              enable_invulnerability = false
              enable_flight = false
              spectator_gamemode = "SPECTATOR"
            
            [interactions]
              allow_pvp = true
              allow_item_drop = true
              allow_item_pickup = true
              allow_inventory_crafting = true
              interactable_blocks = ["minecraft:lever", "minecraft:stone_button"]
            
            [permissions]
              spectator_command_permission_level = 2
              survival_command_permission_level = 3
              require_op_for_spectator = true
            
            [messages]
              use_action_bar_messages = false
              show_distance_warnings = false
            """;
        Files.writeString(configFile, tomlContent);

        // Load config
        config.loadFromFile(tempDir);

        // Verify loaded values
        assertEquals(100.0, config.maxDistance);
        assertTrue(config.allowDimensionTravel);
        assertFalse(config.teleportBackOnExceed);
        assertFalse(config.resetPositionOnLogout);
        
        assertFalse(config.enableInvulnerability);
        assertFalse(config.enableFlight);
        assertEquals("SPECTATOR", config.spectatorGamemode);
        
        assertTrue(config.allowPvp);
        assertTrue(config.allowItemDrop);
        assertTrue(config.allowItemPickup);
        assertTrue(config.allowInventoryCrafting);
        
        assertEquals(2, config.spectatorCommandPermissionLevel);
        assertEquals(3, config.survivalCommandPermissionLevel);
        assertTrue(config.requireOpForSpectator);
        
        assertFalse(config.useActionBarMessages);
        assertFalse(config.showDistanceWarnings);
        
        assertEquals(2, config.interactableBlocks.size());
        assertTrue(config.interactableBlocks.contains("minecraft:lever"));
        assertTrue(config.interactableBlocks.contains("minecraft:stone_button"));
    }

    @Test
    void testLoadFromMissingFile(@TempDir Path tempDir) {
        // Load from non-existent directory
        config.loadFromFile(tempDir.resolve("nonexistent"));

        // Should fall back to defaults
        assertEquals(75.0, config.maxDistance);
        assertFalse(config.allowDimensionTravel);
        assertTrue(config.enableFlight);
    }

    @Test
    void testLoadFromInvalidToml(@TempDir Path tempDir) throws IOException {
        // Create invalid TOML file
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        Files.writeString(configFile, "invalid toml content [[[");

        // Should handle gracefully and use defaults
        config.loadFromFile(tempDir);
        
        assertEquals(75.0, config.maxDistance);
        assertTrue(config.enableFlight);
    }

    @Test
    void testLoadFromPartialToml(@TempDir Path tempDir) throws IOException {
        // Create partial config (only some values)
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [movement_restrictions]
              max_distance = 50.0
            
            [interactions]
              allow_pvp = true
            """;
        Files.writeString(configFile, tomlContent);

        config.loadFromFile(tempDir);

        // Specified values should be loaded
        assertEquals(50.0, config.maxDistance);
        assertTrue(config.allowPvp);
        
        // Unspecified values should use defaults
        assertFalse(config.allowDimensionTravel);
        assertTrue(config.enableFlight);
        assertFalse(config.allowItemDrop);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0, 50.0, 100.0, 1000.0})
    void testMaxDistanceValues(double distance, @TempDir Path tempDir) throws IOException {
        // Reset before each parameterized test iteration
        resetConfigToDefaults();
        
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        
        // Use Locale.US to ensure dot as decimal separator
        String tomlContent = String.format(Locale.US, """
            [movement_restrictions]
              max_distance = %.1f
            """, distance);
        Files.writeString(configFile, tomlContent);

        config.loadFromFile(tempDir);
        assertEquals(distance, config.maxDistance, 
            String.format(Locale.US, "Expected maxDistance to be %.1f after loading config", distance));
    }

    @Test
    void testEmptyInteractableBlocks(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [interactions]
              interactable_blocks = []
            """;
        Files.writeString(configFile, tomlContent);

        config.loadFromFile(tempDir);
        
        assertNotNull(config.interactableBlocks);
        assertTrue(config.interactableBlocks.isEmpty());
    }

    @Test
    void testSingletonPattern() {
        CommonConfig instance1 = CommonConfig.getInstance();
        CommonConfig instance2 = CommonConfig.getInstance();
        
        assertSame(instance1, instance2, "getInstance should return same instance");
    }

    @Test
    void testPermissionLevelBounds(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [permissions]
              spectator_command_permission_level = 4
              survival_command_permission_level = 0
            """;
        Files.writeString(configFile, tomlContent);

        config.loadFromFile(tempDir);
        
        assertEquals(4, config.spectatorCommandPermissionLevel);
        assertEquals(0, config.survivalCommandPermissionLevel);
    }

    @Test
    void testGamemodeValues(@TempDir Path tempDir) throws IOException {
        // Test ADVENTURE
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [player_abilities]
              spectator_gamemode = "ADVENTURE"
            """;
        Files.writeString(configFile, tomlContent);
        config.loadFromFile(tempDir);
        assertEquals("ADVENTURE", config.spectatorGamemode);

        // Test SPECTATOR
        tomlContent = """
            [player_abilities]
              spectator_gamemode = "SPECTATOR"
            """;
        Files.writeString(configFile, tomlContent);
        config.loadFromFile(tempDir);
        assertEquals("SPECTATOR", config.spectatorGamemode);
    }

    @Test
    void testMultipleInteractableBlocks(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("limitedspectator-common.toml");
        String tomlContent = """
            [interactions]
              interactable_blocks = [
                "minecraft:oak_door",
                "minecraft:lever",
                "minecraft:stone_button",
                "minecraft:oak_button",
                "minecraft:birch_door"
              ]
            """;
        Files.writeString(configFile, tomlContent);

        config.loadFromFile(tempDir);
        
        assertEquals(5, config.interactableBlocks.size());
        assertTrue(config.interactableBlocks.contains("minecraft:oak_door"));
        assertTrue(config.interactableBlocks.contains("minecraft:lever"));
        assertTrue(config.interactableBlocks.contains("minecraft:stone_button"));
        assertTrue(config.interactableBlocks.contains("minecraft:oak_button"));
        assertTrue(config.interactableBlocks.contains("minecraft:birch_door"));
    }
}
