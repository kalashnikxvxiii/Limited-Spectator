package com.karashi.limitedspectator.quilt;

import com.karashi.limitedspectator.SpectatorManager;
import com.karashi.limitedspectator.SpectatorState;
import com.karashi.limitedspectator.DistanceValidationResult;
import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.ConfigReloadWatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quilt entry point for Limited Spectator mod.
 * 
 * Quilt is fully compatible with Fabric API, so most of the implementation
 * is identical to Fabric.
 * 
 * This demonstrates the power of the multi-loader architecture:
 * - Core logic in SpectatorManager (shared)
 * - Minimal loader-specific code
 * - Maximum code reuse
 * 
 */
public class LimitedSpectatorQuilt implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("LimitedSpectator");
    public static final String MOD_ID = "limitedspectator";
    private static SpectatorManager spectatorManager;
    private static ConfigReloadWatcher configWatcher;
    
    @Override
    public void onInitialize() {
        // Load configuration from file (or use defaults)
        java.nio.file.Path configDir = java.nio.file.Paths.get("config");
        java.nio.file.Path configFile = configDir.resolve("limitedspectator-common.toml");
        
        try {
            java.nio.file.Files.createDirectories(configDir);
            CommonConfig.getInstance().loadFromFile(configDir);
        } catch (Exception e) {
            LOGGER.warn("Failed to load config file, using defaults", e);
        }
        
        spectatorManager = new SpectatorManager(QuiltSpectatorConfig.getInstance());
        
        // Initialize config watcher for hot-reload
        configWatcher = new ConfigReloadWatcher(configFile, CommonConfig.getInstance());
        
        registerCommands();
        registerEvents();
        registerLifecycleEvents();
        
        LOGGER.info("Quilt mod initialized with hot-reload support");
    }
    
    private void registerCommands() {
        // Quilt uses Fabric's CommandRegistrationCallback - 100% compatible
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // /spectator command
            dispatcher.register(Commands.literal("spectator")
                .requires(source -> source.hasPermission(0))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    
                    // Set gamemode
                    player.setGameMode(GameType.ADVENTURE);
                    player.getAbilities().mayfly = true;
                    player.getAbilities().invulnerable = true;
                    player.onUpdateAbilities();
                    
                    // Save state using SpectatorManager
                    spectatorManager.enterSpectatorMode(
                        player.getUUID(),
                        player.position(),
                        player.level().dimension()
                    );
                    
                    player.displayClientMessage(
                        Component.literal("Spectator mode activated (Quilt)").withStyle(ChatFormatting.AQUA),
                        true
                    );
                    
                    return 1;
                })
            );
            
            // /survival command
            dispatcher.register(Commands.literal("survival")
                .requires(source -> source.hasPermission(0))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    
                    // Exit spectator mode using SpectatorManager
                    SpectatorState state = spectatorManager.exitSpectatorMode(player.getUUID());
                    
                    if (state != null) {
                        // Teleport back
                        player.teleportTo(
                            state.startPosition().x,
                            state.startPosition().y,
                            state.startPosition().z
                        );
                    }
                    
                    // Reset gamemode
                    player.setGameMode(GameType.SURVIVAL);
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.getAbilities().invulnerable = false;
                    player.onUpdateAbilities();
                    
                    player.displayClientMessage(
                        Component.literal("Survival mode activated (Quilt)").withStyle(ChatFormatting.GREEN),
                        true
                    );
                    
                    return 1;
                })
            );
        });
    }
    
    private void registerEvents() {
        // Server tick - distance validation (uses Fabric API)
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!spectatorManager.getConfig().isDistanceLimitEnabled()) return;
            
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                DistanceValidationResult result = spectatorManager.validateDistance(
                    player.getUUID(),
                    player.position()
                );
                
                if (result.isExceeded()) {
                    var targetPos = result.targetPosition();
                    player.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                    player.displayClientMessage(
                        Component.literal("Distance limit exceeded!").withStyle(ChatFormatting.RED),
                        true
                    );
                }
            }
        });
        
        // Player disconnect - cleanup (uses Fabric API)
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayer player = handler.getPlayer();
            
            if (spectatorManager.isInSpectatorMode(player.getUUID())) {
                SpectatorState state = spectatorManager.exitSpectatorMode(player.getUUID());
                
                if (state != null && spectatorManager.getConfig().shouldResetPositionOnLogout()) {
                    player.teleportTo(
                        state.startPosition().x,
                        state.startPosition().y,
                        state.startPosition().z
                    );
                }
                
                player.setGameMode(GameType.SURVIVAL);
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.getAbilities().invulnerable = false;
                player.onUpdateAbilities();
            }
        });
    }
    
    private void registerLifecycleEvents() {
        // Start config watcher when server starts
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (configWatcher != null) {
                configWatcher.start();
                LOGGER.info("Config hot-reload enabled");
            }
        });
        
        // Stop config watcher when server stops
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (configWatcher != null) {
                configWatcher.stop();
            }
        });
        
        // Hook into /reload command
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success && configWatcher != null) {
                configWatcher.reload();
                LOGGER.info("Configuration reloaded via /reload command");
            }
        });
    }
    
    public static SpectatorManager getSpectatorManager() {
        return spectatorManager;
    }
}
