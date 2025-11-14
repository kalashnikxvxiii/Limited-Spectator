# FAQ & Troubleshooting

Common questions and solutions for Limited Spectator.

## 📌 Version Notice

**Current Stable Version: v1.1.1**

This FAQ covers Limited Spectator v1.1.1 with full configuration support. Configuration examples can be customized in `config/limitedspectator-common.toml`.

Legacy versions (v1.0.2, v1.1.0-beta) are superseded by v1.1.1.

---

## Frequently Asked Questions

### General Questions

#### Q: Is this mod server-side only or does it need to be on the client too?

**A**: Limited Spectator is **server-side** with optional client features.

- **Server only**: All core features work (distance limits, restrictions, commands)
- **Client + Server**: Enhanced features like automatic HUD hiding
- **Client only**: Not useful; the mod needs to be on the server to function

**Recommendation**: Install on server (required) and client (optional).

---

#### Q: Does this work with Forge or only NeoForge?

**A**: **NeoForge only**. This mod is built for NeoForge 21.1.213+ and will not work with Forge.

- **NeoForge 1.21.1**: ✅ Supported
- **Forge 1.21.1**: ❌ Not supported
- **Fabric/Quilt**: ❌ Not supported

---

#### Q: Can I use this on a vanilla server?

**A**: No, Limited Spectator requires NeoForge to be installed on the server. Vanilla servers cannot load mods.

---

#### Q: Will this work with existing spectator mode plugins?

**A**: It may conflict. Limited Spectator implements its own spectator system using Adventure mode, which may interfere with other spectator plugins.

**Compatibility Tips**:
- Disable other spectator/gamemode plugins
- Test on a development server first
- Check for permission conflicts

---

#### Q: How many players can be in spectator mode at once?

**A**: Unlimited. The mod tracks each player independently with minimal performance impact.

---

#### Q: Does this work in single-player?

**A**: Yes! Install Limited Spectator in your client `mods/` folder.

- **Beta v1.1.0**: Configuration file at `run/config/limitedspectator-common.toml`
- **Stable v1.0.2**: No config file, uses default settings

---

### Feature Questions

#### Q: Why can't I fly through blocks like vanilla spectator mode?

**A**: Limited Spectator uses **Adventure mode + flight** instead of vanilla Spectator mode. This is intentional to:
- Prevent noclip exploits
- Improve compatibility with anti-cheat plugins
- Allow granular interaction control

**Beta v1.1.0 only**: If you need noclip, set `spectator_gamemode = "SPECTATOR"` in config (not recommended for servers).

**Stable v1.0.2**: Always uses Adventure mode, cannot be changed.

---

#### Q: Can I set different distance limits for different players?

**A**: No, not in either version.

- **Beta v1.1.0**: All players share the same `max_distance` config value
- **Stable v1.0.2**: All players use the fixed 75-block limit

**Workaround**: Use permission mods to grant some players access to `/tp` commands for extended travel.

**Future**: Per-player limits are planned for a future release.

---

#### Q: Can spectators see each other?

**A**: Yes, spectators are visible to all players. They appear as flying players in Adventure mode.

---

#### Q: Can I make spectators invisible?

**A**: Not natively. You can use the `/effect` command:

```
/effect give <player> minecraft:invisibility infinite 0 true
```

This hides the player but their nametag may still be visible.

---

#### Q: Why does the distance limit reset when I re-enter spectator mode?

**A**: The starting position is saved **each time** you use `/spectator`. This is intentional to allow players to spectate different areas.

**Example**:
1. Use `/spectator` at X=0, Z=0 (can travel 75 blocks around this)
2. Use `/survival`, walk to X=200, Z=200
3. Use `/spectator` again (can now travel 75 blocks around X=200, Z=200)

---

#### Q: Can I change my starting position without exiting spectator mode?

**A**: Not currently. You must:
1. Use `/survival` to exit
2. Walk to the new location
3. Use `/spectator` to re-enter

**Future**: A `/spectator reset` command may be added.

---

### Configuration Questions

**⚠️ Beta v1.1.0 Only**: These questions apply only to the beta version with configuration system. Stable v1.0.2 has no config file.

#### Q: Why don't my config changes apply?

**A** (Beta v1.1.0 only): You need to reload the configuration. Use:

```
/reload
```

Or restart the server.

---

#### Q: Can I have different configs for different worlds/dimensions?

**A** (Beta v1.1.0 only): No, Limited Spectator uses a single global config (`limitedspectator-common.toml`) for all worlds and dimensions.

---

#### Q: What happens if I set `max_distance = 0`?

**A** (Beta v1.1.0 only): Players will be immediately teleported back to their starting position. Essentially, they cannot move. Set to `-1` for unlimited distance instead.

**Stable v1.0.2**: This option does not exist. Fixed at 75 blocks.

---

#### Q: Can I use this mod with other game modes (creative, adventure)?

**A**: Limited Spectator is designed for players transitioning from **Survival mode**. Using `/spectator` from Creative or Adventure mode may cause unexpected behavior.

**Best Practice**: Only use on players in Survival mode.

---

## Troubleshooting

### Installation Issues

#### Problem: Mod doesn't load / "Mod not found"

**Symptoms**:
- Commands not available
- No log messages from Limited Spectator
- Mod not in `/mods` list

**Solutions**:

1. **Check NeoForge Version**
   ```
   /version
   ```
   Must show NeoForge 21.1.213 or higher, **not Forge**.

2. **Verify JAR Location**
   - File must be in `mods/` folder
   - File name: `LimitedSpectator-1.21.1-1.1.0-beta.jar`
   - No extra folders (not `mods/LimitedSpectator/...`)

3. **Check Server Logs**
   Look in `logs/latest.log` for:
   ```
   [LimitedSpectator] Limited Spectator mod loaded successfully!
   ```

4. **Verify Minecraft Version**
   Must be Minecraft 1.21.1 (not 1.21, 1.20, etc.)

5. **Check for Conflicts**
   Remove other spectator/gamemode mods temporarily.

---

#### Problem: "Incompatible mod set" error

**Symptoms**:
- Server/client crashes on startup
- Error mentions Limited Spectator

**Solutions**:

1. Ensure Java 21 or higher:
   ```
   java -version
   ```

2. Update NeoForge to 21.1.213+

3. Check for mod conflicts in error log

---

#### Problem: Config file not generating (Beta v1.1.0 only)

**Note**: Stable v1.0.2 does NOT have a config file. If you're using stable, this is expected.

**Symptoms** (Beta only):
- `config/limitedspectator-common.toml` doesn't exist
- Commands use default values

**Solutions**:

1. Verify you're using Beta v1.1.0, not Stable v1.0.2
2. Start the server **fully** (wait for "Done" message)
3. Check `config/` folder permissions (must be writable)
4. Look for config errors in `logs/latest.log`
5. Manually create the file from the [Configuration Guide](Configuration-Guide) examples

---

### Command Issues

#### Problem: "/spectator: Unknown command"

**Symptoms**:
- Command not recognized
- Tab completion doesn't show the command

**Solutions**:

1. Verify mod is loaded (check server console)
2. Check you're on the server with the mod installed
3. Try `/survival` instead (if you're already in spectator)
4. Restart the server

---

#### Problem: "You do not have permission to use this command"

**Symptoms**:
- Command exists but gives permission error

**Solutions**:

1. **If using Beta v1.1.0**: Check config permission levels:
   ```toml
   [commands]
     spectator_command_permission_level = 0
     require_op_for_spectator = false
   ```

2. **If using Stable v1.0.2**: Permission is fixed at level 0. You need OP to bypass if there's a permission error.

3. Ask admin for OP status:
   ```
   /op <yourname>
   ```

3. Check if permission mods are interfering

4. Verify you're not banned/restricted on the server

---

#### Problem: "/spectator" does nothing / no message

**Symptoms**:
- Command executes but nothing happens
- No confirmation message

**Solutions**:

1. **Already in spectator mode**
   - Use `/survival` to exit first
   - Check your abilities: `/data get entity @s abilities`

2. **Config blocks the feature**
   - Check `enable_flight = true` in config

3. **Lag/connection issues**
   - Check network latency
   - Try logging out and back in

---

#### Problem: Can't use "/survival" to exit spectator mode

**Symptoms**:
- "You are not in spectator mode" message
- Stuck in Adventure mode

**Solutions**:

1. Use vanilla gamemode command:
   ```
   /gamemode survival
   ```

2. Ask admin to teleport you back:
   ```
   /tp <yourname> <x> <y> <z>
   ```

3. Log out and back in (Beta: if `reset_position_on_logout = true`; Stable: always resets)

4. Check server logs for errors during spectator entry

---

### Gameplay Issues

#### Problem: Teleported back immediately when trying to move

**Symptoms**:
- Can't move more than a few blocks
- Constant teleporting back to start

**Solutions**:

**If using Beta v1.1.0**:

1. **Check distance limit**
   ```toml
   [movement]
     max_distance = 75.0
   ```
   If set to a very low value (e.g., 1.0), increase it.

2. **Disable teleport-back temporarily**
   ```toml
   teleport_back_on_exceed = false
   ```

3. **Set unlimited distance**
   ```toml
   max_distance = -1.0
   ```

4. Reload config: `/reload`

**If using Stable v1.0.2**:

This is a bug or unexpected behavior. Stable uses fixed 75 blocks. Check server logs for errors.

---

#### Problem: Can't interact with ANY blocks

**Symptoms**:
- Doors don't open
- Buttons don't work
- Nothing responds to clicks

**Solutions**:

**If using Beta v1.1.0**:

1. **Check interactable blocks list**
   ```toml
   [interactions]
     interactable_blocks = [
       "minecraft:oak_door",
       "minecraft:stone_button"
     ]
   ```

**Note**: Block breaking and placing are **always disabled** in ADVENTURE mode (Minecraft GameMode restriction).

**If using Stable v1.0.2**:

Only doors, trapdoors, and fence gates are interactable (hardcoded). Cannot be changed.

**For all versions**:

- **Client-side issue**: Ensure Limited Spectator is installed on your client too

---

#### Problem: Can still drop/pickup items despite config (Beta v1.1.0 only)

**Note**: Stable v1.0.2 always blocks item drop/pickup. No config needed.

**Symptoms** (Beta only):
- `allow_item_drop = false` but items drop
- `allow_item_pickup = false` but items are picked up

**Solutions** (Beta only):

1. **Reload configuration**
   ```
   /reload
   ```

2. **Verify config syntax**
   Check for typos in `limitedspectator-common.toml`

3. **Server-side enforcement**
   If you're testing in single-player, ensure config changes are applied

4. **Check for mod conflicts**
   Other mods may override these restrictions

---

#### Problem: Dimension travel is allowed despite being disabled

**Symptoms**:
- Can enter Nether/End portals
- (Beta only) `allow_dimension_travel = false` but can change dimensions

**Solutions**:

**If using Beta v1.1.0**:

1. **Reload config**
   ```
   /reload
   ```

2. **Check config value**
   ```toml
   [movement]
     allow_dimension_travel = false
   ```

**Both versions**:

3. **Portal bypass**
   Some mods/plugins may override dimension events. Check for conflicts.

**If using Stable v1.0.2**:

Dimension travel is always blocked (hardcoded). If you can still travel, there's a mod conflict or bug.

---

#### Problem: HUD doesn't hide in spectator mode

**Symptoms**:
- HUD stays visible
- Hotbar/health/hunger bars still shown in spectator mode

**Solutions**:

1. **Install client-side**
   HUD hiding requires Limited Spectator on the **client**. It won't work with server-only installation.

2. **HUD Behavior**
   HUD is **always automatically hidden** when entering spectator mode (hard-coded behavior).
   Press **F1** to toggle HUD visibility temporarily.

3. **Use F1 manually**
   Press F1 to show/hide HUD (works in spectator mode)

---

#### Problem: Stuck in spectator mode after death

**Symptoms**:
- Died while in spectator
- Respawned still in spectator mode

**Solutions**:

1. Use `/survival` command

2. Admin can force gamemode:
   ```
   /gamemode survival <player>
   ```

3. If commands don't work, check server logs for errors

---

### Performance Issues

#### Problem: Server lag when many players are in spectator mode

**Symptoms**:
- TPS drops when spectators are active
- Server slows down

**Solutions**:

1. **Increase distance check interval** (requires code modification)
   Default: Every tick (20 times per second)

2. **Reduce max distance**
   Smaller distance = faster calculations

3. **Check for other performance issues**
   Limited Spectator has minimal impact. Look for other causes:
   - Chunk loading
   - Other mods
   - Entity count

---

#### Problem: Client FPS drops in spectator mode

**Symptoms**:
- Low framerate while spectating
- Game stutters

**Solutions**:

1. **Not caused by Limited Spectator**
   The mod adds no client-side rendering. Check:
   - Render distance
   - Shader mods
   - Entity count in view

2. **Use F1 to toggle HUD**
   Press **F1** to temporarily show HUD (includes debug info)

---

## Error Messages

### "You cannot change dimensions in spectator mode!"

**Cause**: Tried to enter portal or teleport to another dimension with `allow_dimension_travel = false`

**Solution**:
- Use `/survival` to exit spectator mode first
- Or ask admin to enable dimension travel in config

---

### "You have exceeded the distance limit and were teleported back!"

**Cause**: Traveled beyond `max_distance` from starting position

**Solution**:
- Stay within the distance limit
- Ask admin to increase `max_distance`
- Or exit spectator, walk closer, re-enter

---

### "Warning: You are reaching the distance limit!"

**Cause**: At 90% of `max_distance`

**Solution**: Turn back or you'll be teleported soon

---

### "You are already in spectator mode"

**Cause**: Used `/spectator` while already spectating

**Solution**: Use `/survival` to exit first

---

### "You are not in spectator mode"

**Cause**: Used `/survival` while in survival/creative mode

**Solution**: Use `/spectator` to enter spectator mode first

---

## Mod Compatibility

### Compatible Mods

Limited Spectator works well with:

- **Create**: Spectators can observe contraptions
- **Waystones**: Teleportation works normally (within distance limits)
- **JourneyMap / Xaero's**: Minimap mods work fine
- **Inventory Management**: (e.g., Inventory Profiles Next)

### Known Conflicts

Potential conflicts with:

- **Other spectator mods**: May override spectator behavior
- **GameMode plugins**: May conflict with Adventure mode detection
- **Anti-cheat plugins**: May flag flying spectators (configure anti-cheat to allow flight)

### Testing Compatibility

To test a mod for compatibility:

1. Install both mods on a test server
2. Enter spectator mode: `/spectator`
3. Test key features: distance limits, interactions, dimension travel
4. Check server logs for errors
5. Report incompatibilities on GitHub Issues

---

## Getting Help

### Before Asking for Help

1. Check this FAQ page
2. Review the [Configuration Guide](Configuration-Guide)
3. Check `logs/latest.log` for errors
4. Verify mod version and requirements
5. Test on a clean server without other mods

### Reporting Bugs

If you find a bug, please report it on [GitHub Issues](../../issues/new) with:

1. **Mod version**: `1.21.1-1.1.0-beta`
2. **NeoForge version**: From `/version` command
3. **Minecraft version**: 1.21.1
4. **Steps to reproduce**: Detailed steps to recreate the bug
5. **Expected behavior**: What should happen
6. **Actual behavior**: What actually happens
7. **Server logs**: Relevant portions from `logs/latest.log`
8. **Config file**: Your `limitedspectator-common.toml`
9. **Other mods**: List of installed mods

### Feature Requests

For feature requests, open an issue on [GitHub](../../issues/new) with:

1. **Description**: What feature you want
2. **Use case**: Why this feature is needed
3. **Expected behavior**: How it should work
4. **Configuration**: Any config options needed

---

**Still need help?** [Open an issue](../../issues/new) on GitHub!
