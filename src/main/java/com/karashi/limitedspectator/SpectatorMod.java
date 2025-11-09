package com.karashi.limitedspectator;

// Import Java standard
import java.util.HashMap;
import java.util.UUID;

import com.karashi.limitedspectator.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod("limitedspectator")
public class SpectatorMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpectatorMod.class);
    private static final HashMap<UUID, Vec3> spectatorStartPositions = new HashMap<>();
    private static final HashMap<UUID, ResourceKey<Level>> spectatorStartDimensions = new HashMap<>();
    private static final HashMap<UUID, Boolean> inSpectatorMode = new HashMap<>();
    public static final String MODID = "limitedspectator";

    public SpectatorMod(IEventBus modBus, ModContainer modContainer) {
        LOGGER.info("Mod initialized successfully.");
        NeoForge.EVENT_BUS.register(this);

        // Register configuration
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, com.karashi.limitedspectator.ModConfig.SPEC);
        LOGGER.info("Configuration registered: limitedspectator-common.toml");

        // Register config event listener
        modBus.addListener(com.karashi.limitedspectator.ModConfig::onLoad);

        // Register MOD bus events (non-deprecated way for NeoForge 1.21.1+)
        modBus.addListener(this::onRegisterPayloads);
    }

    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        NetworkHandler.register(event);
    }

    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        // Command: /spectator
        dispatcher.register(Commands.literal("spectator")
            .requires(source -> {
                if (com.karashi.limitedspectator.ModConfig.requireOpForSpectator) {
                    return source.hasPermission(2); // OP level
                }
                return source.hasPermission(com.karashi.limitedspectator.ModConfig.spectatorCommandPermissionLevel);
            })
            .executes(ctx -> {
                try {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    // Debug or control — needed to understand who and where
                    LOGGER.info("Running /spectator command for {} at coordinates [{}, {}, {}]",
                        player.getName().getString(),
                        String.format("%.1f", player.getX()),
                        String.format("%.1f", player.getY()),
                        String.format("%.1f", player.getZ()));

                    // Set gamemode based on config
                    GameType targetMode = com.karashi.limitedspectator.ModConfig.spectatorGamemode.equalsIgnoreCase("SPECTATOR")
                        ? GameType.SPECTATOR
                        : GameType.ADVENTURE;
                    player.setGameMode(targetMode);

                    // Set abilities based on config
                    if (com.karashi.limitedspectator.ModConfig.enableFlight) {
                        player.getAbilities().mayfly = true;
                        if (com.karashi.limitedspectator.ModConfig.autoStartFlying) {
                            player.getAbilities().flying = true;
                        }
                    }
                    player.getAbilities().invulnerable = com.karashi.limitedspectator.ModConfig.enableInvulnerability;

                    // Enable building if block breaking or placing is allowed
                    // ADVENTURE mode sets mayBuild=false by default, which blocks all building
                    if (targetMode == GameType.ADVENTURE) {
                        player.getAbilities().mayBuild = com.karashi.limitedspectator.ModConfig.allowBlockBreaking
                            || com.karashi.limitedspectator.ModConfig.allowBlockPlacing;
                    }

                    player.onUpdateAbilities();

                    // Force sync abilities packet to client to ensure flying state is applied immediately
                    if (com.karashi.limitedspectator.ModConfig.autoStartFlying && com.karashi.limitedspectator.ModConfig.enableFlight) {
                        // Send abilities update packet to ensure client sees flying=true
                        player.connection.send(new net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket(player.getAbilities()));
                    }

                    // Save start position and dimension
                    spectatorStartPositions.put(player.getUUID(), player.position());
                    spectatorStartDimensions.put(player.getUUID(), player.level().dimension());
                    inSpectatorMode.put(player.getUUID(), true);

                    // Hide HUD if configured
                    if (com.karashi.limitedspectator.ModConfig.autoHideHud) {
                        NetworkHandler.sendHudState(player, true);
                    }

                    // Build and send message
                    String distanceInfo = com.karashi.limitedspectator.ModConfig.maxDistance > 0
                        ? String.format("Maximum radius: %.0f blocks.", com.karashi.limitedspectator.ModConfig.maxDistance)
                        : "No distance limit.";

                    Component message = Component.literal("SPECTATOR ")
                        .withStyle(ChatFormatting.AQUA)
                        .append(Component.literal("mode activated. " + distanceInfo).withStyle(ChatFormatting.GRAY));

                    player.displayClientMessage(message, com.karashi.limitedspectator.ModConfig.useActionBarMessages);

                    return 1;
                } catch (Exception e) {
                    LOGGER.error("Error executing /spectator command", e);
                    ctx.getSource().sendFailure(Component.literal("§cInternal error: " + e));
                    return 0;
                }
            })
        );

        // Command: /survival
        dispatcher.register(Commands.literal("survival")
            .requires(source -> {
                if (com.karashi.limitedspectator.ModConfig.requireOpForSpectator) {
                    return source.hasPermission(2); // OP level
                }
                return source.hasPermission(com.karashi.limitedspectator.ModConfig.survivalCommandPermissionLevel);
            })
            .executes(ctx -> {
                try {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    // Retrieve saved location and dimension
                    Vec3 startPos = spectatorStartPositions.remove(player.getUUID());
                    ResourceKey<Level> startDimension = spectatorStartDimensions.remove(player.getUUID());

                    if (startPos != null && startDimension != null) {
                        // Check if player is in a different dimension
                        if (!player.level().dimension().equals(startDimension)) {
                            // Get the target dimension
                            ServerLevel targetLevel = player.server.getLevel(startDimension);
                            if (targetLevel != null) {
                                // Teleport to the correct dimension and position
                                player.teleportTo(targetLevel, startPos.x, startPos.y, startPos.z, player.getYRot(), player.getXRot());
                            } else {
                                // Fallback: just teleport to position in current dimension
                                player.teleportTo(startPos.x, startPos.y, startPos.z);
                            }
                        } else {
                            // Same dimension, just teleport to position
                            player.teleportTo(startPos.x, startPos.y, startPos.z);
                        }
                    }

                    // Remove from spectator tracking
                    inSpectatorMode.remove(player.getUUID());

                    // Force survival mode
                    player.setGameMode(GameType.SURVIVAL);

                    // Disable flight and spectator abilities
                    player.getAbilities().flying = false;
                    player.getAbilities().mayfly = false;
                    player.getAbilities().invulnerable = false;
                    player.onUpdateAbilities();

                    // Show HUD again
                    NetworkHandler.sendHudState(player, false); // false = show HUD

                    Component message = Component.literal("SURVIVAL mode activated.")
                        .withStyle(ChatFormatting.GREEN);

                    player.displayClientMessage(message, com.karashi.limitedspectator.ModConfig.useActionBarMessages);
                    return 1;

                } catch (Exception e) {
                    LOGGER.error("Error executing /survival command", e);
                    ctx.getSource().sendFailure(Component.literal("§cInternal error: " + e.getMessage()));
                    return 0;
                }
            })
        );
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        // Skip if distance limit is disabled
        if (com.karashi.limitedspectator.ModConfig.maxDistance < 0) return;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
                    Vec3 start = spectatorStartPositions.get(player.getUUID());
                    if (start != null) {
                        double distance = player.position().distanceTo(start);
                        if (distance > com.karashi.limitedspectator.ModConfig.maxDistance) {
                            if (com.karashi.limitedspectator.ModConfig.teleportBackOnExceed) {
                                // Teleport the player back to start position
                                player.teleportTo(start.x, start.y, start.z);
                                player.displayClientMessage(
                                    Component.literal("You have exceeded the movement limit!").withStyle(ChatFormatting.RED),
                                    com.karashi.limitedspectator.ModConfig.useActionBarMessages
                                );
                            } else {
                                // Stop the player at the boundary
                                Vec3 currentPos = player.position();
                                Vec3 direction = currentPos.subtract(start).normalize();
                                Vec3 boundaryPos = start.add(direction.scale(com.karashi.limitedspectator.ModConfig.maxDistance));
                                player.teleportTo(boundaryPos.x, boundaryPos.y, boundaryPos.z);

                                if (com.karashi.limitedspectator.ModConfig.showDistanceWarnings) {
                                    player.displayClientMessage(
                                        Component.literal("You have reached the distance limit!").withStyle(ChatFormatting.RED),
                                        com.karashi.limitedspectator.ModConfig.useActionBarMessages
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // When changing game mode → update skills
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        player.getAbilities().flying = player.isSpectator();
        player.onUpdateAbilities();

        // FOR DEBUGGING
        // LOGGER.debug("Game mode change detected for {}: {}", player.getName().getString(),
        //    player.gameMode.getGameModeForPlayer());
    }

    // Block left click (break blocks) - handled with config
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if player is in limited spectator mode
        if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
            // Check config - if block breaking is not allowed, cancel event
            if (!com.karashi.limitedspectator.ModConfig.allowBlockBreaking) {
                event.setCanceled(true);
            }
        }
    }

    // Block placing in spectator mode
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if player is in limited spectator mode
        if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
            // Check config - if block placing is not allowed, cancel event
            if (!com.karashi.limitedspectator.ModConfig.allowBlockPlacing) {
                event.setCanceled(true);
            }
        }
    }

    // Block breaking in spectator mode
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        // Check if player is in limited spectator mode
        if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
            // Check config - if block breaking is not allowed, cancel event
            if (!com.karashi.limitedspectator.ModConfig.allowBlockBreaking) {
                event.setCanceled(true);
            }
        }
    }

    // Returns to survival when you log out
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerLogout(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // If the player was in "limited" spectator mode
        if (spectatorStartPositions.containsKey(player.getUUID())) {
            // Restore saved position if configured
            Vec3 startPos = spectatorStartPositions.remove(player.getUUID());
            spectatorStartDimensions.remove(player.getUUID()); // Clean up dimension tracking

            if (startPos != null && com.karashi.limitedspectator.ModConfig.resetPositionOnLogout) {
                player.teleportTo(startPos.x, startPos.y, startPos.z);
            }

            // Remove from spectator tracking
            inSpectatorMode.remove(player.getUUID());

            // Force survival mode
            player.setGameMode(GameType.SURVIVAL);

            // Deactivate flight and invulnerability
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.getAbilities().invulnerable = false;
            player.onUpdateAbilities();

            // FOR DEBUGGING AND CONTROL
            LOGGER.info("{} left the server. Spectator mode automatically disabled.", player.getName().getString());
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerChangeDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Skip if dimension travel is allowed in config
        if (com.karashi.limitedspectator.ModConfig.allowDimensionTravel) return;

        // Check if the player is in limited spectator mode
        if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
            event.setCanceled(true);
            player.displayClientMessage(
                Component.literal("You can't change dimensions in spectator mode!").withStyle(ChatFormatting.RED),
                com.karashi.limitedspectator.ModConfig.useActionBarMessages
            );
        }

        // FOR DEBUGGING
        // LOGGER.debug("Player {} attempted to change dimension while in limited spectator mode", player.getName().getString());
    }

    // Handle interactions in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if the player is in limited spectator mode using tracking map
        if (!inSpectatorMode.getOrDefault(player.getUUID(), false)) return;

        // Get clicked block
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();

        // Get block ID for comparison with config list
        String blockId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).toString();

        // Check if block is in the interactable list
        if (com.karashi.limitedspectator.ModConfig.interactableBlocks.contains(blockId)) {
            return; // Allow interaction
        }

        // Everything else blocked
        event.setCanceled(true);

        // FOR DEBUGGING
        // LOGGER.debug("Interaction blocked by {} with block {}", player.getName().getString(), blockId);
    }

    // Disable PvP and attacks in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if player is in limited spectator mode using tracking map
        if (!inSpectatorMode.getOrDefault(serverPlayer.getUUID(), false)) return;

        // Check if target is a player (PvP) or mob
        boolean isTargetPlayer = event.getTarget() instanceof Player;

        if (isTargetPlayer) {
            // PvP attack - check config
            if (!com.karashi.limitedspectator.ModConfig.allowPvp) {
                event.setCanceled(true);
                // Optional: feedback message
                // serverPlayer.displayClientMessage(Component.literal("§cYou cannot attack players in spectator mode!"), true);
            }
        } else {
            // Mob attack - check config
            if (!com.karashi.limitedspectator.ModConfig.allowMobAttacks) {
                event.setCanceled(true);
                // Optional: feedback message
                // serverPlayer.displayClientMessage(Component.literal("§cYou cannot attack mobs in spectator mode!"), true);
            }
        }

        // FOR DEBUG AND CONTROL
        // LOGGER.debug("Attack attempted by {} in limited spectator mode on {}", player.getName().getString(),
        //    event.getTarget().getName().getString());
    }

    // Block item dropping in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if player is in limited spectator mode using tracking map
        if (!inSpectatorMode.getOrDefault(serverPlayer.getUUID(), false)) return;

        // Check config
        if (!com.karashi.limitedspectator.ModConfig.allowItemDrop) {
            // Cancel the event to prevent item from entering the world
            event.setCanceled(true);

            // Return the item to the player's inventory
            // (The item was already removed from inventory before this event fires)
            serverPlayer.addItem(event.getEntity().getItem().copy());

            // Optional: feedback message for the player
            // serverPlayer.displayClientMessage(Component.literal("§cYou cannot drop items in spectator mode!"), true);
        }

        // FOR DEBUG AND CONTROL
        // LOGGER.debug("Item drop attempted by {} in limited spectator mode", player.getName().getString());
    }

    // Block item pickup in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent.Pre event) {
        Player player = event.getPlayer();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if player is in limited spectator mode using tracking map
        if (!inSpectatorMode.getOrDefault(serverPlayer.getUUID(), false)) return;

        // Check config
        if (!com.karashi.limitedspectator.ModConfig.allowItemPickup) {
            // Completely block item pickup
            event.setCanPickup(TriState.FALSE);

            // Optional: feedback message for the player
            // serverPlayer.displayClientMessage(Component.literal("§cYou cannot pick up items in spectator mode!"), true);
        }

        // FOR DEBUG AND CONTROL
        // LOGGER.debug("Item pickup attempted by {} in limited spectator mode", player.getName().getString());
    }

    // Handle damage when invulnerability is disabled
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLivingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if player is in limited spectator mode
        if (!inSpectatorMode.getOrDefault(player.getUUID(), false)) return;

        // If invulnerability is enabled in config, keep the default behavior (no damage)
        // If invulnerability is disabled, we need to allow damage through
        if (!com.karashi.limitedspectator.ModConfig.enableInvulnerability) {
            // Remove invulnerability flag temporarily to allow damage
            // The abilities.invulnerable flag prevents damage, but we want to allow it
            // Note: abilities.invulnerable is already set to false in /spectator command when config is false
            // This handler ensures damage is processed correctly
            return; // Allow the damage event to proceed
        }
        // If invulnerability is enabled, the abilities.invulnerable=true will handle blocking damage
    }
}
