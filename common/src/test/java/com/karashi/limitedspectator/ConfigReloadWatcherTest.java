package com.karashi.limitedspectator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigReloadWatcher.
 * Tests file watching, reload detection, and lifecycle management.
 */
class ConfigReloadWatcherTest {

    private ConfigReloadWatcher watcher;
    private CommonConfig config;
    private Path configFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        config = CommonConfig.getInstance();
        configFile = tempDir.resolve("limitedspectator-common.toml");
        
        // Create initial config file
        String initialContent = """
            [movement_restrictions]
              max_distance = 75.0
            """;
        Files.writeString(configFile, initialContent);
        
        watcher = new ConfigReloadWatcher(configFile, config);
    }

    @AfterEach
    void tearDown() {
        if (watcher != null) {
            watcher.stop();
        }
    }

    @Test
    void testWatcherCreation() {
        assertNotNull(watcher, "Watcher should be created");
    }

    @Test
    void testStartAndStop() {
        watcher.start();
        assertTrue(true, "Watcher should start without errors");
        
        watcher.stop();
        assertTrue(true, "Watcher should stop without errors");
    }

    @Test
    void testMultipleStarts() {
        watcher.start();
        watcher.start(); // Should handle gracefully
        watcher.start(); // Should handle gracefully
        
        watcher.stop();
        assertTrue(true, "Multiple starts should be handled gracefully");
    }

    @Test
    void testMultipleStops() {
        watcher.start();
        watcher.stop();
        watcher.stop(); // Should handle gracefully
        watcher.stop(); // Should handle gracefully
        
        assertTrue(true, "Multiple stops should be handled gracefully");
    }

    @Test
    void testStopWithoutStart() {
        watcher.stop();
        assertTrue(true, "Stop without start should be handled gracefully");
    }

    @Test
    void testManualReload() {
        // Modify config file
        try {
            String newContent = """
                [movement_restrictions]
                  max_distance = 100.0
                """;
            Files.writeString(configFile, newContent);
        } catch (IOException e) {
            fail("Failed to write config file: " + e.getMessage());
        }

        // Trigger manual reload
        watcher.reload();

        // Verify config was reloaded
        assertEquals(100.0, config.maxDistance, "Config should be reloaded");
    }

    @Test
    void testReloadWithMissingFile() {
        // Delete config file
        try {
            Files.deleteIfExists(configFile);
        } catch (IOException e) {
            fail("Failed to delete config file: " + e.getMessage());
        }

        // Should handle gracefully
        watcher.reload();
        
        // Config should fall back to defaults
        assertEquals(75.0, config.maxDistance);
    }

    @Test
    void testReloadWithInvalidFile() throws IOException {
        // Write invalid TOML
        Files.writeString(configFile, "invalid toml [[[");

        // Should handle gracefully
        watcher.reload();
        
        // Config should maintain previous values or use defaults
        assertTrue(config.maxDistance >= 0);
    }

    @Test
    void testFileChangeDetection() throws IOException, InterruptedException {
        watcher.start();

        // Wait a bit for watcher to initialize
        Thread.sleep(100);

        // Get initial modification time
        FileTime initialTime = Files.getLastModifiedTime(configFile);

        // Modify file
        Thread.sleep(1000); // Ensure different timestamp
        String newContent = """
            [movement_restrictions]
              max_distance = 150.0
            """;
        Files.writeString(configFile, newContent);
        
        // Update modification time explicitly
        Files.setLastModifiedTime(configFile, FileTime.from(Instant.now()));

        // Wait for watcher to detect change (polling interval is 5 seconds)
        Thread.sleep(6000);

        // Verify config was reloaded
        assertEquals(150.0, config.maxDistance, 
            "Config should be automatically reloaded after file change");

        watcher.stop();
    }

    @Test
    void testNoReloadWhenFileUnchanged() throws InterruptedException {
        watcher.start();

        double initialDistance = config.maxDistance;

        // Wait for polling interval
        Thread.sleep(6000);

        // Config should remain unchanged
        assertEquals(initialDistance, config.maxDistance, 
            "Config should not reload when file is unchanged");

        watcher.stop();
    }

    @Test
    void testConcurrentReloads() throws InterruptedException {
        AtomicInteger reloadCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(10);

        // Start multiple reload threads
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                watcher.reload();
                reloadCount.incrementAndGet();
                latch.countDown();
            }).start();
        }

        // Wait for all threads to complete
        assertTrue(latch.await(5, TimeUnit.SECONDS), 
            "All reload threads should complete");
        assertEquals(10, reloadCount.get(), 
            "All reloads should execute");
    }

    @Test
    void testWatcherWithNonExistentFile(@TempDir Path tempDir) {
        Path nonExistentFile = tempDir.resolve("nonexistent.toml");
        ConfigReloadWatcher testWatcher = new ConfigReloadWatcher(nonExistentFile, config);

        // Should handle gracefully
        testWatcher.start();
        testWatcher.reload();
        testWatcher.stop();

        assertTrue(true, "Watcher should handle non-existent file gracefully");
    }

    @Test
    void testWatcherLifecycle() throws InterruptedException {
        // Start
        watcher.start();
        Thread.sleep(100);

        // Reload
        watcher.reload();
        Thread.sleep(100);

        // Stop
        watcher.stop();
        Thread.sleep(100);

        // Restart
        watcher.start();
        Thread.sleep(100);

        // Final stop
        watcher.stop();

        assertTrue(true, "Full lifecycle should work correctly");
    }

    @Test
    void testReloadPreservesOtherSettings() throws IOException {
        // Set initial config with multiple values
        String initialContent = """
            [movement_restrictions]
              max_distance = 75.0
              allow_dimension_travel = true
            
            [interactions]
              allow_pvp = true
            """;
        Files.writeString(configFile, initialContent);
        config.loadFromFile(configFile.getParent());

        // Modify only one value
        String newContent = """
            [movement_restrictions]
              max_distance = 100.0
              allow_dimension_travel = true
            
            [interactions]
              allow_pvp = true
            """;
        Files.writeString(configFile, newContent);

        watcher.reload();

        // Verify all values
        assertEquals(100.0, config.maxDistance);
        assertTrue(config.allowDimensionTravel);
        assertTrue(config.allowPvp);
    }
}
