# Configuration Guide

## 📍 Configuration File Location

The configuration file is **automatically generated** on first launch:

**Path**: `config/limitedspectator-common.toml`

**Note**: Configuration is **NeoForge only**. Fabric and Quilt use hardcoded defaults.

## 🔄 Applying Changes

After editing the config file:

1. **Save the file**
2. **In-game**: Run `/reload` command
3. Changes apply immediately (no restart needed)

## 📋 Configuration Sections

### 🚶 Movement Restrictions

Controls how far players can travel in spectator mode.

```toml
[movement_restrictions]
  # Maximum distance from start position (-1 = unlimited)
  max_distance = 75.0
  
  # Allow traveling to Nether, End, etc.
  allow_dimension_travel = false
  
  # Teleport back when exceeding distance (true) or just warn (false)
  teleport_back_on_exceed = true
  
  # Reset position when player logs out
  reset_position_on_logout = true
```

**Examples**:
- `max_distance = -1` → No distance limit
- `max_distance = 50.0` → 50 block radius
- `allow_dimension_travel = true` → Can go to Nether/End

### 🎮 Player Abilities

Controls what players can do in spectator mode.

```toml
[player_abilities]
  # Make players invulnerable (mobs, lava, fire, etc.)
  # Note: Fall damage always prevented by Minecraft engine
  enable_invulnerability = true
  
  # Allow flight (double-press spacebar to activate)
  enable_flight = true
  
  # Game mode: "ADVENTURE" (recommended) or "SPECTATOR" (enables noclip)
  spectator_gamemode = "ADVENTURE"
```

**Important**: 
- `spectator_gamemode = "SPECTATOR"` enables noclip (reduces server security)
- `spectator_gamemode = "ADVENTURE"` prevents noclip (recommended for servers)

### 🔧 Interaction Restrictions

Controls what players can interact with.

```toml
[interactions]
  # Allow attacking other players
  allow_pvp = false
  
  # Allow dropping items
  allow_item_drop = false
  
  # Allow picking up items
  allow_item_pickup = false
  
  # Allow inventory crafting (2x2 grid)
  allow_inventory_crafting = false
  
  # Block IDs players can interact with
  # Default: doors, trapdoors, fence gates
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:spruce_door",
    "minecraft:birch_door",
    "minecraft:jungle_door",
    "minecraft:acacia_door",
    "minecraft:dark_oak_door",
    "minecraft:mangrove_door",
    "minecraft:cherry_door",
    "minecraft:pale_oak_door",
    "minecraft:oak_trapdoor",
    "minecraft:spruce_trapdoor",
    "minecraft:birch_trapdoor",
    "minecraft:jungle_trapdoor",
    "minecraft:acacia_trapdoor",
    "minecraft:dark_oak_trapdoor",
    "minecraft:mangrove_trapdoor",
    "minecraft:cherry_trapdoor",
    "minecraft:pale_oak_trapdoor",
    "minecraft:oak_fence_gate",
    "minecraft:spruce_fence_gate",
    "minecraft:birch_fence_gate",
    "minecraft:jungle_fence_gate",
    "minecraft:acacia_fence_gate",
    "minecraft:dark_oak_fence_gate",
    "minecraft:mangrove_fence_gate",
    "minecraft:cherry_fence_gate",
    "minecraft:pale_oak_fence_gate"
  ]
```

**Examples**:
- Add buttons: `"minecraft:oak_button", "minecraft:stone_button"`
- Add levers: `"minecraft:lever"`
- Add chests: `"minecraft:chest"` (not recommended)

### 🔐 Command Permissions

Controls who can use spectator commands.

```toml
[permissions]
  # Permission level for /spectator command (0-4)
  # 0 = everyone, 1 = moderator, 2 = admin, 3 = operator, 4 = owner
  spectator_command_permission_level = 0
  
  # Permission level for /survival command (0-4)
  survival_command_permission_level = 0
  
  # Require OP status for spectator commands
  require_op_for_spectator = false
```

**Permission Levels**:
- `0` = Everyone
- `1` = Moderator
- `2` = Admin
- `3` = Operator
- `4` = Owner

### 💬 Message Settings

Controls how messages are displayed.

```toml
[messages]
  # Show messages in action bar (top of screen) instead of chat
  use_action_bar_messages = true
  
  # Show warnings when approaching distance limit
  show_distance_warnings = true
```

## 📝 Configuration Examples

### Example 1: Relaxed Spectator Mode

For creative servers where players need more freedom:

```toml
[movement_restrictions]
  max_distance = -1  # No distance limit
  allow_dimension_travel = true

[interactions]
  allow_item_pickup = true
  allow_item_drop = true
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:lever",
    "minecraft:stone_button",
    "minecraft:oak_button"
  ]

[permissions]
  spectator_command_permission_level = 0  # Everyone can use
```

### Example 2: Strict Survival Server

For survival servers with tight restrictions:

```toml
[movement_restrictions]
  max_distance = 50.0  # 50 block radius
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:oak_trapdoor",
    "minecraft:oak_fence_gate"
  ]

[permissions]
  spectator_command_permission_level = 2  # Admin only
  require_op_for_spectator = true
```

### Example 3: Builder Mode

For creative building with observation:

```toml
[movement_restrictions]
  max_distance = -1  # Unlimited
  allow_dimension_travel = true

[player_abilities]
  enable_invulnerability = true
  enable_flight = true

[interactions]
  allow_item_pickup = true
  allow_item_drop = true
  allow_inventory_crafting = true
  interactable_blocks = [
    # All doors, trapdoors, gates
    "minecraft:oak_door",
    "minecraft:spruce_door",
    "minecraft:birch_door",
    "minecraft:jungle_door",
    "minecraft:acacia_door",
    "minecraft:dark_oak_door",
    "minecraft:mangrove_door",
    "minecraft:cherry_door",
    "minecraft:pale_oak_door",
    "minecraft:oak_trapdoor",
    "minecraft:spruce_trapdoor",
    "minecraft:birch_trapdoor",
    "minecraft:jungle_trapdoor",
    "minecraft:acacia_trapdoor",
    "minecraft:dark_oak_trapdoor",
    "minecraft:mangrove_trapdoor",
    "minecraft:cherry_trapdoor",
    "minecraft:pale_oak_trapdoor",
    "minecraft:oak_fence_gate",
    "minecraft:spruce_fence_gate",
    "minecraft:birch_fence_gate",
    "minecraft:jungle_fence_gate",
    "minecraft:acacia_fence_gate",
    "minecraft:dark_oak_fence_gate",
    "minecraft:mangrove_fence_gate",
    "minecraft:cherry_fence_gate",
    "minecraft:pale_oak_fence_gate"
  ]
```

## 🔍 Finding Block IDs

To find the correct block ID:

1. **In-game**: Hold block in hand
2. **Press F3** (Debug screen)
3. **Look for**: "Block: minecraft:block_name"
4. **Copy**: The full ID (e.g., `minecraft:oak_door`)

Or check [Minecraft Wiki](https://minecraft.wiki/w/Block#List_of_blocks) for complete list.

## ⚠️ Important Notes

### Minecraft Engine Limitations

These **cannot be changed** via config:

- **Fall Damage**: Always prevented when flying (Minecraft engine behavior)
- **Block Breaking/Placing**: Always blocked in ADVENTURE mode
- **Auto-Start Flying**: Players must double-press spacebar
- **Mob Attacks**: Always blocked (mobs don't target flying players)

### Loader-Specific Notes

| Loader | Config Support | Notes |
|--------|----------------|-------|
| **NeoForge** | ✅ Full | All options available |
| **Fabric** | ❌ None | Uses hardcoded defaults |
| **Quilt** | ❌ None | Uses hardcoded defaults |

## 🔄 Resetting Configuration

To reset to defaults:

1. **Delete** `config/limitedspectator-common.toml`
2. **Restart** Minecraft or server
3. **New config** will be generated with defaults

## 📞 Need Help?

- Check [FAQ](FAQ-and-Troubleshooting.md) for common questions
- Report issues: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- Ask questions: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)
