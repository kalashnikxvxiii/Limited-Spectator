# Server Admin Guide

This guide covers everything server administrators need to know about Limited Spectator.

## 🖥️ Server Installation

### Prerequisites

- Minecraft Server 1.21.1+
- Mod Loader (NeoForge 21.1.217+ / Fabric 0.16.5+ / Quilt 0.26.4+)
- Java 21+

### Installation Steps

1. **Install Mod Loader**

   ```bash
   # NeoForge
   java -jar neoforge-installer.jar --installServer
   
   # Fabric
   java -jar fabric-installer.jar server
   
   # Quilt
   java -jar quilt-installer.jar install server
   ```

2. **Download Limited Spectator JAR**

   Choose the correct version for your loader:
   - NeoForge: `LimitedSpectator-neoforge-2.0.0.jar`
   - Fabric: `LimitedSpectator-fabric-2.0.0.jar`
   - Quilt: `LimitedSpectator-quilt-2.0.0.jar`

3. **Place JAR in Mods Folder**

   ```bash
   cp LimitedSpectator-*.jar server/mods/
   ```

4. **Start Server**

   ```bash
   ./start.sh  # Linux/macOS
   # or
   start.bat   # Windows
   ```

5. **Verify Installation**

   Check server logs for:
   ```
   [LimitedSpectator] Mod loaded successfully
   ```

## ⚙️ Configuration (NeoForge Only)

After first server start, a config file is created:

**Location**: `config/limitedspectator-common.toml`

### Quick Setup

Edit the config file to customize:

```toml
[movement_restrictions]
  max_distance = 75.0              # Distance limit in blocks
  allow_dimension_travel = false   # Can players go to Nether/End?
  teleport_back_on_exceed = true   # Teleport back or just warn?

[interactions]
  allow_pvp = false                # Can players attack each other?
  allow_item_drop = false          # Can players drop items?
  allow_item_pickup = false        # Can players pick up items?

[permissions]
  spectator_command_permission_level = 0  # Who can use /spectator?
  require_op_for_spectator = false        # Require OP status?
```

### Applying Changes

After editing the config:

1. **Save the file**
2. **In-game**: Run `/reload` command
3. Changes apply immediately (no restart needed)

See [Configuration Guide](Configuration-Guide.md) for all options.

## 👥 Permission Management

### Permission Levels

Limited Spectator uses Minecraft's standard permission levels:

| Level | Role | Can Use `/spectator` |
|-------|------|----------------------|
| 0 | Everyone | ✅ Yes (default) |
| 1 | Moderator | ✅ Yes |
| 2 | Admin | ✅ Yes |
| 3 | Operator | ✅ Yes |
| 4 | Owner | ✅ Yes |

### Setting Permission Levels

Edit `config/limitedspectator-common.toml`:

```toml
[permissions]
  # Only admins (level 2+) can use /spectator
  spectator_command_permission_level = 2
  
  # Require OP status
  require_op_for_spectator = true
```

### Granting OP Status

```bash
# In-game
/op <player_name>

# Or in ops.json file
# Add player UUID with permission level 4
```

## 🔧 Common Configurations

### Configuration 1: Open Spectator Mode

For creative servers where everyone can spectate:

```toml
[movement_restrictions]
  max_distance = -1                # No distance limit
  allow_dimension_travel = true    # Can go anywhere

[interactions]
  allow_item_pickup = true         # Can pick up items
  allow_item_drop = true           # Can drop items

[permissions]
  spectator_command_permission_level = 0  # Everyone can use
  require_op_for_spectator = false
```

### Configuration 2: Restricted Spectator Mode

For survival servers with tight restrictions:

```toml
[movement_restrictions]
  max_distance = 50.0              # 50 block radius
  allow_dimension_travel = false   # No Nether/End
  teleport_back_on_exceed = true   # Teleport back

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false

[permissions]
  spectator_command_permission_level = 2  # Admin only
  require_op_for_spectator = true
```

### Configuration 3: Builder Mode

For creative servers with observation:

```toml
[movement_restrictions]
  max_distance = -1                # Unlimited
  allow_dimension_travel = true    # Can go anywhere

[interactions]
  allow_item_pickup = true
  allow_item_drop = true
  allow_inventory_crafting = true

[permissions]
  spectator_command_permission_level = 1  # Moderator+
```

## 📊 Monitoring & Logging

### Console Logs

Limited Spectator logs important events with `[LimitedSpectator]` prefix:

```
[LimitedSpectator] Player <name> entered spectator mode
[LimitedSpectator] Player <name> exceeded distance limit
[LimitedSpectator] Player <name> exited spectator mode
```

### Checking Active Spectators

Currently, there's no built-in command to list active spectators. You can:

1. **Check logs** for entry/exit messages
2. **Use commands** like `/list` to see players
3. **Monitor distance** violations in logs

## 🚨 Troubleshooting

### Mod Not Loading

**Problem**: Mod doesn't appear in mod list

**Solutions**:
1. Verify JAR is in `server/mods/` folder
2. Check server log for errors
3. Verify mod loader is installed correctly
4. Ensure Minecraft version is 1.21.1+

### Commands Not Working

**Problem**: `/spectator` command not recognized

**Solutions**:
1. Verify mod loaded (check logs)
2. Check player permission level
3. Verify config permissions (if NeoForge)
4. Restart server

### Config Not Applying

**Problem**: Changes to config don't take effect

**Solutions**:
1. Verify file is saved
2. Run `/reload` command
3. Check for syntax errors in TOML file
4. Restart server if `/reload` doesn't work

### Fabric/Quilt Config Not Working

**Problem**: Config file not generated or ignored

**Solution**: This is expected. Fabric and Quilt use hardcoded defaults.

**Workaround**: Use NeoForge for full config support, or request feature on GitHub.

## 🔐 Security Considerations

### Server-Side Enforcement

All restrictions are enforced **server-side**, making the mod secure for multiplayer:

- ✅ Players cannot bypass restrictions with client mods
- ✅ Distance limits enforced by server
- ✅ Interactions validated server-side
- ✅ Permissions checked server-side

### Recommended Settings

For maximum security:

```toml
[permissions]
  spectator_command_permission_level = 2  # Admin only
  require_op_for_spectator = true         # Require OP

[movement_restrictions]
  max_distance = 50.0              # Limited distance
  allow_dimension_travel = false   # No Nether/End
  reset_position_on_logout = true  # Prevent abuse

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
```

## 📈 Performance

Limited Spectator has minimal performance impact:

- **CPU**: Negligible (only checks distance on tick)
- **Memory**: ~2-5 MB per server
- **Network**: Minimal (only HUD state packets)

No performance issues expected even on large servers.

## 🔄 Updates & Compatibility

### Version Compatibility

| Minecraft | NeoForge | Fabric | Quilt | Status |
|-----------|----------|--------|-------|--------|
| 1.21.1 | 21.1.217+ | 0.16.5+ | 0.26.4+ | ✅ Tested |
| 1.21.10 | 21.1.217+ | 0.16.5+ | 0.26.4+ | ✅ Compatible |
| 1.21.11+ | 21.1.217+ | 0.16.5+ | 0.26.4+ | ✅ Compatible |

### Updating the Mod

1. **Stop server**
2. **Backup** `config/limitedspectator-common.toml`
3. **Replace** JAR in `mods/` folder
4. **Start server**
5. **Verify** mod loaded correctly

Config files are compatible across versions.

## 📞 Support

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Questions**: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)
- **Suggestions**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)

### Reporting Issues

Include:
- Minecraft version
- Mod loader and version
- Mod version
- Server log (with `[LimitedSpectator]` messages)
- Config file (if applicable)
- Steps to reproduce

## 📚 Related Guides

- [Installation Guide](Installation.md) - How to install
- [Configuration Guide](Configuration-Guide.md) - Detailed config options
- [Commands Reference](Commands.md) - Available commands
- [FAQ](FAQ-and-Troubleshooting.md) - Common questions

---

**Last Updated**: 2026-01-23  
**Version**: 2.0.0
