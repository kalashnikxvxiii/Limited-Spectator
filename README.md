🧭 Limited Spectator
======================

A lightweight NeoForge mod that introduces a restricted spectator mode for Minecraft players.


📖 Overview
=============

Limited Spectator allows players to enter a controlled spectator mode that limits what they can do while flying and observing the world.
Unlike the vanilla spectator mode, this version allows to press F1 to show HUD and restricts interactions to preserve game balance on servers or custom worlds.

Perfect for:

• SMP;

• Builders who want visual observation without full spectator privileges.


✨ Features
=============

✅ **Fully Configurable Spectator Mode**
Comprehensive TOML-based configuration system allowing server owners to customize every aspect of spectator behavior.

✅ **Limited Spectator Mode**
Players can enter a custom spectator state that allows free flight but prevents inventory use, PvP, and world interactions.

✅ **Flexible Distance Limits**
Configure maximum travel distance from start position (default 75 blocks) or disable it entirely. Choose between teleport-back or warning behavior.

✅ **Customizable Block Interactions**
Define exactly which blocks players can interact with using Minecraft block IDs. Default includes doors, trapdoors, and fence gates.

✅ **Granular Permission Control**
Set different permission levels (0-4) for `/spectator` and `/survival` commands, with optional OP requirement.

✅ **Selective HUD Visibility**
HUD elements remain hidden by default but can be temporarily shown by pressing F1 (configurable).

✅ **Dimension Travel Control**
Configure whether players can switch dimensions (Nether, End, etc.) while in spectator mode.

✅ **Player Repositioning**
Automatic position reset when exceeding distance limits or executing `/survival` command.

✅ **Flight Capability**
Players in spectator mode can fly by double-pressing the space bar.

✅ **Combat & Interaction Restrictions**
Individually toggle PvP, mob attacks, item drop/pickup, and inventory crafting.

✅ **Hot-Reloadable Configuration**
All settings can be changed in `config/limitedspectator-common.toml` and reloaded with `/reload` command.

✅ **Server-Friendly**
All restrictions are handled server-side for secure multiplayer behavior.


🧠 Technical Details
=====================

| Aspect                | Value              |
| --------------------- | ------------------ |
| **Minecraft version** | 1.21.1             |
| **Mod loader**        | NeoForge           |
| **NeoForge version**  | 21.1.0+            |
| **Java version**      | 21                 |
| **Gradle wrapper**    | 8.10.2             |
| **Mod ID**            | `limitedspectator` |


⚙️ Commands
=============

| Command      | Description                                   | Default Permission |
| ------------ | --------------------------------------------- | ------------------ |
| `/spectator` | Switch to the limited spectator mode          | Level 0 (everyone) |
| `/survival`  | Return to normal gameplay and restore the HUD | Level 0 (everyone) |

🧩 Command feedback messages are in English (localization planned).

**Note**: Permission levels can be customized in the config file. See Configuration section below.


⚙️ Configuration
==================

Limited Spectator features a comprehensive configuration system. On first launch, a config file is automatically generated at:

**Location**: `config/limitedspectator-common.toml`

### Configuration Categories

#### 🚶 Movement Restrictions
- `max_distance` - Maximum travel distance from start position (default: 75.0 blocks, -1 to disable)
- `allow_dimension_travel` - Allow dimension changes in spectator mode (default: false)
- `teleport_back_on_exceed` - Teleport back when exceeding distance vs. warning (default: true)
- `reset_position_on_logout` - Reset position on logout to prevent abuse (default: true)

#### 🎮 Player Abilities
- `enable_invulnerability` - Make players invulnerable (protects from mobs and environmental damage, not fall damage) (default: true)
- `enable_flight` - Allow flight in spectator mode (default: true)
- `spectator_gamemode` - GameMode to use: "ADVENTURE" or "SPECTATOR" (default: "ADVENTURE")

#### 🔧 Interaction Restrictions
- `allow_pvp` - Allow attacking other players (default: false)
- `allow_item_drop` - Allow dropping items (default: false)
- `allow_item_pickup` - Allow picking up items (default: false)
- `allow_inventory_crafting` - Allow inventory crafting (2x2 grid) (default: false)
- `interactable_blocks` - List of block IDs players can interact with (default: all doors, trapdoors, gates)

**Note**: Mob attacks are always disabled because mobs don't target players with `mayfly=true` ability (Minecraft core behavior).

#### 🔐 Command Permissions
- `spectator_command_permission_level` - Permission level for `/spectator` (0-4, default: 0)
- `survival_command_permission_level` - Permission level for `/survival` (0-4, default: 0)
- `require_op_for_spectator` - Require OP status for spectator commands (default: false)

#### 🖥️ Client & HUD Behavior
- HUD automatically hides when entering spectator mode (hard-coded)
- F1 key toggles HUD visibility temporarily

#### 💬 Message Settings
- `use_action_bar_messages` - Show messages in action bar instead of chat (default: true)
- `show_distance_warnings` - Show warnings when approaching distance limit (default: true)

### Example Configurations

**Server with relaxed spectator mode:**
```toml
[movement_restrictions]
  max_distance = -1  # No distance limit
  allow_dimension_travel = true

[interactions]
  allow_item_pickup = true  # Allow picking up items
  interactable_blocks = ["minecraft:oak_door", "minecraft:lever", "minecraft:stone_button"]
```

**Strict survival server:**
```toml
[permissions]
  spectator_command_permission_level = 2  # Requires moderator rank
  require_op_for_spectator = true

[movement_restrictions]
  max_distance = 50.0  # Shorter distance limit
  reset_position_on_logout = true  # Prevent logout abuse
```

All configuration changes can be applied without restarting the server using `/reload`.


🔐 Default Restrictions in Spectator Mode
===========================================

**Note**: Most restrictions below are configurable via `config/limitedspectator-common.toml`

• ❌ No block breaking or placing (enforced by ADVENTURE mode - cannot be changed)

• ❌ No chest, bed, crafting table, or item interactions (configurable via block whitelist)

• ❌ No inventory crafting (2x2 grid) - ingredients restored automatically (configurable)

• ❌ No dimension travel (configurable)

• ❌ No mob attacks (mobs don't target players with mayfly ability)

• ❌ No PvP (configurable)

• ❌ No item dropping or pickup (individually configurable)

• ✅ Doors, trapdoors, and fence gates remain interactable (fully customizable via block IDs)

• ✅ F1 toggles HUD visibility (configurable)

• ✅ Distance limit: 75 blocks (configurable, can be disabled)


🧩 Compatibility
==================

• ✅ Minecraft 1.21.1

• ✅ NeoForge 21.1.0+

• ✅ Multiplayer-safe

• ⚠️ Limited Spectator uses standard NeoForge event hooks and should be compatible with most mods. However, mods that deeply alter player gamemode handling or HUD rendering may interfere with its behavior.


🧰 Installation
=================

### ⚠️ Beta Release

This is a **beta version** with extensive new features. Most functionality works well, but some advanced features have known limitations (see Known Issues below).

• Install NeoForge 21.1.0+

• Place `limitedspectator-1.21.1-1.1.0-beta.jar` into your `mods/` folder

• Launch Minecraft - config file will be auto-generated at `config/limitedspectator-common.toml`

• (Optional) Customize the configuration to suit your server's needs

• Reload changes with `/reload` command without restarting


🧪 Developer / Debug Info
===========================

For debugging, Limited Spectator includes console logs for:

• Player mode changes

• Spectator mode entry/exit

• Interaction blocking

• HUD toggle events

• Packet handling (SpectatorHudPacket)

These logs appear in the console with prefix:
[LimitedSpectator]


🌍 Localization
=================

Multi-language support will be added in future versions.


⚠️ Minecraft ADVENTURE Mode Limitations
=========================================

Limited Spectator uses Minecraft's ADVENTURE mode (instead of SPECTATOR mode) to prevent noclip while allowing flight. This design choice introduces some inherent limitations:

### Core Limitations (By Design)

**These are NOT bugs** - they are fundamental restrictions of Minecraft's ADVENTURE mode that cannot be overridden:

1. **Manual Flight Activation**: Players must double-press spacebar to start flying. Auto-start flying requires vanilla SPECTATOR mode (which enables noclip).

2. **Fall Damage Only**: When `mayfly=true`, Minecraft's engine prevents fall damage calculation entirely. However, `enable_invulnerability=true` **DOES protect** against mobs, lava, fire, cacti, and other environmental damage.

3. **Block Breaking/Placing Disabled**: ADVENTURE mode blocks all building actions at the GameMode level. This cannot be changed without deep Mixins into Minecraft's core engine.

### Why These Limitations Exist

Limited Spectator prioritizes **observation without noclip** over feature completeness. Enabling vanilla SPECTATOR mode would allow players to:
- Phase through walls and blocks (noclip)
- Access areas they shouldn't see
- Bypass server protections

These limitations preserve the mod's core purpose: **balanced spectating for servers**.

### Alternative: Use SPECTATOR Mode

If you need full vanilla spectator features (invulnerability, noclip, etc.), set `spectator_gamemode = "SPECTATOR"` in the config. Note that this enables noclip and reduces security for multiplayer servers.


🧾 License
============

This project is licensed under the MIT License.
See the LICENSE file for details.
Copyright (c) 2025 karashi

You may use, modify, and distribute this mod freely, provided that attribution is maintained.


📦 Project Links
==================

| Platform     | Link                                                                                             |
| ------------ | ------------------------------------------------------------------------------------------------ |
| **Modrinth** | [Limited Spectator on Modrinth](https://modrinth.com/mod/limited-spectator)                      |
| **CurseForge** | [Limited Spectator on CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/limited-spectator)                      |
| **GitHub**   | [GitHub Repository](https://github.com/kalashnikxvxiii-collab/Limited-Spectator)                 |
| **Issues**   | [Report Bugs / Suggestions](https://github.com/kalashnikxvxiii-collab/Limited-Spectator/issues)  |


❤️ Credits
============

• Author: Karashi

• Development: Full custom codebase for NeoForge 1.21.1

• Icon Design: AI-generated minimalist design (OpenAI)

• Special thanks: Minecraft & NeoForge community


🚀 Future Roadmap
===================

• 🌐 Add multilingual translation files (en_us.json, it_it.json) for command messages and notifications

• 🧱 Optional integration with third-party permission systems (LuckPerms, FTB Chunks)

• 🔍 Extended API for custom spectator events (SpectatorModeEnterEvent, SpectatorModeExitEvent)

• 💾 Persistent state storage (save spectator positions across server restarts)

• ⏱️ Spectator time limits (configurable max duration in spectator mode)

• ✨ Particle effects or visual boundaries for distance limits
