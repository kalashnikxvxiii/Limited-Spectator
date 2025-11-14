# Beta Features (v1.1.0-beta)

This page documents the **beta version 1.1.0** of Limited Spectator, which introduces a complete configuration system and many new features.

## ⚠️ Beta Warning

**This is a BETA release** with known issues. Do not use on production servers without thorough testing.

### Should You Use the Beta?

✅ **Use Beta if**:
- You're testing on a development/test server
- You need configurable distance limits or permissions
- You want to customize interaction restrictions
- You're comfortable troubleshooting issues

❌ **Use Stable (v1.0.2) if**:
- You're running a production server
- You need 100% reliability
- You're okay with hardcoded settings
- You want a tested, stable experience

---

## 🆕 What's New in v1.1.0-beta

### 1. Complete Configuration System

The biggest change: **everything is now configurable** via `config/limitedspectator-common.toml`.

#### Configuration File

The config file is automatically generated on first launch with 25+ options organized into sections:

```toml
[movement]
  max_distance = 75.0
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
  interactable_blocks = ["minecraft:oak_door", ...]

[commands]
  spectator_command_permission_level = 0
  survival_command_permission_level = 0
  require_op_for_spectator = false

[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

See the full [Configuration Guide](Configuration-Guide) for details on every option.

---

### 2. Configurable Distance Limits

**Stable v1.0.2**: Fixed 75 blocks (hardcoded)
**Beta v1.1.0**: Fully configurable

```toml
[movement]
  # Set to -1 for unlimited distance
  max_distance = 75.0

  # Choose teleport vs warning
  teleport_back_on_exceed = true
```

**Examples**:
- `max_distance = 50.0` - Tight restriction
- `max_distance = 200.0` - Wide area
- `max_distance = -1.0` - Unlimited (not recommended)

---

### 3. Customizable Permission Levels

**Stable v1.0.2**: All players can use commands (permission level 0)
**Beta v1.1.0**: Configurable per-command

```toml
[commands]
  # 0 = all players, 2 = operators, 4 = console only
  spectator_command_permission_level = 0
  survival_command_permission_level = 0

  # Require OP status regardless of level
  require_op_for_spectator = false
```

**Use Cases**:
- Public servers: Set `spectator_command_permission_level = 2` (ops only)
- Private servers: Keep at `0` (all players)
- Strict control: Enable `require_op_for_spectator = true`

---

### 4. Flexible Interaction Controls

**Stable v1.0.2**: Hardcoded (doors, trapdoors, gates only)
**Beta v1.1.0**: Per-interaction toggles + custom block whitelist

```toml
[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false

  # Custom block whitelist (any Minecraft block ID)
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:stone_button",
    "minecraft:lever",
    "create:copper_valve_handle"  # Modded blocks too!
  ]
```

**Comparison**:
| Feature | Stable | Beta |
|---------|--------|------|
| PvP Toggle | ❌ Always off | ✅ Configurable |
| Mob Attacks | ❌ Always off | ✅ Configurable |
| Item Drop | ❌ Always off | ✅ Configurable |
| Item Pickup | ❌ Always off | ✅ Configurable |
| Block Whitelist | ❌ Hardcoded | ✅ Custom list |

---

### 5. Hot-Reload Support

**Stable v1.0.2**: Requires restart to change settings (hardcoded)
**Beta v1.1.0**: Live reload via `/reload` command

Changes take effect immediately:
```
/reload
```

No server restart needed for config changes!

---

### 6. Gamemode Selection

**Stable v1.0.2**: Always uses Adventure mode
**Beta v1.1.0**: Choose between Adventure or vanilla Spectator

```toml
[abilities]
  # "ADVENTURE" or "SPECTATOR"
  spectator_gamemode = "ADVENTURE"
```

**⚠️ Warning**: Using `"SPECTATOR"` may allow noclip and bypass restrictions. Only use on trusted servers.

---

### 7. Toggle Invulnerability

**Stable v1.0.2**: Always invulnerable
**Beta v1.1.0**: Configurable

```toml
[abilities]
  enable_invulnerability = true
```

**Note**: Due to a known issue, disabling this doesn't enable fall damage (see Known Issues below).

---

### 8. Message Customization

**Stable v1.0.2**: Action bar messages only
**Beta v1.1.0**: Choose action bar or chat

```toml
[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

---

### 9. Dimension Tracking Fix

**Stable v1.0.2**: Bug when switching dimensions
**Beta v1.1.0**: Fixed with proper dimension tracking

The beta correctly saves and restores your dimension when using `/survival`, preventing cross-dimension bugs.

---

## 🐛 Known Issues

### Known Limitations

#### 1. Fall Damage Cannot Be Enabled

**Status**: Minecraft Engine Limitation (Not a Bug)
**Severity**: N/A

**Description**: Fall damage is **always prevented** when `mayfly=true` (Minecraft core behavior). This cannot be changed even with `enable_invulnerability=false`.

**Workaround**: None. This is a Minecraft engine limitation, not a mod issue.

**Impact**: Spectators are always protected from fall damage. `enable_invulnerability` protects against other damage types (mobs, lava, fire, etc.).

---

## 📊 Comparison: Stable vs Beta

| Feature | Stable v1.0.2 | Beta v1.1.0 |
|---------|---------------|-------------|
| **Stability** | ✅ Production-ready | ⚠️ Beta testing |
| **Configuration File** | ❌ No config | ✅ Full TOML config |
| **Distance Limit** | 🔒 75 blocks (hardcoded) | ✅ Configurable |
| **Permission Levels** | 🔒 Level 0 (hardcoded) | ✅ Configurable |
| **Interaction Toggles** | 🔒 Hardcoded | ✅ Individual toggles |
| **Block Whitelist** | 🔒 Doors/gates only | ✅ Custom list |
| **Hot-Reload** | ❌ Restart needed | ✅ `/reload` support |
| **Dimension Fix** | ⚠️ Known bug | ✅ Fixed |
| **Known Issues** | ✅ None | ⚠️ 3 high-priority |
| **Best For** | Production servers | Testing/advanced |

---

## 🔄 Migration Guide: Stable → Beta

Upgrading from v1.0.2 to v1.1.0-beta:

### Step 1: Backup

```bash
# Backup your server
cp -r server/ server-backup/

# Backup player data
cp -r world/playerdata/ world/playerdata-backup/
```

### Step 2: Install Beta

1. Stop the server
2. Remove `LimitedSpectator-1.21.1-1.0.2.jar` from `mods/`
3. Add `LimitedSpectator-1.21.1-1.1.0-beta.jar` to `mods/`
4. Start the server

### Step 3: Configure

The config file generates automatically at `config/limitedspectator-common.toml` with defaults matching stable behavior:

```toml
[movement]
  max_distance = 75.0  # Same as stable
  allow_dimension_travel = false  # Same as stable

[interactions]
  # All false - same as stable
  interactable_blocks = [doors, trapdoors, gates]  # Same as stable
```

**Default config replicates stable v1.0.2 behavior**, so no changes needed unless you want to customize.

### Step 4: Test Thoroughly

Test on a development server first:

- [ ] Test `/spectator` and `/survival` commands
- [ ] Verify distance limits work
- [ ] Test dimension travel blocking
- [ ] Verify interactions are blocked correctly
- [ ] Check HUD hiding
- [ ] Test with multiple players

### Step 5: Deploy or Rollback

If testing succeeds, deploy to production. If issues occur:

1. Stop server
2. Restore stable JAR: `cp server-backup/mods/LimitedSpectator-1.21.1-1.0.2.jar mods/`
3. Delete beta JAR
4. Delete config: `rm config/limitedspectator-common.toml`
5. Start server

---

## 🚀 Beta Testing Feedback

Help improve Limited Spectator by testing the beta!

### How to Report Issues

1. Go to [GitHub Issues](../../issues/new)
2. Title: `[Beta 1.1.0] <short description>`
3. Include:
   - Steps to reproduce
   - Expected vs actual behavior
   - Config file (`limitedspectator-common.toml`)
   - Server logs (`logs/latest.log`)
   - Minecraft/NeoForge versions

### What to Test

Priority testing areas:

- [ ] Configuration hot-reloading
- [ ] Custom permission levels
- [ ] Custom block whitelists
- [ ] Distance limit variations (50, 100, 500, unlimited)
- [ ] Interaction toggles (PvP, mob attacks, items)
- [ ] Dimension travel blocking
- [ ] Multi-player scenarios

---

## 📅 Beta Roadmap

### Before Stable Release

The following must be resolved before v1.1.0 goes stable:

1. **Fix auto-start flying** (high priority)
2. **Document or fix fall damage** (high priority)
3. **Resolve block breaking/placing** or remove config options (medium priority)
4. **Testing period**: At least 2 weeks of beta testing on various servers

### Target Stable Release

**Estimated**: 2-4 weeks after v1.1.0-beta (TBD based on testing feedback)

---

## 🔗 Related Pages

- [Configuration Guide](Configuration-Guide) - Full configuration reference (beta only)
- [Version Comparison](Version-Comparison) - Detailed feature comparison
- [Installation](Installation) - How to install beta version
- [FAQ & Troubleshooting](FAQ-and-Troubleshooting) - Common issues

---

**Ready to test?** Download [v1.1.0-beta](../../releases/tag/v1.1.0-beta) and report your findings on [GitHub Issues](../../issues)!
