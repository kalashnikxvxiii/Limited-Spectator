# Commands Reference

Limited Spectator provides two main commands for entering and exiting spectator mode.

## Version Compatibility

**Current Stable Version: v1.1.1**

| Feature | v1.1.1 (Current) | Legacy v1.0.2 |
|---------|------------------|---------------|
| **Commands** | ✅ `/spectator`, `/survival` | ✅ `/spectator`, `/survival` |
| **Permission Levels** | ✅ Configurable (0-4) | 🔒 Fixed at 0 (all players) |
| **OP Requirement** | ✅ Configurable | ❌ Not available |

**Note**: Permission configuration is available in v1.1.1. Legacy v1.0.2 always uses permission level 0.

---

## Command Overview

| Command | Permission (Stable) | Permission (Beta) | Description |
|---------|---------------------|-------------------|-------------|
| `/spectator` | Level 0 (all players) | Configurable (default: 0) | Enter limited spectator mode |
| `/survival` | Level 0 (all players) | Configurable (default: 0) | Exit spectator mode and return to survival |

## `/spectator` Command

Enters limited spectator mode, allowing the player to fly and observe with restrictions.

### Syntax

```
/spectator
```

### Behavior

When executed:

1. **Saves Current Position**: Your exact coordinates are saved as the "starting position"
2. **Changes Game Mode**: Sets you to Adventure mode with flight abilities
3. **Enables Flight**: Players can double-tap spacebar to start flying
4. **Hides HUD**: Hides the HUD for cleaner viewing (F1 key toggles temporarily)
5. **Applies Restrictions**: All configured restrictions take effect immediately

### Success Message

```
Entered spectator mode. Use /survival to return.
```

### Example

```
/spectator
```

**Result**: You begin flying at your current position, which becomes your boundary center.

### Permission Requirements

#### v1.1.1 (Current)

**Configurable permission levels** - Set in `config/limitedspectator-common.toml`:
```toml
[commands]
  spectator_command_permission_level = 0  # 0-4
  require_op_for_spectator = false
```

#### Legacy v1.0.2

**Fixed at permission level 0** - All players can use `/spectator`. No configuration available.

#### Beta v1.1.0

Permission is controlled by two config options:

```toml
[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false
```

**Permission Levels**:
- `0` - All players can use the command
- `1` - Moderators only (bypass spawn protection)
- `2` - Operators only
- `3` - Operators (admin commands)
- `4` - Console/high-level admins only

**Operator Requirement**:
- If `require_op_for_spectator = true`, only OP players can use the command regardless of permission level

### Error Messages

#### Insufficient Permissions

```
You do not have permission to use this command.
```

**Cause**: Your permission level is below `spectator_command_permission_level`
**Solution**: Ask a server admin to give you permission or adjust the config

#### Already in Spectator Mode

```
You are already in spectator mode.
```

**Cause**: You're already in spectator mode
**Solution**: Use `/survival` to exit first

---

## `/survival` Command

Exits spectator mode and returns the player to survival mode at their starting position.

### Syntax

```
/survival
```

### Behavior

When executed:

1. **Teleports to Start**: Returns you to the exact position where you used `/spectator`
2. **Restores Game Mode**: Sets you back to Survival mode
3. **Disables Flight**: Removes flight abilities
4. **Shows HUD**: Restores the HUD (if it was hidden)
5. **Clears State**: Removes you from the spectator tracking system

### Success Message

```
Returned to survival mode.
```

### Example

```
/survival
```

**Result**: You're teleported back to where you started spectating and returned to survival mode.

### Permission Requirements

```toml
[commands]
  survival_command_permission_level = 0
```

**Default**: Permission level `0` (all players)

**Recommended**: Keep this at `0` so spectators can always return to survival mode themselves.

### Error Messages

#### Not in Spectator Mode

```
You are not in spectator mode.
```

**Cause**: You're not currently in spectator mode
**Solution**: Use `/spectator` to enter spectator mode first

#### Insufficient Permissions

```
You do not have permission to use this command.
```

**Cause**: Your permission level is below `survival_command_permission_level` (unusual configuration)
**Solution**: Ask an admin to adjust the config or use `/gamemode survival` if you have OP

---

## Permission Configuration

### Setting Permission Levels

Edit `config/limitedspectator-common.toml`:

```toml
[commands]
  # Who can enter spectator mode
  spectator_command_permission_level = 0

  # Who can exit spectator mode
  survival_command_permission_level = 0

  # Require OP status for spectator commands
  require_op_for_spectator = false
```

### Common Permission Setups

#### 1. Public Access (Default)

Everyone can enter and exit spectator mode:

```toml
spectator_command_permission_level = 0
survival_command_permission_level = 0
require_op_for_spectator = false
```

#### 2. Staff Only

Only operators can enter spectator mode:

```toml
spectator_command_permission_level = 2
survival_command_permission_level = 0
require_op_for_spectator = false
```

#### 3. Operators Only (Strict)

Only OP players can use spectator commands:

```toml
spectator_command_permission_level = 0  # Ignored
survival_command_permission_level = 0   # Ignored
require_op_for_spectator = true
```

#### 4. Admin Controlled

Only high-level admins can grant spectator access:

```toml
spectator_command_permission_level = 4
survival_command_permission_level = 0
require_op_for_spectator = false
```

**Use case**: Admins manually set players to spectator mode using other commands

### Granting Permissions

#### Using Vanilla `/op`

```
/op <player>
```

This gives permission level 4 (all commands)

#### Using Permission Mods

If using mods like **LuckPerms** or **FTB Chunks**, set permissions:

```
# Example with LuckPerms
/lp user <player> permission set limitedspectator.command.spectator true
```

**Note**: Limited Spectator doesn't have native permission mod integration yet. Use permission levels in the config instead.

---

## Usage Examples

### Example 1: Basic Usage

```
Player: /spectator
Server: Entered spectator mode. Use /survival to return.

[Player flies around, observes base]

Player: /survival
Server: Returned to survival mode.
```

### Example 2: Distance Limit

```
Player: /spectator
Server: Entered spectator mode. Use /survival to return.

[Player flies 70 blocks away - approaching limit]
Server (action bar): Warning: You are reaching the distance limit!

[Player flies 80 blocks away - exceeds limit]
Server: You have exceeded the distance limit and were teleported back!
[Player teleported to starting position]

Player: /survival
Server: Returned to survival mode.
```

### Example 3: Dimension Travel Blocked

```
Player: /spectator
Server: Entered spectator mode. Use /survival to return.

[Player tries to enter Nether portal]
Server: You cannot change dimensions in spectator mode!
[Player remains in Overworld]

Player: /survival
Server: Returned to survival mode.
```

### Example 4: Permission Denied

```
Player: /spectator
Server: You do not have permission to use this command.

[Player asks admin for permission]

Admin: /op Player
Server: Made Player a server operator

Player: /spectator
Server: Entered spectator mode. Use /survival to return.
```

---

## Command Behavior Details

### What Happens on Logout?

If `reset_position_on_logout = true` (default):

1. Player logs out while in spectator mode
2. On rejoin, player spawns at their spectator starting position
3. Player is still in spectator mode
4. Must use `/survival` to exit

**Prevents**: Position abuse by logging out at distant locations

### What Happens on Death?

If you die while in spectator mode (with `enable_invulnerability = false`):

1. Normal death mechanics apply
2. Spectator state is **cleared**
3. You respawn normally at your respawn point
4. Must use `/spectator` again to re-enter

### What Happens with `/gamemode`?

If an admin uses `/gamemode creative <player>` on a spectator:

1. Player changes to creative mode
2. Spectator state persists internally
3. Distance limits and restrictions **still apply**
4. Use `/survival` to properly exit spectator mode

**Recommendation**: Always use `/survival` instead of `/gamemode` to exit spectator mode properly.

### Multiple Spectator Entries

```
Player: /spectator
Server: Entered spectator mode.

[Starting position: X=100, Y=70, Z=200]

Player: /survival
Server: Returned to survival mode.
[Teleported to X=100, Y=70, Z=200]

[Player walks to X=500, Y=80, Z=600]

Player: /spectator
Server: Entered spectator mode.

[New starting position: X=500, Y=80, Z=600]
```

**Note**: The starting position updates each time you use `/spectator`

---

## Admin Commands

### Forcing Players Out of Spectator Mode

Admins can use vanilla commands:

```
/gamemode survival <player>
```

However, this **does not** properly clear spectator state. The player should use:

```
/execute as <player> run survival
```

Or manually:

```
/tell <player> Please use /survival to exit spectator mode properly
```

### Checking Spectator Status

There's no built-in command to check who is in spectator mode. Admins should:

1. Check the server console for spectator entry/exit logs:
   ```
   [LimitedSpectator] Player entered spectator mode at (X, Y, Z)
   [LimitedSpectator] Player exited spectator mode
   ```

2. Use `/data get entity <player>` and look for:
   - `playerGameType: 2` (Adventure mode)
   - `abilities.mayfly: 1b` (flight enabled)

---

## Troubleshooting Commands

### Command Not Found

**Symptoms**: `/spectator` shows "Unknown command"

**Causes**:
1. Mod not installed on server
2. Mod failed to load
3. You're on the client without the mod

**Solutions**:
1. Check server logs for mod loading
2. Verify `LimitedSpectator-1.21.1-1.1.0-beta.jar` is in `mods/` folder
3. Restart the server

### Command Does Nothing

**Symptoms**: Command executes but nothing happens

**Causes**:
1. You're already in spectator mode (`/spectator`)
2. You're not in spectator mode (`/survival`)
3. Config has incompatible settings

**Solutions**:
1. Check your current game mode: `/gamemode`
2. Try the opposite command
3. Check server logs for errors

### Permission Denied

**Symptoms**: "You do not have permission to use this command"

**Solutions**:
1. Ask admin to check `spectator_command_permission_level` in config
2. Ask for OP: `/op <yourname>`
3. Check if `require_op_for_spectator = true`

---

**Next**: Learn about [Features](Features) or check [FAQ & Troubleshooting](FAQ-and-Troubleshooting) for common issues.
