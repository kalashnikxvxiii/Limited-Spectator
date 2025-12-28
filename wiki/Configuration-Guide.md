# Configuration Guide

## ⚠️ Configuration System

**Current Stable Version: v1.2.1**

This configuration system is available in v1.2.1 and can be customized in `config/limitedspectator-common.toml`. All settings support hot-reload via `/reload` command.

**Legacy v1.0.2**: No configuration file. Settings are hardcoded (75 blocks, level 0 permissions, doors/trapdoors/gates only).
- All other settings: Default values

See [Version Comparison](Version-Comparison) to understand the differences.

---

Limited Spectator Beta uses a comprehensive TOML configuration file that controls every aspect of the spectator mode behavior. This guide explains every setting in detail.

## Configuration File Location

| Environment | Path |
|-------------|------|
| **Dedicated Server** | `config/limitedspectator-common.toml` |
| **Single-Player/Client** | `run/config/limitedspectator-common.toml` |
| **Development** | `run/config/limitedspectator-common.toml` |

## Hot-Reloading

All configuration changes can be applied **without restarting**:

```
/reload
```

This command reloads all configurations, including Limited Spectator settings.

## Configuration Sections

### 📍 Movement Restrictions

Controls where and how far players can travel in spectator mode.

```toml
[movement]
  max_distance = 75.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true
```

#### `max_distance`
- **Type**: Double
- **Default**: `75.0`
- **Range**: `-1.0` (unlimited) or `> 0`
- **Description**: Maximum distance in blocks from the starting position

Players are teleported back when exceeding this distance. Set to `-1` to allow unlimited travel.

**Examples**:
- `50.0` - Tight restriction for small areas
- `150.0` - Moderate freedom for larger bases
- `500.0` - Wide area for exploring
- `-1.0` - No distance limit (not recommended for public servers)

#### `allow_dimension_travel`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Allow dimension changes (Nether, End, custom dimensions)

When `false`, players attempting to use portals or `/tp` to other dimensions will be blocked with a warning message.

**Security Note**: Enabling this can allow players to bypass distance limits by traveling through dimensions.

#### `teleport_back_on_exceed`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Teleport player back when exceeding distance, or just send a warning

When `true`, players are automatically teleported to their starting position. When `false`, only a warning message is shown.

#### `reset_position_on_logout`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Reset player position to starting point on logout

Prevents players from logging out at distant locations and logging back in outside the spectator area.

---

### ⚡ Player Abilities

Controls player capabilities and game mode behavior in spectator mode.

```toml
[player_abilities]
  enable_invulnerability = true
  enable_flight = true
  spectator_gamemode = "ADVENTURE"
```

#### `enable_invulnerability`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Make players invulnerable to damage (mobs, lava, fire, cacti, drowning, etc.)

Recommended to keep `true` to prevent accidental deaths while spectating.

**Note**: Fall damage is ALWAYS prevented when `mayfly=true` (Minecraft engine behavior). This cannot be disabled even with `enable_invulnerability=false`.

#### `enable_flight`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Allow flight in spectator mode

Core feature. Set to `false` if you want ground-only spectating (unusual).

**Note**: Players must double-tap spacebar to start flying (Minecraft ADVENTURE mode limitation).

#### `spectator_gamemode`
- **Type**: String
- **Default**: `"ADVENTURE"`
- **Options**: `"ADVENTURE"` or `"SPECTATOR"`
- **Description**: GameMode to use for spectator mode

**ADVENTURE** (default) - Uses Adventure mode with flight abilities. More compatible with other mods.
**SPECTATOR** - Uses vanilla spectator mode. May bypass some restrictions.

**⚠️ Warning**: Using `"SPECTATOR"` may allow players to bypass restrictions like block clipping and dimension travel.

---

### 🎮 Interaction Restrictions

Granular control over what players can interact with.

```toml
[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:iron_door",
    "minecraft:oak_trapdoor",
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
  ]
```

#### `allow_pvp`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Allow attacking other players

Set to `true` only if your server allows spectators to participate in PvP.

**Note**: Mob attacks are always blocked. Mobs don't target players with `mayfly=true` anyway (Minecraft core behavior).

**Note**: Block breaking and placing are always blocked in ADVENTURE mode at the GameMode level (cannot be overridden).

#### `allow_item_drop`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Allow dropping items from inventory

Prevents spectators from transferring items to other players.

#### `allow_item_pickup`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Allow picking up items from the ground

Prevents spectators from collecting loot or resources.

#### `allow_inventory_crafting`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Allow using inventory crafting (2x2 grid)

Prevents spectators from crafting items in their inventory.

#### `interactable_blocks`
- **Type**: String array
- **Default**: Doors, trapdoors, fence gates
- **Description**: List of block IDs that players can interact with

Players can right-click these blocks even in spectator mode. Useful for allowing door opening without full interaction privileges.

**Common additions**:
```toml
# Add buttons and levers
interactable_blocks = [
  # ... default doors/gates ...
  "minecraft:stone_button",
  "minecraft:oak_button",
  "minecraft:lever"
]
```

```toml
# Add pressure plates
interactable_blocks = [
  # ... default doors/gates ...
  "minecraft:stone_pressure_plate",
  "minecraft:heavy_weighted_pressure_plate"
]
```

**Modded blocks**: Use the full block ID, e.g., `"create:copper_valve_handle"`

---

### 🔑 Command Permissions

Controls who can use spectator commands.

```toml
[commands]
  spectator_command_permission_level = 0
  survival_command_permission_level = 0
  require_op_for_spectator = false
```

#### `spectator_command_permission_level`
- **Type**: Integer
- **Default**: `0`
- **Range**: `0-4`
- **Description**: Permission level required for `/spectator` command

Permission levels in Minecraft:
- **0** - All players
- **1** - Moderators (bypass spawn protection)
- **2** - Operators (commands like `/gamemode`, `/give`)
- **3** - Operators (commands like `/ban`, `/kick`)
- **4** - Operators (commands like `/stop`, `/op`)

**Recommended settings**:
- `0` - Open spectator mode for all players
- `2` - Restrict to operators only
- `4` - Restrict to console/high-level admins

#### `survival_command_permission_level`
- **Type**: Integer
- **Default**: `0`
- **Range**: `0-4`
- **Description**: Permission level required for `/survival` command

Usually kept at `0` to allow spectators to return to survival mode themselves.

#### `require_op_for_spectator`
- **Type**: Boolean
- **Default**: `false`
- **Description**: Require operator status for spectator commands

When `true`, only players with OP status can use spectator commands, ignoring permission levels.

**Use case**: Enable this on public servers where you don't use permission levels but want to restrict spectator access to staff.

---

### 🖥️ Client & HUD Behavior

**HUD is automatically hidden** when entering spectator mode for a cleaner viewing experience.
**F1 key** can be used to temporarily show the HUD.

This behavior is hard-coded and cannot be configured.

---

### 💬 Message Settings

Controls how messages are displayed to players.

```toml
[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

#### `use_action_bar_messages`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Show messages in action bar (above hotbar) instead of chat

Action bar messages are less intrusive than chat messages.

When `false`, all messages appear in chat.

#### `show_distance_warnings`
- **Type**: Boolean
- **Default**: `true`
- **Description**: Show warnings when approaching distance limit

Players receive warnings at 90% of `max_distance`:
- Action bar: "Warning: You are reaching the distance limit!"
- When exceeded: "You have exceeded the distance limit and were teleported back!"

Set to `false` for silent enforcement.

---

## Configuration Examples

### Example 1: Strict Server Configuration

For competitive servers where spectating should be highly restricted:

```toml
[movement]
  max_distance = 50.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[player_abilities]
  enable_invulnerability = true
  enable_flight = true
  spectator_gamemode = "ADVENTURE"

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
  interactable_blocks = []  # No interactions allowed

[commands]
  spectator_command_permission_level = 2  # Operators only
  survival_command_permission_level = 0
  require_op_for_spectator = true

[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

### Example 2: Cooperative/PvE Server

For cooperative servers where spectators can help a bit:

```toml
[movement]
  max_distance = 150.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = true  # Can collect loot
  allow_inventory_crafting = false
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:stone_button",
    "minecraft:lever"
  ]

[commands]
  spectator_command_permission_level = 0  # All players
  require_op_for_spectator = false
```

### Example 3: Creative/Building Server

For creative servers where spectators need more freedom:

```toml
[movement]
  max_distance = 500.0
  allow_dimension_travel = true  # Can visit Nether/End
  teleport_back_on_exceed = false  # Just warn
  reset_position_on_logout = false

[interactions]
  allow_pvp = false
  allow_item_drop = true
  allow_item_pickup = true
  allow_inventory_crafting = true
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:oak_button",
    "minecraft:lever",
    "minecraft:chest",
    "minecraft:barrel"
  ]

[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false
```

### Example 4: Testing/Development Server

For admins testing the server:

```toml
[movement]
  max_distance = -1.0  # Unlimited
  allow_dimension_travel = true
  teleport_back_on_exceed = false
  reset_position_on_logout = false

[interactions]
  allow_pvp = true
  allow_item_drop = true
  allow_item_pickup = true
  allow_inventory_crafting = true
  interactable_blocks = []  # All blocks interactable

[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false

[messages]
  show_distance_warnings = false  # No warnings needed
```

---

## Troubleshooting Configuration

### Config not loading changes

1. Ensure TOML syntax is correct (use a TOML validator)
2. Run `/reload` after making changes
3. Check `logs/latest.log` for config errors

### Config file keeps resetting

The config regenerates if it's corrupted or has invalid values. Always backup before editing.

### Commands not working after permission changes

Run `/reload` and rejoin the server for permission changes to take effect.

---

**Next**: Learn how to use [Commands](Commands) or explore [Features](Features) in detail.
