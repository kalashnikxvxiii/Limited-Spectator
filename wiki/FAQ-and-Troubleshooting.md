# FAQ & Troubleshooting

## ❓ Frequently Asked Questions

### General Questions

#### Q: What is Limited Spectator?
**A**: Limited Spectator is a Minecraft mod that allows players to enter a restricted spectator mode. Unlike vanilla spectator mode, it prevents noclip while allowing flight and observation, making it perfect for servers and creative worlds.

#### Q: Which Minecraft versions are supported?
**A**: Minecraft 1.21.1 and higher. The mod is tested on 1.21.1, 1.21.10, and 1.21.11+, and works on all 1.21.x versions.

#### Q: Which mod loaders are supported?
**A**: 
- ✅ NeoForge 21.1.217+ (full support)
- ✅ Fabric 0.16.5+ (full support)
- ✅ Quilt 0.26.4+ (production only)

#### Q: Is it multiplayer-safe?
**A**: Yes! All restrictions are enforced server-side, making it secure for multiplayer servers.

#### Q: Can I use this on a server?
**A**: Yes! The mod is designed for both single-player and multiplayer servers. See [Server Admin Guide](For-Server-Admins.md) for setup.

### Installation Questions

#### Q: Where do I download the mod?
**A**: Download from:
- [Modrinth](https://modrinth.com/mod/limited-spectator)
- [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/limited-spectator)
- [GitHub Releases](https://github.com/kalashnikxvxiii/Limited-Spectator/releases)

#### Q: Which JAR should I download?
**A**: Choose based on your mod loader:
- **NeoForge**: `LimitedSpectator-neoforge-2.0.0.jar`
- **Fabric**: `LimitedSpectator-fabric-2.0.0.jar`
- **Quilt**: `LimitedSpectator-quilt-2.0.0.jar`

#### Q: Where do I put the JAR file?
**A**: Place it in your `mods/` folder:
- **Windows**: `%APPDATA%\.minecraft\mods\`
- **macOS**: `~/Library/Application Support/minecraft/mods/`
- **Linux**: `~/.minecraft/mods/`

#### Q: The mod isn't loading. What do I do?
**A**: See [Troubleshooting: Mod Not Loading](#mod-not-loading) below.

### Configuration Questions

#### Q: Where is the config file?
**A**: `config/limitedspectator-common.toml` (NeoForge only)

**Note**: Fabric and Quilt use hardcoded defaults. Configuration support planned for future release.

#### Q: How do I apply config changes?
**A**: 
1. Edit the config file
2. Save it
3. Run `/reload` command in-game
4. Changes apply immediately (no restart needed)

#### Q: Can I customize which blocks players can interact with?
**A**: Yes! Edit the `interactable_blocks` list in the config file. See [Configuration Guide](Configuration-Guide.md) for details.

#### Q: Can I disable the distance limit?
**A**: Yes! Set `max_distance = -1` in the config file.

#### Q: Can I allow players to go to the Nether/End?
**A**: Yes! Set `allow_dimension_travel = true` in the config file.

### Permission Questions

#### Q: How do I control who can use `/spectator`?
**A**: Use the `spectator_command_permission_level` setting in the config file (NeoForge only).

#### Q: Can I require OP status for spectator commands?
**A**: Yes! Set `require_op_for_spectator = true` in the config file.

#### Q: What are permission levels?
**A**: 
- 0 = Everyone
- 1 = Moderator
- 2 = Admin
- 3 = Operator
- 4 = Owner

#### Q: How do I give a player OP status?
**A**: Use `/op <player_name>` command in-game.

### Feature Questions

#### Q: Can players fly in spectator mode?
**A**: Yes! Players must double-press spacebar to activate flight.

#### Q: Can players take damage in spectator mode?
**A**: By default, no. Set `enable_invulnerability = false` to allow damage (except fall damage, which is always prevented by Minecraft).

#### Q: Can players attack other players?
**A**: By default, no. Set `allow_pvp = true` to enable PvP.

#### Q: Can players pick up items?
**A**: By default, no. Set `allow_item_pickup = true` to enable item pickup.

#### Q: Can players craft items?
**A**: By default, no. Set `allow_inventory_crafting = true` to enable crafting.

#### Q: Why can't players break/place blocks?
**A**: This is a Minecraft engine limitation. ADVENTURE mode (which Limited Spectator uses) blocks all building actions at the GameMode level. This cannot be changed.

#### Q: Why do players have to double-press spacebar to fly?
**A**: This is a Minecraft engine limitation. ADVENTURE mode requires manual flight activation. Auto-flying requires vanilla SPECTATOR mode, which enables noclip.

#### Q: Why is fall damage always prevented?
**A**: This is a Minecraft engine limitation. When `mayfly=true`, Minecraft's engine prevents fall damage calculation entirely. However, `enable_invulnerability=true` protects against other damage types (mobs, lava, fire, etc.).

### Loader-Specific Questions

#### Q: What's the difference between NeoForge, Fabric, and Quilt?
**A**: They're different mod loaders with different APIs. Limited Spectator supports all three with identical features.

#### Q: Why does Quilt say "Production Only"?
**A**: Quilt Loader has dependency issues in the development environment. The compiled JAR works perfectly in production Minecraft, but `.\gradlew.bat :quilt:runClient` doesn't work. See [Quilt Limitation](../CONTRIBUTING.md#quilt-development-environment-limitation) for details.

#### Q: Can I use Fabric and NeoForge together?
**A**: No. You must choose one mod loader per Minecraft installation.

#### Q: Which loader should I use?
**A**: All three are fully supported. Choose based on your preference or server requirements.

---

## 🐛 Troubleshooting

### Mod Not Loading

**Problem**: Mod doesn't appear in mod list

**Checklist**:
- [ ] JAR is in correct `mods/` folder
- [ ] Minecraft version is 1.21.1+
- [ ] Mod loader is installed correctly
- [ ] JAR filename matches your loader (e.g., `*-neoforge-*.jar` for NeoForge)

**Solutions**:

1. **Verify JAR location**
   ```bash
   # Windows
   dir %APPDATA%\.minecraft\mods\
   
   # Linux/macOS
   ls ~/.minecraft/mods/
   ```

2. **Check Minecraft version**
   - Launcher → Installation → Version
   - Must be 1.21.1 or higher

3. **Verify mod loader**
   - Launcher → Installation → Loader
   - Should show NeoForge, Fabric, or Quilt

4. **Check console logs**
   - Look for `[LimitedSpectator]` messages
   - Look for error messages

5. **Reinstall mod loader**
   - Download latest version
   - Run installer again
   - Place JAR in mods folder

### Commands Not Working

**Problem**: `/spectator` command not recognized

**Checklist**:
- [ ] Mod is loaded (check mod list)
- [ ] You have permission level 0+ (default)
- [ ] Config permissions allow your rank (if NeoForge)

**Solutions**:

1. **Verify mod loaded**
   - Open mod list (usually Mod Menu or similar)
   - Look for "Limited Spectator"
   - If not there, see [Mod Not Loading](#mod-not-loading)

2. **Check permission level**
   - Default is 0 (everyone)
   - If restricted, ask server admin for permission

3. **Check config (NeoForge only)**
   - Edit `config/limitedspectator-common.toml`
   - Verify `spectator_command_permission_level = 0`
   - Run `/reload` to apply changes

4. **Restart Minecraft**
   - Close and reopen Minecraft
   - Try command again

### Config File Not Generated

**Problem**: No `limitedspectator-common.toml` file

**Note**: This is **NeoForge only**. Fabric and Quilt don't generate config files.

**Solutions**:

1. **Create a world/server**
   - Launch Minecraft or server
   - Create new world or start server
   - Config should generate automatically

2. **Check folder permissions**
   - Ensure you have write permissions to `config/` folder
   - Try running Minecraft as administrator

3. **Manual creation**
   - Create `config/limitedspectator-common.toml` manually
   - Copy default config from [Configuration Guide](Configuration-Guide.md)

### Config Changes Not Applying

**Problem**: Changes to config don't take effect

**Solutions**:

1. **Save the file**
   - Ensure file is saved (Ctrl+S)
   - Close text editor

2. **Run `/reload` command**
   - In-game: `/reload`
   - Changes apply immediately

3. **Check for syntax errors**
   - TOML files are sensitive to formatting
   - Verify brackets and quotes are correct
   - Use online TOML validator if unsure

4. **Restart server/Minecraft**
   - If `/reload` doesn't work
   - Restart Minecraft or server
   - Config should load on startup

### Quilt Dev Environment Failing

**Problem**: `.\gradlew.bat :quilt:runClient` fails

**Error**: `syncTask` property not configured

**Solution**: This is a known limitation. Quilt works in production but not in dev environment.

**Workaround**:
```bash
# 1. Build the JAR
.\gradlew.bat :quilt:build

# 2. Copy to .minecraft/mods/
copy quilt\build\libs\LimitedSpectator-quilt-2.0.0.jar %APPDATA%\.minecraft\mods\

# 3. Launch Minecraft with Quilt Loader
# (Use official launcher with Quilt profile)
```

See [Contributing Guide](../CONTRIBUTING.md#quilt-development-environment-limitation) for details.

### Performance Issues

**Problem**: Game is slow or laggy

**Solutions**:

1. **Check mod list**
   - Ensure only necessary mods are loaded
   - Remove conflicting mods

2. **Increase RAM**
   - Launcher → Installation → JVM Arguments
   - Increase `-Xmx` value (e.g., `-Xmx4G` for 4GB)

3. **Check distance limit**
   - Large distance limits may cause lag
   - Try reducing `max_distance` in config

4. **Report issue**
   - If still slow, report on [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
   - Include mod list and system specs

### Multiplayer Sync Issues

**Problem**: Spectator mode not syncing between players

**Solutions**:

1. **Verify server has mod**
   - Check server logs for `[LimitedSpectator]`
   - Ensure JAR is in server `mods/` folder

2. **Check network**
   - Ensure stable connection
   - Try restarting server

3. **Report issue**
   - If still not working, report on [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
   - Include server logs

---

## 📞 Getting More Help

### Still Have Questions?

- **Check**: [Features Guide](Features.md) - What the mod can do
- **Read**: [Configuration Guide](Configuration-Guide.md) - All config options
- **See**: [Commands Reference](Commands.md) - Available commands
- **Ask**: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)

### Report Issues

- **Found a bug?** → [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Have a suggestion?** → [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Need help?** → [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)

Include:
- Minecraft version
- Mod loader and version
- Mod version
- Error messages or logs
- Steps to reproduce

---

**Last Updated**: 2026-01-23  
**Version**: 2.0.0
