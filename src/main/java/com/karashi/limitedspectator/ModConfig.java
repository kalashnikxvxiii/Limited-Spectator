package com.karashi.limitedspectator;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ModConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ========== MOVEMENT RESTRICTIONS ==========
    public static final ModConfigSpec.DoubleValue MAX_DISTANCE;
    public static final ModConfigSpec.BooleanValue ALLOW_DIMENSION_TRAVEL;
    public static final ModConfigSpec.BooleanValue TELEPORT_BACK_ON_EXCEED;
    public static final ModConfigSpec.BooleanValue RESET_POSITION_ON_LOGOUT;

    // ========== PLAYER ABILITIES ==========
    public static final ModConfigSpec.BooleanValue ENABLE_INVULNERABILITY;
    public static final ModConfigSpec.BooleanValue ENABLE_FLIGHT;
    public static final ModConfigSpec.BooleanValue AUTO_START_FLYING;
    public static final ModConfigSpec.ConfigValue<String> SPECTATOR_GAMEMODE;

    // ========== INTERACTIONS ==========
    public static final ModConfigSpec.BooleanValue ALLOW_PVP;
    public static final ModConfigSpec.BooleanValue ALLOW_MOB_ATTACKS;
    public static final ModConfigSpec.BooleanValue ALLOW_ITEM_DROP;
    public static final ModConfigSpec.BooleanValue ALLOW_ITEM_PICKUP;
    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_BREAKING;
    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_PLACING;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> INTERACTABLE_BLOCKS;

    // ========== PERMISSIONS ==========
    public static final ModConfigSpec.IntValue SPECTATOR_COMMAND_PERMISSION_LEVEL;
    public static final ModConfigSpec.IntValue SURVIVAL_COMMAND_PERMISSION_LEVEL;
    public static final ModConfigSpec.BooleanValue REQUIRE_OP_FOR_SPECTATOR;

    // ========== CLIENT/HUD SETTINGS ==========
    public static final ModConfigSpec.BooleanValue AUTO_HIDE_HUD;
    public static final ModConfigSpec.BooleanValue ALLOW_F1_HUD_TOGGLE;

    // ========== MESSAGES ==========
    public static final ModConfigSpec.BooleanValue USE_ACTION_BAR_MESSAGES;
    public static final ModConfigSpec.BooleanValue SHOW_DISTANCE_WARNINGS;

    static {
        BUILDER.comment("==============================================")
                .comment("  Limited Spectator Configuration")
                .comment("  Configure all aspects of the spectator mode")
                .comment("==============================================")
                .push("movement_restrictions");

        MAX_DISTANCE = BUILDER
                .comment("Maximum distance (in blocks) a player can travel from their starting position")
                .comment("Set to -1 to disable distance limit")
                .defineInRange("max_distance", 75.0, -1.0, 10000.0);

        ALLOW_DIMENSION_TRAVEL = BUILDER
                .comment("Allow players to travel to different dimensions (Nether, End, etc.) while in spectator mode")
                .define("allow_dimension_travel", false);

        TELEPORT_BACK_ON_EXCEED = BUILDER
                .comment("Teleport player back to start position when exceeding max distance")
                .comment("If false, player will be stopped at the boundary")
                .define("teleport_back_on_exceed", true);

        RESET_POSITION_ON_LOGOUT = BUILDER
                .comment("Reset player position to spectator start point when they log out")
                .comment("Prevents position abuse by logging out and back in")
                .define("reset_position_on_logout", true);

        BUILDER.pop();

        // ========== PLAYER ABILITIES ==========
        BUILDER.comment("==============================================")
                .comment("  Player Abilities in Spectator Mode")
                .comment("==============================================")
                .push("player_abilities");

        ENABLE_INVULNERABILITY = BUILDER
                .comment("Make players invulnerable while in spectator mode")
                .define("enable_invulnerability", true);

        ENABLE_FLIGHT = BUILDER
                .comment("Allow players to fly in spectator mode")
                .define("enable_flight", true);

        AUTO_START_FLYING = BUILDER
                .comment("Automatically start flying when entering spectator mode")
                .comment("Requires 'enable_flight' to be true")
                .define("auto_start_flying", true);

        SPECTATOR_GAMEMODE = BUILDER
                .comment("GameMode to use for spectator mode")
                .comment("Options: ADVENTURE, SPECTATOR")
                .comment("ADVENTURE is recommended for servers (allows collisions, prevents phasing through blocks)")
                .define("spectator_gamemode", "ADVENTURE");

        BUILDER.pop();

        // ========== INTERACTIONS ==========
        BUILDER.comment("==============================================")
                .comment("  Interaction Restrictions")
                .comment("==============================================")
                .push("interactions");

        ALLOW_PVP = BUILDER
                .comment("Allow players to attack other players while in spectator mode")
                .define("allow_pvp", false);

        ALLOW_MOB_ATTACKS = BUILDER
                .comment("Allow players to attack mobs while in spectator mode")
                .define("allow_mob_attacks", false);

        ALLOW_ITEM_DROP = BUILDER
                .comment("Allow players to drop items while in spectator mode")
                .define("allow_item_drop", false);

        ALLOW_ITEM_PICKUP = BUILDER
                .comment("Allow players to pick up items while in spectator mode")
                .define("allow_item_pickup", false);

        ALLOW_BLOCK_BREAKING = BUILDER
                .comment("Allow players to break blocks while in spectator mode")
                .define("allow_block_breaking", false);

        ALLOW_BLOCK_PLACING = BUILDER
                .comment("Allow players to place blocks while in spectator mode")
                .define("allow_block_placing", false);

        INTERACTABLE_BLOCKS = BUILDER
                .comment("List of blocks that players CAN interact with in spectator mode")
                .comment("Use Minecraft block IDs (e.g., 'minecraft:oak_door', 'minecraft:lever')")
                .comment("Default allows doors, trapdoors, and fence gates")
                .defineList("interactable_blocks",
                        List.of(
                                "minecraft:oak_door",
                                "minecraft:spruce_door",
                                "minecraft:birch_door",
                                "minecraft:jungle_door",
                                "minecraft:acacia_door",
                                "minecraft:dark_oak_door",
                                "minecraft:mangrove_door",
                                "minecraft:cherry_door",
                                "minecraft:bamboo_door",
                                "minecraft:crimson_door",
                                "minecraft:warped_door",
                                "minecraft:iron_door",
                                "minecraft:oak_trapdoor",
                                "minecraft:spruce_trapdoor",
                                "minecraft:birch_trapdoor",
                                "minecraft:jungle_trapdoor",
                                "minecraft:acacia_trapdoor",
                                "minecraft:dark_oak_trapdoor",
                                "minecraft:mangrove_trapdoor",
                                "minecraft:cherry_trapdoor",
                                "minecraft:bamboo_trapdoor",
                                "minecraft:crimson_trapdoor",
                                "minecraft:warped_trapdoor",
                                "minecraft:iron_trapdoor",
                                "minecraft:oak_fence_gate",
                                "minecraft:spruce_fence_gate",
                                "minecraft:birch_fence_gate",
                                "minecraft:jungle_fence_gate",
                                "minecraft:acacia_fence_gate",
                                "minecraft:dark_oak_fence_gate",
                                "minecraft:mangrove_fence_gate",
                                "minecraft:cherry_fence_gate",
                                "minecraft:bamboo_fence_gate",
                                "minecraft:crimson_fence_gate",
                                "minecraft:warped_fence_gate"
                        ),
                        obj -> obj instanceof String);

        BUILDER.pop();

        // ========== PERMISSIONS ==========
        BUILDER.comment("==============================================")
                .comment("  Command Permissions")
                .comment("==============================================")
                .push("permissions");

        SPECTATOR_COMMAND_PERMISSION_LEVEL = BUILDER
                .comment("Permission level required to use /spectator command")
                .comment("0 = everyone, 1 = moderators, 2 = game masters, 3 = admins, 4 = owners")
                .defineInRange("spectator_command_permission_level", 0, 0, 4);

        SURVIVAL_COMMAND_PERMISSION_LEVEL = BUILDER
                .comment("Permission level required to use /survival command")
                .comment("0 = everyone, 1 = moderators, 2 = game masters, 3 = admins, 4 = owners")
                .defineInRange("survival_command_permission_level", 0, 0, 4);

        REQUIRE_OP_FOR_SPECTATOR = BUILDER
                .comment("Require OP status to use spectator commands (overrides permission level)")
                .define("require_op_for_spectator", false);

        BUILDER.pop();

        // ========== CLIENT/HUD SETTINGS ==========
        BUILDER.comment("==============================================")
                .comment("  Client & HUD Settings")
                .comment("==============================================")
                .push("client_hud");

        AUTO_HIDE_HUD = BUILDER
                .comment("Automatically hide the HUD when entering spectator mode")
                .define("auto_hide_hud", true);

        ALLOW_F1_HUD_TOGGLE = BUILDER
                .comment("Allow players to temporarily show HUD with F1 key")
                .define("allow_f1_hud_toggle", true);

        BUILDER.pop();

        // ========== MESSAGES ==========
        BUILDER.comment("==============================================")
                .comment("  Message Settings")
                .comment("==============================================")
                .push("messages");

        USE_ACTION_BAR_MESSAGES = BUILDER
                .comment("Display feedback messages in the action bar instead of chat")
                .define("use_action_bar_messages", true);

        SHOW_DISTANCE_WARNINGS = BUILDER
                .comment("Show warnings when player is approaching distance limit")
                .define("show_distance_warnings", true);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    // Cached values for performance (avoid constant config lookups)
    public static double maxDistance;
    public static boolean allowDimensionTravel;
    public static boolean teleportBackOnExceed;
    public static boolean resetPositionOnLogout;
    public static boolean enableInvulnerability;
    public static boolean enableFlight;
    public static boolean autoStartFlying;
    public static String spectatorGamemode;
    public static boolean allowPvp;
    public static boolean allowMobAttacks;
    public static boolean allowItemDrop;
    public static boolean allowItemPickup;
    public static boolean allowBlockBreaking;
    public static boolean allowBlockPlacing;
    public static List<? extends String> interactableBlocks;
    public static int spectatorCommandPermissionLevel;
    public static int survivalCommandPermissionLevel;
    public static boolean requireOpForSpectator;
    public static boolean autoHideHud;
    public static boolean allowF1HudToggle;
    public static boolean useActionBarMessages;
    public static boolean showDistanceWarnings;

    public static void onLoad(final ModConfigEvent event) {
        // Cache all config values when config loads or reloads
        maxDistance = MAX_DISTANCE.get();
        allowDimensionTravel = ALLOW_DIMENSION_TRAVEL.get();
        teleportBackOnExceed = TELEPORT_BACK_ON_EXCEED.get();
        resetPositionOnLogout = RESET_POSITION_ON_LOGOUT.get();
        enableInvulnerability = ENABLE_INVULNERABILITY.get();
        enableFlight = ENABLE_FLIGHT.get();
        autoStartFlying = AUTO_START_FLYING.get();
        spectatorGamemode = SPECTATOR_GAMEMODE.get();
        allowPvp = ALLOW_PVP.get();
        allowMobAttacks = ALLOW_MOB_ATTACKS.get();
        allowItemDrop = ALLOW_ITEM_DROP.get();
        allowItemPickup = ALLOW_ITEM_PICKUP.get();
        allowBlockBreaking = ALLOW_BLOCK_BREAKING.get();
        allowBlockPlacing = ALLOW_BLOCK_PLACING.get();
        interactableBlocks = INTERACTABLE_BLOCKS.get();
        spectatorCommandPermissionLevel = SPECTATOR_COMMAND_PERMISSION_LEVEL.get();
        survivalCommandPermissionLevel = SURVIVAL_COMMAND_PERMISSION_LEVEL.get();
        requireOpForSpectator = REQUIRE_OP_FOR_SPECTATOR.get();
        autoHideHud = AUTO_HIDE_HUD.get();
        allowF1HudToggle = ALLOW_F1_HUD_TOGGLE.get();
        useActionBarMessages = USE_ACTION_BAR_MESSAGES.get();
        showDistanceWarnings = SHOW_DISTANCE_WARNINGS.get();
    }
}
