package com.karashi.limitedspectator;

// Import Java standard
import java.util.HashMap;
import java.util.UUID;

import com.karashi.limitedspectator.network.NetworkHandler;

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

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

@Mod("limitedspectator")
public class SpectatorMod {

    private static final double SPECTATOR_MAX_DISTANCE = 75.0; // block limit
    private static final HashMap<UUID, Vec3> spectatorStartPositions = new HashMap<>();
    private static final HashMap<UUID, Boolean> inSpectatorMode = new HashMap<>();
    public static final String MODID = "limitedspectator";

    public SpectatorMod() {
        System.out.println("[LimitedSpectator] Mod initialized successfully.");
        NeoForge.EVENT_BUS.register(this);
    }

    @EventBusSubscriber(modid = SpectatorMod.MODID, bus = EventBusSubscriber.Bus.MOD)
    public class ModEvents {
        @SubscribeEvent
        public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
            NetworkHandler.register(event);
        }
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        // Command: /spectator
        dispatcher.register(Commands.literal("spectator")
            .requires(source -> source.hasPermission(0)) // everyone
            .executes(ctx -> {
                try {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                    if (player == null) {
                        ctx.getSource().sendFailure(Component.literal("§cThis command can only be executed by players."));
                        return 0;
                    }

                    // Debug or control — needed to understand who and where
                    System.out.println("[LimitedSpectator] Running /spectator command for " + player.getName().getString() 
                        + " at these coordinates [" 
                        + String.format("%.1f, %.1f, %.1f", player.getX(), player.getY(), player.getZ()) 
                        + "]");

                    // Limited spectator mode
                    player.setGameMode(GameType.ADVENTURE);
                    player.getAbilities().mayfly = true;
                    player.getAbilities().flying = true;
                    player.getAbilities().invulnerable = true;
                    player.onUpdateAbilities();

                    spectatorStartPositions.put(player.getUUID(), player.position());
                    inSpectatorMode.put(player.getUUID(), true);

                    // Hide HUD (added control)
                    if (player.connection != null && player.server != null) {
                        NetworkHandler.sendHudState(player, true);
                    } else {
                        System.err.println("[LimitedSpectator] ERROR: Null connection or server for " + player.getName().getString());
                        ctx.getSource().sendFailure(Component.literal("§cUnable to send packet to client at this time."));
                    }

                    ctx.getSource().sendSuccess(() ->
                        Component.literal("§bSPECTATOR §7mode (with collisions) §7activated. Maximum radius: " + SPECTATOR_MAX_DISTANCE + " blocks."), true);

                    return 1;
                } catch (Exception e) {
                    e.printStackTrace(); // force full log in console
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
                    if (player == null) {
                        ctx.getSource().sendFailure(Component.literal("§cThis command can only be executed by one player."));
                        return 0;
                    }

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
                    if (player.connection != null && player.server != null) {
                        NetworkHandler.sendHudState(player, false); // false = show HUD
                    } else {
                        System.err.println("[LimitedSpectator] ERROR: Null connection or server for " + player.getName().getString());
                        ctx.getSource().sendFailure(Component.literal("§cUnable to send packet to client at this time."));
                    }

                    player.displayClientMessage(Component.literal("§aSURVIVAL mode activated."), true);
                    return 1;

                } catch (Exception e) {
                    e.printStackTrace(); // print full error in console
                    ctx.getSource().sendFailure(Component.literal("§cIInternal error: " + e.getMessage()));
                    return 0;
                }
            })
        );
    }

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
    @SubscribeEvent
    public void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        player.getAbilities().flying = player.isSpectator();
        player.onUpdateAbilities();

        // FOR DEBUGGING
        // System.out.println("[LimitedSpectator] Game mode change detected for " + player.getName().getString() +
        //    ": " + player.gameMode.getGameModeForPlayer());
    }

    // Block right click on blocks
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Block right click in the air (use items)
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Block left click (attack or break blocks)
    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player && player.isSpectator()) {
            event.setCanceled(true);
        }
    }

    // Returns to survival when you log out
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
            System.out.println("[LimitedSpectator] " + player.getName().getString() + " left the server. Spectator mode automatically disabled.");
        }
    }

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
        // System.out.println("[LimitedSpectator] Player " + player.getName().getString() + " attempted to change dimension while in limited spectator mode.");
    }

    // Handle interactions in limited spectator mode
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
        if (state == null) return;

        Block block = state.getBlock();

        // Allow doors, trapdoors and fence gates
        if (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) {
            return;
        }

        // Everything else blocked
        event.setCanceled(true);

        // FOR DEBUGGING
        // System.out.println("[LimitedSpectator] Interaction blocked by " + player.getName().getString() +
        // " with block " + event.getLevel().getBlockState(event.getPos()).getBlock().getName().getString());
    }

    // 🔹 Disable PvP and attacks in limited spectator mode
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
            // serverPlayer.displayClientMessage(Component.literal("§cNon puoi attaccare in modalità spettatore!"), true);
        }

        // FOR DEBUG AND CONTROL
        // System.out.println("[LimitedSpectator] Attack attempted by " + player.getName().getString() +
        // " in limited spectator mode on " + event.getTarget().getName().getString());
    }
}
