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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.Commands;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.Mod;

@Mod("limitedspectator")
public class SpectatorMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpectatorMod.class);
    private static final double SPECTATOR_MAX_DISTANCE = 75.0; // block limit
    private static final HashMap<UUID, Vec3> spectatorStartPositions = new HashMap<>();
    private static final HashMap<UUID, Boolean> inSpectatorMode = new HashMap<>();
    public static final String MODID = "limitedspectator";

    public SpectatorMod(IEventBus modBus) {
        LOGGER.info("Mod initialized successfully.");
        NeoForge.EVENT_BUS.register(this);

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
            .requires(source -> source.hasPermission(0)) // everyone
            .executes(ctx -> {
                try {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    // Debug or control — needed to understand who and where
                    LOGGER.info("Running /spectator command for {} at coordinates [{}, {}, {}]",
                        player.getName().getString(),
                        String.format("%.1f", player.getX()),
                        String.format("%.1f", player.getY()),
                        String.format("%.1f", player.getZ()));

                    // Limited spectator mode
                    player.setGameMode(GameType.ADVENTURE);
                    player.getAbilities().mayfly = true;
                    player.getAbilities().flying = true;
                    player.getAbilities().invulnerable = true;
                    player.onUpdateAbilities();

                    spectatorStartPositions.put(player.getUUID(), player.position());
                    inSpectatorMode.put(player.getUUID(), true);

                    // Hide HUD
                    NetworkHandler.sendHudState(player, true);

                    ctx.getSource().sendSuccess(() ->
                        Component.literal("§bSPECTATOR §7mode (with collisions) §7activated. Maximum radius: " + SPECTATOR_MAX_DISTANCE + " blocks."), true);

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
            .requires(source -> source.hasPermission(0)) // everyone
            .executes(ctx -> {
                try {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    // Retrieve saved location if it exists
                    Vec3 startPos = spectatorStartPositions.remove(player.getUUID());
                    if (startPos != null) {
                        player.teleportTo(startPos.x, startPos.y, startPos.z);
                    }

                    // Force survival mode
                    player.setGameMode(GameType.SURVIVAL);

                    // Disable flight and spectator abilities
                    player.getAbilities().flying = false;
                    player.getAbilities().mayfly = false;
                    player.onUpdateAbilities();

                    // Show HUD again
                    NetworkHandler.sendHudState(player, false); // false = show HUD

                    player.displayClientMessage(Component.literal("§aSURVIVAL mode activated."), true);
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
        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
                    Vec3 start = spectatorStartPositions.get(player.getUUID());
                    if (start != null) {
                        double distance = player.position().distanceTo(start);
                        if (distance > SPECTATOR_MAX_DISTANCE) {
                            // Teleport the player back and automatically sync
                            player.teleportTo(start.x, start.y, start.z);
                            player.displayClientMessage(Component.literal("§cYou have exceeded the movement limit!"), true);
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

    // Block right click on blocks
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Block right click in the air (use items)
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Block left click (attack or break blocks)
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Returns to survival when you log out
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerLogout(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // If the player was in "limited" spectator mode
        if (spectatorStartPositions.containsKey(player.getUUID())) {
            // Restore saved position (if still stored)
            Vec3 startPos = spectatorStartPositions.remove(player.getUUID());
            if (startPos != null) {
                player.teleportTo(startPos.x, startPos.y, startPos.z);
            }

            // Force survival mode
            player.setGameMode(GameType.SURVIVAL);

            // Deactivate flight
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();

            // FOR DEBUGGING AND CONTROL
            LOGGER.info("{} left the server. Spectator mode automatically disabled.", player.getName().getString());
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerChangeDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if the player is in "fake spectator" mode
        boolean isLimitedSpectator = 
            player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE &&
            player.getAbilities().mayfly &&
            !player.isCreative();

        // If yes, block the size change
        if (isLimitedSpectator) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("§cYou can't change dimensions in spectator mode!"), true);
        }

        // FOR DEBUGGING
        // LOGGER.debug("Player {} attempted to change dimension while in limited spectator mode", player.getName().getString());
    }

    // Handle interactions in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if the player is in "limited spectator" mode
        boolean isLimitedSpectator =
                player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE &&
                player.getAbilities().mayfly &&
                !player.isCreative();

        if (!isLimitedSpectator) return;

        // Get clicked block
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();

        // Allow doors, trapdoors and fence gates
        if (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) {
            return;
        }

        // Everything else blocked
        event.setCanceled(true);

        // FOR DEBUGGING
        // LOGGER.debug("Interaction blocked by {} with block {}", player.getName().getString(),
        //    event.getLevel().getBlockState(event.getPos()).getBlock().getName().getString());
    }

    // Disable PvP and attacks in limited spectator mode
    @SuppressWarnings({"unused", "deprecation"})
    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if player is in limited spectator mode
        boolean isLimitedSpectator =
                serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE &&
                serverPlayer.getAbilities().mayfly &&
                !serverPlayer.isCreative();

        if (isLimitedSpectator) {
            // Completely block attack
            event.setCanceled(true);

            // Optional: feedback message for the player (kept in Italian)
            // serverPlayer.displayClientMessage(Component.literal("§cYou cannot attack in spectator mode!"), true);
        }

        // FOR DEBUG AND CONTROL
        // LOGGER.debug("Attack attempted by {} in limited spectator mode on {}", player.getName().getString(),
        //    event.getTarget().getName().getString());
    }
}
