# Features

Limited Spectator provides a comprehensive set of features designed to allow safe, controlled observation of your Minecraft world while maintaining gameplay balance and server security.

## 📌 Version Notice

**Current Stable Version: v1.1.1**

This page documents features for Limited Spectator v1.1.1, which includes full configuration support. Configuration examples can be customized in `config/limitedspectator-common.toml`.

**Legacy versions** (v1.0.2, v1.1.0-beta) are superseded by v1.1.1.

---

## Core Features

### 🛫 Flight System

Players in spectator mode can fly freely within configured boundaries.

**How It Works**:
- Automatically enabled when entering spectator mode
- Uses Adventure mode with `mayfly` and `flying` abilities
- Speed controlled by vanilla flight mechanics
- No noclip (cannot fly through blocks)

**Configuration**:
```toml
[player_abilities]
  enable_flight = true
```

**Note**: Players must double-tap spacebar to start flying (ADVENTURE mode limitation).

**Advantages Over Vanilla Spectator**:
- Cannot phase through blocks
- Cannot view mob perspectives
- More compatible with anti-cheat plugins

---

### 📏 Distance Limits

Prevents players from traveling too far from their starting position.

**How It Works**:
1. When you use `/spectator`, your position is saved
2. Server checks your distance every tick
3. If distance exceeds `max_distance`, you're teleported back
4. Warning shown at 90% of the limit

**Configuration** (Beta v1.1.0 only):
```toml
[movement]
  max_distance = 75.0
  teleport_back_on_exceed = true
  show_distance_warnings = true
```

**Stable v1.0.2**: Fixed at 75 blocks, always teleports back, always shows warnings.

**Example Scenarios** (Beta only - Stable is always 75 blocks):

| `max_distance` | Use Case |
|----------------|----------|
| `50.0` | Small base inspection |
| `75.0` | Default (Stable uses this) |
| `100.0` | Medium builds/farms |
| `250.0` | Large towns/cities |
| `-1.0` | Unlimited (not recommended) |

**Technical Details**:
- Distance calculated in 3D Euclidean space: `√(Δx² + Δy² + Δz²)`
- Calculated from saved starting position
- Ignores Y-axis for 2D distance if needed (future feature)

---

### 🌍 Dimension Locking

Prevents dimension changes while in spectator mode.

**How It Works**:
- Intercepts `PlayerChangedDimensionEvent`
- Blocks portal usage (Nether, End, custom dimensions)
- Blocks `/tp` commands to other dimensions
- Shows warning message to player

**Configuration** (Beta v1.1.0 only):
```toml
[movement]
  allow_dimension_travel = false
```

**Stable v1.0.2**: Dimension travel always blocked, cannot be enabled.

**Blocked Actions**:
- Entering Nether portals
- Entering End portals
- Using `/execute in <dimension>`
- Teleporting via mods/plugins to other dimensions

**Why This Matters**:
Dimension travel could be exploited to:
- Bypass distance limits (e.g., travel 8x distance via Nether)
- Scout End dimensions for dragon fights
- Access protected dimensions

**When to Enable** (Beta v1.1.0 only):
Set `allow_dimension_travel = true` only on:
- Creative/testing servers
- Servers where dimension travel is already unrestricted
- Single-player worlds

**Note**: This option is not available in Stable v1.0.2.

---

### 🚫 Interaction Restrictions

Granular control over world interactions.

#### Block Interactions

**How It Works**:
- Client blocks left/right clicks on blocks
- Server validates all interactions
- Whitelist system for specific blocks

**Configuration** (Beta v1.1.0 only):
```toml
[interactions]
  interactable_blocks = ["minecraft:oak_door", "minecraft:lever"]
```

**Note**: Block breaking/placing always blocked in ADVENTURE mode at GameMode level (cannot be overridden).

**Blocked by Default**:
- Breaking blocks
- Placing blocks
- Opening chests, barrels, shulker boxes
- Using crafting tables, furnaces, anvils
- Interacting with villagers
- Using beds

**Allowed by Default**:
- Doors (oak, iron, etc.)
- Trapdoors
- Fence gates

**Custom Whitelist Example** (Beta v1.1.0 only):
```toml
interactable_blocks = [
  "minecraft:oak_door",
  "minecraft:stone_button",
  "minecraft:lever",
  "minecraft:chest",  # Allow chest access
  "create:copper_valve_handle"  # Modded block
]
```

**Stable v1.0.2**: Cannot customize this list.

#### Entity Interactions

**How It Works**:
- `AttackEntityEvent` cancelled for spectators
- Prevents damage to players and mobs
- Prevents villager trading

**Configuration**:
```toml
[interactions]
  allow_pvp = false
```

**Note**: Mob attacks are **always blocked**. Mobs don't target players with `mayfly=true` anyway (Minecraft core behavior).

**Blocked Actions**:
- Attacking players
- Attacking mobs
- Damaging entities with projectiles
- Using splash potions on entities

**When to Enable PvP/Mob Attacks**:
- Cooperative servers where spectators can help
- Testing servers
- Servers where spectators are temporary teammates

#### Item Interactions

**How It Works**:
- `ItemTossEvent` cancelled and item restored to inventory
- `ItemEntityPickupEvent.Pre` returns `TriState.FALSE`
- Client blocks right-click item usage

**Configuration** (Beta v1.1.0 only):
```toml
[interactions]
  allow_item_drop = false
  allow_item_pickup = false
```

**Stable v1.0.2**: Item drop/pickup always blocked, cannot be enabled.

**Blocked Actions**:
- Dropping items from inventory
- Picking up items from ground
- Using items (food, potions, tools)
- Throwing items (ender pearls, snowballs)

**Technical Details**:
- **Item Drop**: Event is cancelled + item manually restored because `ItemTossEvent` removes items before firing
- **Item Pickup**: Uses `TriState.FALSE` to properly block without side effects

**Why This Matters**:
Prevents spectators from:
- Transferring items to other players
- Collecting valuable loot
- Using consumables without consequences

---

### 🛡️ Invulnerability

Makes spectators immune to all damage.

**How It Works**:
- Sets `invulnerable` ability flag
- Blocks all damage sources
- Prevents death in spectator mode

**Configuration** (Beta v1.1.0 only):
```toml
[abilities]
  enable_invulnerability = true
```

**Stable v1.0.2**: Invulnerability always enabled, cannot be disabled.

**Note**: Even in Beta, disabling invulnerability doesn't fully work due to a known issue with fall damage.

**Damage Sources Blocked**:
- Fall damage
- Fire/lava damage
- Drowning
- Mob attacks
- Player attacks
- Environmental damage (cacti, sweet berries, etc.)
- Void damage

**When to Disable**:
Rarely recommended, but can be disabled if:
- You want spectators to be vulnerable
- Testing fall damage on builds
- Creative/sandbox servers

---

### 🖥️ HUD Management

Automatically hides the HUD for a cleaner viewing experience.

**How It Works**:
- Server sends `SpectatorHudPacket` to client
- Client applies HUD visibility changes
- F1 key toggles HUD temporarily

**Configuration** (Beta v1.1.0 only):
**Note**: HUD always auto-hides when entering spectator mode. F1 key can toggle HUD temporarily. This behavior is hard-coded and cannot be configured.

**Hidden Elements**:
- Hotbar
- Health/hunger bars
- Armor bar
- Experience bar
- Crosshair

**Visible Elements**:
- Chat (always visible)
- Debug screen (F3)
- Pause menu (ESC)

**Client-Side Requirement**:
This feature **only works** if Limited Spectator is installed on the client. Server-only installations will not hide the HUD.

**F1 Toggle**:
- Press F1 to temporarily show HUD
- Press F1 again to hide it
- HUD automatically hides again when exiting spectator mode

---

### 📍 Position Management

Tracks and manages player positions throughout spectator sessions.

#### Position Saving

**How It Works**:
- On `/spectator`: Saves exact coordinates (X, Y, Z) and dimension
- Stored in server memory (`HashMap<UUID, Vec3>`)
- Used as the center point for distance calculations

**Saved Data**:
```java
Position {
  x: 123.456
  y: 64.0
  z: -789.012
  dimension: minecraft:overworld
}
```

#### Position Restoration

**How It Works**:
- On `/survival`: Teleports player back to saved position
- Restores exact coordinates (including decimal positions)
- Clears saved position from memory

**When Position is Restored**:
1. Using `/survival` command
2. On logout (if `reset_position_on_logout = true`)
3. On server restart (positions are cleared)

#### Position Reset on Logout

**How It Works**:
- On player logout, checks if they're in spectator mode
- If yes, teleports to saved position before logout
- Prevents position abuse

**Configuration** (Beta v1.1.0 only):
```toml
[movement]
  reset_position_on_logout = true
```

**Stable v1.0.2**: Position always resets on logout, cannot be disabled.

**Why This Matters**:
Without this, players could:
1. Use `/spectator` at base
2. Fly 70 blocks away
3. Log out
4. Log back in at the distant position
5. Use `/survival` to "teleport" there

With reset enabled, players always log back in at their starting position.

---

### 💬 Message System

Flexible notification system for player feedback.

**How It Works**:
- Two display modes: Action bar and chat
- Warnings for approaching distance limits
- Confirmation messages for mode changes

**Configuration** (Beta v1.1.0 only):
```toml
[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

**Stable v1.0.2**: Always uses action bar messages, always shows distance warnings.

#### Action Bar Messages

Displayed above the hotbar, non-intrusive.

**Examples**:
```
[Action Bar] Entered spectator mode. Use /survival to return.
[Action Bar] Warning: You are reaching the distance limit!
[Action Bar] You cannot change dimensions in spectator mode!
```

#### Chat Messages

Displayed in chat, more permanent.

**Examples**:
```
[Chat] Entered spectator mode. Use /survival to return.
[Chat] You have exceeded the distance limit and were teleported back!
```

#### Distance Warnings

**Warning Thresholds**:
- **90% of max_distance**: Warning message
- **100% of max_distance**: Teleport + error message

**Example with `max_distance = 100.0`**:
- At 89 blocks: No warning
- At 90 blocks: "Warning: You are reaching the distance limit!"
- At 100 blocks: "You have exceeded the distance limit and were teleported back!"

---

### 🔄 Hot-Reload Support

All configuration changes can be applied without restarting.

**How It Works**:
- Configuration is cached in memory
- `/reload` command triggers `ModConfigEvent`
- All config values are reloaded
- Changes apply immediately to all players

**What Can Be Hot-Reloaded**:
- Distance limits
- Interaction permissions
- Command permissions
- Message settings
- Client settings

**What Cannot Be Hot-Reloaded**:
- Mod installation (requires restart)
- Network packet registration

**Usage**:
```
/reload
```

Server will reload all configs, including Limited Spectator.

---

### 🔐 Security Features

Built with server security in mind.

#### Server-Side Authority

**How It Works**:
- All restrictions enforced on server
- Client checks are for UX only
- No client trust required

**Why This Matters**:
Modded clients cannot bypass:
- Distance limits
- Interaction restrictions
- Dimension locks
- Item drop/pickup blocks

#### Dependency Security

**Updated Dependencies**:
```gradle
// Fixes CVE-2025-24970 (CVSS 7.5)
force 'io.netty:netty-all:4.1.118.Final'

// Fixes CVE-2025-48924 (CVSS 5.3)
force 'org.apache.commons:commons-lang3:3.18.0'
```

These ensure the mod uses secure versions of critical libraries.

#### Permission System

**Multiple Permission Layers**:
1. Permission levels (0-4)
2. OP requirement flag
3. Per-command permissions

**Example**:
```toml
spectator_command_permission_level = 2
require_op_for_spectator = true
```

Only operators with permission level 2+ can use `/spectator`.

---

## Feature Comparison

### Limited Spectator vs. Vanilla Spectator

| Feature | Limited Spectator | Vanilla Spectator |
|---------|-------------------|-------------------|
| **Flight** | ✅ Yes | ✅ Yes |
| **Noclip** | ❌ No | ✅ Yes |
| **Distance Limits** | ✅ Configurable | ❌ None |
| **Dimension Lock** | ✅ Yes | ❌ No |
| **Interaction Control** | ✅ Granular | ❌ All blocked |
| **Mob Perspective** | ❌ No | ✅ Yes |
| **Invulnerability** | ✅ Configurable | ✅ Yes |
| **HUD Control** | ✅ Auto-hide | 🔶 Manual |
| **Position Restore** | ✅ Yes | ❌ No |
| **Server Security** | ✅ High | 🔶 Medium |

### Limited Spectator vs. Creative Mode

| Feature | Limited Spectator | Creative Mode |
|---------|-------------------|---------------|
| **Flight** | ✅ Yes | ✅ Yes |
| **Break Blocks** | ❌ No (default) | ✅ Yes |
| **Place Blocks** | ❌ No (default) | ✅ Yes |
| **Invulnerability** | ✅ Yes | ✅ Yes |
| **Distance Limits** | ✅ Yes | ❌ None |
| **Item Access** | 🔶 Inventory only | ✅ All items |
| **Purpose** | Observation | Building |

---

## Advanced Features

### Hybrid Mode Detection

**How It Works**:
Limited Spectator uses a hybrid approach:
- **Game Mode**: ADVENTURE (not SPECTATOR)
- **Abilities**: `mayfly=true`, `flying=true`, `invulnerable=true`
- **Detection**: Checks for Adventure + mayfly + !creative

**Client-Side Detection**:
```java
private boolean isInSpectatorMode(Player player) {
    return player.getAbilities().mayfly
        && !player.getAbilities().instabuild
        && player.gameMode == GameType.ADVENTURE;
}
```

**Server-Side Tracking**:
```java
private static final Map<UUID, Boolean> inSpectatorMode = new HashMap<>();
```

**Why Hybrid**:
- More compatible with other mods
- Better anti-cheat compatibility
- Prevents noclip exploits
- Allows granular interaction control

---

## Planned Features

Features planned for future releases:

- **Translations**: Multi-language support (en_us, it_it, etc.)
- **Time Limits**: Maximum duration in spectator mode
- **Custom Events**: `SpectatorModeEnterEvent`, `SpectatorModeExitEvent` for mod developers
- **Permission Integration**: Native LuckPerms, FTB Chunks support
- **Persistent Storage**: Save positions across server restarts
- **Visual Boundaries**: Particle effects at distance limits
- **Spectator List**: Command to see all spectators
- **Per-Player Limits**: Different distance limits per player/group

See [CLAUDE.md](../../blob/main/CLAUDE.md) for the full roadmap.

---

**Next**: Check out [Configuration Guide](Configuration-Guide) for detailed settings or [Commands](Commands) for usage instructions.
