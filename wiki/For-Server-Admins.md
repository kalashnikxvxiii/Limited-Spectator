# For Server Admins

This guide is specifically for server administrators deploying Limited Spectator in production environments. It covers best practices, security considerations, and deployment strategies.

## 📌 Version Notice for Admins

**Current Stable Version: v1.2.1**

This guide covers Limited Spectator v1.2.1 with full configuration support. All configuration examples are available in `config/limitedspectator-common.toml` and can be hot-reloaded with `/reload`.

**Legacy versions** (v1.0.2, v1.1.0-beta, v1.1.1): If using older versions, upgrade to v1.2.1 for the latest features and security updates.

---

## Deployment Strategies

### Server Types and Recommended Configurations

**Note**: These configurations require **Beta v1.1.0**. Stable v1.0.2 uses fixed defaults.

#### Competitive PvP Servers

**Goal**: Minimize spectator advantages, prevent scouting

**Recommended Config**:
```toml
[movement]
  max_distance = 50.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
  interactable_blocks = []

[commands]
  spectator_command_permission_level = 2
  require_op_for_spectator = true

[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

**Rationale**:
- Tight 50-block radius prevents long-range scouting
- No interactions prevent cheating assistance
- OP-only access ensures only trusted staff can spectate
- Position reset prevents teleport exploits

---

#### Cooperative PvE Servers

**Goal**: Allow helpful spectating without breaking gameplay

**Recommended Config**:
```toml
[movement]
  max_distance = 150.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = true
  allow_inventory_crafting = false
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:stone_button",
    "minecraft:lever"
  ]

[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false

[messages]
  use_action_bar_messages = true
  show_distance_warnings = true
```

**Rationale**:
- 150-block radius allows base exploration
- Mob attacks enabled so spectators can help with defense
- Item pickup allowed for loot collection
- Open access for all players
- Still prevents griefing (no block breaking/placing)

---

#### Creative/Building Servers

**Goal**: Allow flexible spectating for design review

**Recommended Config**:
```toml
[movement]
  max_distance = 500.0
  allow_dimension_travel = true
  teleport_back_on_exceed = false
  reset_position_on_logout = false

[interactions]
  allow_pvp = false
  allow_item_drop = true
  allow_item_pickup = true
  allow_inventory_crafting = true
  interactable_blocks = [
    "minecraft:oak_door",
    "minecraft:chest",
    "minecraft:lever",
    "minecraft:oak_button"
  ]

[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false

[messages]
  show_distance_warnings = false
```

**Rationale**:
- Large 500-block radius for big builds
- Dimension travel for Nether/End builds
- Warnings only (no forced teleport)
- Item interactions for inventory management
- Block breaking still disabled to prevent griefing

---

#### Roleplay/Survival Servers

**Goal**: Allow spectating for AFK/idle players

**Recommended Config**:
```toml
[movement]
  max_distance = 100.0
  allow_dimension_travel = false
  teleport_back_on_exceed = true
  reset_position_on_logout = true

[player_abilities]
  enable_invulnerability = true
  enable_flight = true

[interactions]
  allow_pvp = false
  allow_item_drop = false
  allow_item_pickup = false
  allow_inventory_crafting = false
  interactable_blocks = ["minecraft:oak_door"]

[commands]
  spectator_command_permission_level = 0
  require_op_for_spectator = false
```

**Rationale**:
- Moderate distance for temporary observation
- Full invulnerability for AFK safety
- Minimal interactions (doors only)
- Public access for all players
- HUD automatically hidden (F1 to toggle)

---

## Security Best Practices

### Permission Management

#### Using Permission Levels

Set appropriate permission levels based on server trust:

```toml
# Public servers (untrusted players)
spectator_command_permission_level = 2
require_op_for_spectator = true

# Semi-private servers (trusted community)
spectator_command_permission_level = 1
require_op_for_spectator = false

# Private servers (friends/family)
spectator_command_permission_level = 0
require_op_for_spectator = false
```

#### Granting Temporary Spectator Access

For temporary access without full OP:

1. Give OP temporarily:
   ```
   /op <player>
   [Player uses /spectator]
   /deop <player>
   ```

2. Or reduce permission requirement:
   ```toml
   spectator_command_permission_level = 0
   ```
   Then change it back after.

3. Or use permission mods (future feature).

---

### Preventing Exploits

#### Distance Limit Bypass Prevention

**Exploit**: Player uses `/spectator`, flies near limit, logs out, logs back in at edge, uses `/survival` to "teleport"

**Prevention**:
```toml
[movement]
  reset_position_on_logout = true
```

This forces players back to starting position on logout.

---

#### Dimension Travel Exploits

**Exploit**: Player uses Nether to travel 8x distance (coordinate scaling)

**Prevention**:
```toml
[movement]
  allow_dimension_travel = false
```

---

#### Item Transfer Exploits

**Exploit**: Spectator drops items to help other players

**Prevention**:
```toml
[interactions]
  allow_item_drop = false
  allow_item_pickup = false
```

The mod properly handles item drops by cancelling the event and restoring the item to inventory.

---

#### Noclip Exploits

**Risk**: Using vanilla `SPECTATOR` gamemode allows phasing through walls

**Prevention**:
```toml
[abilities]
  spectator_gamemode = "ADVENTURE"
```

Default `ADVENTURE` mode prevents noclip while allowing flight.

---

### Monitoring and Logging

#### Server Console Logs

Limited Spectator logs all spectator mode changes:

```
[LimitedSpectator] Player entered spectator mode at (123.45, 64.0, -678.90)
[LimitedSpectator] Player exited spectator mode
[LimitedSpectator] Player exceeded distance limit (current: 85.3, max: 75.0)
```

**Monitoring Tips**:
- Watch for frequent spectator mode switches (potential exploit attempts)
- Track distance violations (may indicate bypass attempts)
- Log spectator entries during PvP events (potential unfair advantage)

#### Custom Logging Script

Create a log parser to track spectator usage:

```bash
#!/bin/bash
# spectator-monitor.sh
grep "LimitedSpectator" logs/latest.log | grep "entered spectator" | awk '{print $4, $5, $6}'
```

This extracts all spectator entries with timestamps.

---

## Integration with Other Plugins/Mods

### Anti-Cheat Compatibility

Limited Spectator may trigger anti-cheat plugins due to flight. Configure your anti-cheat:

#### NoCheatPlus

Add to `config.yml`:
```yaml
checks:
  moving:
    survivalfly:
      active: false
```

Or exempt spectators:
```yaml
permissions:
  - 'nocheatplus.checks.moving.survivalfly'
```

#### Spartan Anti-Cheat

Whitelist Limited Spectator:
```yaml
Checks:
  Flight:
    enabled: false
```

---

### Permission Plugins (Future)

Limited Spectator does not yet integrate with permission plugins, but you can work around this:

#### LuckPerms Workaround

Use command aliases to require permissions:

1. Install a command alias plugin
2. Create alias that checks permission, then runs `/spectator`

**Future versions** will have native LuckPerms support.

---

### Claiming/Protection Plugins

Limited Spectator respects server-side block interaction events, so plugins like:

- **FTB Chunks**: Claimed areas block spectator interactions
- **GriefPrevention**: Protected areas remain protected
- **WorldGuard**: Region flags apply to spectators

**Testing**: Ensure spectators cannot interact with protected blocks.

---

### Economy Plugins

No direct integration needed, but consider:

- Charging for spectator access via economy plugins
- Restricting spectator mode to premium members

**Implementation**: Use permission levels + economy plugin rewards.

---

## Performance Optimization

### Measuring Performance Impact

Limited Spectator has minimal performance impact:

- **Per-tick overhead**: ~0.01ms per spectator (distance check)
- **Memory usage**: ~200 bytes per spectator (position storage)
- **Network usage**: ~10 bytes per HUD packet

#### Benchmarking

Test with Spark profiler:

```
/spark profiler start
[Players use spectator mode]
/spark profiler stop
```

Look for `SpectatorMod.onPlayerTick` in results.

---

### Optimization Tips

#### Reduce Distance Check Frequency (Advanced)

For servers with 100+ simultaneous spectators, consider modifying the code to check distance every 5 ticks instead of every tick:

```java
// In SpectatorMod.java
if (serverPlayer.tickCount % 5 == 0) {
    // Distance check logic
}
```

This reduces overhead by 80% with minimal impact on enforcement.

---

#### Increase Distance Limit Instead of Unlimited

Instead of `max_distance = -1.0` (unlimited), use a very large value:

```toml
max_distance = 10000.0
```

This provides soft limits without disabling the feature, allowing future enforcement if needed.

---

## Backup and Maintenance

### Configuration Backups

Always backup config before updates:

```bash
cp config/limitedspectator-common.toml config/limitedspectator-common.toml.backup
```

Restore if update breaks config:

```bash
cp config/limitedspectator-common.toml.backup config/limitedspectator-common.toml
```

---

### Version Updates

When updating Limited Spectator:

1. **Read Changelog**: Check for breaking changes
2. **Backup Config**: Save current config
3. **Test on Development Server**: Never update directly on production
4. **Compare Configs**: Check for new config options
5. **Update Production**: Once tested, deploy to production

---

### Rollback Plan

If an update causes issues:

1. Stop the server
2. Replace JAR with previous version
3. Restore config backup
4. Restart server
5. Report bug on GitHub

---

## Advanced Configuration Scenarios

### Scenario 1: Time-Limited Spectator Access

**Goal**: Allow spectators for 5 minutes, then auto-kick

**Implementation**: Use a scheduled task plugin

```yaml
# With CommandTimer or similar
tasks:
  - command: "/execute as @a[gamemode=adventure] run survival"
    interval: 300 # 5 minutes
```

**Future**: Native time limits planned.

---

### Scenario 2: Spectator-Only Areas

**Goal**: Restrict spectator mode to specific regions

**Implementation**: Use WorldGuard + command restrictions

1. Create region: `/region define spectator-zone`
2. Allow `/spectator` only in region using command filtering

**Workaround**: Not natively supported yet.

---

### Scenario 3: Spectator Queue System

**Goal**: Limit spectators to 5 at a time

**Implementation**: External queue system

1. Track spectator count with a plugin
2. Only allow `/spectator` if count < 5
3. Auto-kick oldest spectator when limit reached

**Future**: Native spectator limits planned.

---

### Scenario 4: Per-Rank Distance Limits

**Goal**: VIP members get 200-block radius, regular members get 75

**Implementation**: Not supported in current version

**Workaround**:
1. Create two separate servers with different configs
2. Use permission plugin + command override
3. Wait for future update with per-player limits

---

## Troubleshooting for Admins

### Players Report Being "Stuck" in Spectator Mode

**Diagnosis**:
1. Check player's game mode: `/data get entity <player> playerGameType`
2. Check abilities: `/data get entity <player> abilities`
3. Check server logs for errors during spectator entry

**Solutions**:
1. Force game mode change: `/gamemode survival <player>`
2. Teleport to spawn: `/tp <player> <spawn coordinates>`
3. Kick and rejoin: `/kick <player> Spectator mode reset`

---

### Distance Limits Not Enforced

**Diagnosis**:
1. Check config: `max_distance` value
2. Check if `/reload` was run after config change
3. Verify mod is loaded: grep logs for "LimitedSpectator"

**Solutions**:
1. Reload config: `/reload`
2. Restart server
3. Verify TOML syntax (use online validator)

---

### Spectators Can Break/Place Blocks

**Note**: Block breaking and placing are **always disabled** in ADVENTURE mode at the GameMode level. This cannot be changed or configured.

If spectators can break blocks, they are likely not in Limited Spectator mode. Verify with:
1. Check if player is in spectator mode (`/spectator` was used)
2. Verify `spectator_gamemode = "ADVENTURE"` in config
3. Check for mod conflicts overriding game modes
3. Check logs for event cancellation messages

---

## Server Rules and Player Guidelines

### Recommended Server Rules for Spectator Mode

Post these rules in your server documentation:

1. **No Scouting**: Don't use spectator mode to scout enemy bases in PvP
2. **No Ghosting**: Don't share info from spectator mode in voice chat
3. **No Exploit Attempts**: Don't try to bypass distance limits or restrictions
4. **Respectful Spectating**: Don't harass players while spectating
5. **Time Limits**: Spectator mode is for temporary observation only (if applicable)

### Enforcing Rules

- **Warnings**: Use `/warn <player>` (if using moderation plugin)
- **Temp Ban**: `/ban <player> 1d Spectator mode abuse`
- **Remove Access**: Change config to `require_op_for_spectator = true`

---

## Multi-Server Setups

### Using Limited Spectator Across Multiple Servers

For networks with multiple servers (BungeeCord, Velocity):

**Considerations**:
- Each server has independent config
- Spectator state does NOT transfer between servers
- Player switching servers exits spectator mode

**Recommended Setup**:
1. Use identical configs across all servers
2. Document that spectator mode is per-server
3. Consider adding `/spectator` to each server's help text

---

## Support and Community

### Getting Admin Support

For admin-specific questions:

1. Check this guide first
2. Review [Configuration Guide](Configuration-Guide)
3. Search [GitHub Issues](../../issues)
4. Open new issue with "Server Admin" tag

### Reporting Security Issues

**DO NOT** report security vulnerabilities publicly. Email: `[your email]` or open a private security advisory on GitHub.

### Contributing to Development

Admins can contribute by:

- Testing beta releases on test servers
- Suggesting features based on real-world usage
- Reporting compatibility issues with other mods
- Documenting advanced configurations

See [CLAUDE.md](../../blob/main/CLAUDE.md) for roadmap and contribution guidelines.

---

## Checklist for Production Deployment

Before deploying Limited Spectator to production:

- [ ] Tested on development server
- [ ] Config customized for server type
- [ ] Permission levels set appropriately
- [ ] Anti-cheat configured to allow spectator flight
- [ ] Server rules updated with spectator guidelines
- [ ] Backup of config created
- [ ] Staff trained on `/spectator` and `/survival` commands
- [ ] Players notified of new feature
- [ ] Monitoring set up for spectator logs
- [ ] Rollback plan documented

---

**Need more help?** [Open an issue](../../issues/new) or check the [FAQ](FAQ-and-Troubleshooting).
