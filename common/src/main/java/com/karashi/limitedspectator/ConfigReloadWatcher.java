package com.karashi.limitedspectator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Watches the configuration file for changes and automatically reloads it.
 * 
 * This provides hot-reload functionality for Fabric and Quilt loaders,
 * matching the behavior of NeoForge's ModConfigEvent system.
 * 
 * The watcher polls the file every 5 seconds to detect modifications.
 * When a change is detected, it automatically reloads the configuration.
 */
public class ConfigReloadWatcher {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("LimitedSpectator");
    private static final long POLL_INTERVAL_SECONDS = 5;
    
    private final Path configFile;
    private final CommonConfig config;
    private ScheduledExecutorService scheduler;
    private FileTime lastModifiedTime;
    private volatile boolean running;
    
    public ConfigReloadWatcher(Path configFile, CommonConfig config) {
        this.configFile = configFile;
        this.config = config;
        this.running = false;
        
        // Initialize last modified time
        try {
            if (Files.exists(configFile)) {
                this.lastModifiedTime = Files.getLastModifiedTime(configFile);
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to get initial file modification time", e);
        }
    }
    
    /**
     * Start watching the configuration file for changes.
     */
    public synchronized void start() {
        if (running) {
            return;
        }
        
        // Create new scheduler (allows restart after stop)
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "LimitedSpectator-ConfigWatcher");
            thread.setDaemon(true);
            return thread;
        });
        
        running = true;
        scheduler.scheduleAtFixedRate(
            this::checkForChanges,
            POLL_INTERVAL_SECONDS,
            POLL_INTERVAL_SECONDS,
            TimeUnit.SECONDS
        );
        
        LOGGER.info("Config file watcher started (polling every {} seconds)", POLL_INTERVAL_SECONDS);
    }
    
    /**
     * Stop watching the configuration file.
     */
    public synchronized void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        LOGGER.info("Config file watcher stopped");
    }
    
    /**
     * Manually trigger a configuration reload.
     * This is called when the /reload command is executed.
     */
    public void reload() {
        try {
            config.loadFromFile(configFile.getParent());
            
            // Update last modified time
            if (Files.exists(configFile)) {
                lastModifiedTime = Files.getLastModifiedTime(configFile);
            }
            
            LOGGER.info("Configuration reloaded successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to reload configuration", e);
        }
    }
    
    /**
     * Check if the configuration file has been modified.
     */
    private void checkForChanges() {
        try {
            if (!Files.exists(configFile)) {
                return;
            }
            
            FileTime currentModifiedTime = Files.getLastModifiedTime(configFile);
            
            // Check if file was modified
            if (lastModifiedTime == null || currentModifiedTime.compareTo(lastModifiedTime) > 0) {
                LOGGER.info("Configuration file changed, reloading...");
                
                // Reload configuration
                config.loadFromFile(configFile.getParent());
                
                // Update last modified time
                lastModifiedTime = currentModifiedTime;
                
                LOGGER.info("Configuration reloaded successfully");
            }
        } catch (IOException e) {
            LOGGER.warn("Error checking config file for changes", e);
        } catch (Exception e) {
            LOGGER.error("Error reloading configuration", e);
        }
    }
    
    /**
     * Check if the watcher is currently running.
     */
    public boolean isRunning() {
        return running;
    }
}
