# Installation Guide

This guide will walk you through installing Limited Spectator on your Minecraft client or server.

## Which Version Should I Download?

### 🟢 Stable v1.1.1 (Recommended)

**Best for**: All servers and users

**Download**: [LimitedSpectator-1.21.1-1.1.1.jar](../../releases/tag/v1.1.1)

**Features**:
- ✅ Fully tested and stable
- ✅ Full configuration system (20+ options)
- ✅ Configurable distance, permissions, interactions
- ✅ Generates `config/limitedspectator-common.toml`
- ✅ Hot-reload support via `/reload` command
- ✅ No known bugs

### 📦 Previous Versions

For legacy servers, older versions remain available:
- [v1.0.2 (Previous Stable)](../../releases/tag/v1.0.2) - Hardcoded settings, no config file
- [v1.1.0-beta (Superseded)](../../releases/tag/v1.1.0-beta) - Beta version superseded by v1.1.1

---

## System Requirements

Before installing, ensure your system meets these requirements:

| Component | Requirement |
|-----------|-------------|
| **Minecraft** | 1.21.1 |
| **Mod Loader** | NeoForge 21.1.213+ |
| **Java** | Java 21 or higher |
| **Operating System** | Windows, Linux, macOS |

## Download Links

### Official Releases

- **[GitHub Releases](../../releases)** - All versions available
  - [v1.1.1 (Current Stable)](../../releases/tag/v1.1.1)
  - [v1.0.2 (Previous Stable)](../../releases/tag/v1.0.2)
  - [v1.1.0-beta (Superseded)](../../releases/tag/v1.1.0-beta)
- **CurseForge** - *(coming soon)*
- **Modrinth** - *(coming soon)*

## Installation Methods

### 🖥️ Server Installation (Recommended)

Limited Spectator is primarily a **server-side mod**, which means it works best when installed on the server.

#### Step 1: Install NeoForge on Server

1. Download **NeoForge 21.1.213** or higher from [NeoForge Downloads](https://neoforged.net/)
2. Run the installer: `java -jar neoforge-installer.jar --installServer`
3. This creates a `run.sh` (Linux/Mac) or `run.bat` (Windows) file

#### Step 2: Add Limited Spectator

1. Locate your server's `mods/` folder
   - If it doesn't exist, create it in the server root directory
2. Copy `LimitedSpectator-1.21.1-1.1.1.jar` into the `mods/` folder
3. Start the server using `./run.sh` (Linux/Mac) or `run.bat` (Windows)

#### Step 3: First-Time Configuration

When the server starts for the first time:

1. The mod will generate `config/limitedspectator-common.toml`
2. Stop the server
3. Edit `config/limitedspectator-common.toml` to customize settings (see [Configuration Guide](Configuration-Guide))
4. Restart the server or use `/reload` command

### 💻 Client Installation (Optional)

Client installation is **optional** but recommended for enhanced features like automatic HUD hiding.

#### Step 1: Install NeoForge on Client

1. Download **NeoForge 21.1.213** or higher from [NeoForge Downloads](https://neoforged.net/)
2. Run the installer and select "Install Client"
3. Open the Minecraft Launcher and select the NeoForge profile

#### Step 2: Add Limited Spectator

1. Open your Minecraft folder:
   - **Windows**: `%APPDATA%\.minecraft`
   - **Linux**: `~/.minecraft`
   - **macOS**: `~/Library/Application Support/minecraft`
2. Navigate to the `mods/` folder (create it if it doesn't exist)
3. Copy `LimitedSpectator-1.21.1-1.1.1.jar` into the `mods/` folder
4. Launch Minecraft with the NeoForge profile

### 🔧 Single-Player Installation

For single-player or LAN worlds:

1. Follow the **Client Installation** steps above
2. Configuration file will be at: `run/config/limitedspectator-common.toml`

## Verifying Installation

### On Server

Check the server console during startup. You should see:

```
[INFO] [LimitedSpectator] Limited Spectator mod loaded successfully!
```

You can also run `/spectator help` in-game to verify commands are available.

### On Client

1. Launch Minecraft
2. Go to "Mods" menu from the main screen
3. Look for "Limited Spectator" in the mod list
4. Version should show: `1.21.1-1.1.1`

## Post-Installation Setup

### 1. Configure Permissions

Edit `config/limitedspectator-common.toml`:

```toml
[commands]
  # Permission level required for /spectator command (0-4)
  # 0 = all players, 2 = operators, 4 = server console only
  spectator_command_permission_level = 0

  # Require operator status for spectator commands
  require_op_for_spectator = false
```

### 2. Set Distance Limits

```toml
[movement]
  # Maximum distance from starting position (-1 = unlimited)
  max_distance = 75.0

  # Teleport back when exceeding distance, or just warn
  teleport_back_on_exceed = true
```

### 3. Test the Installation

1. Join the server
2. Run `/spectator` to enter spectator mode
3. Run `/survival` to return to survival mode
4. Verify restrictions are working (distance limits, interactions, etc.)

## Common Installation Issues

### "Mod not loading" or "Missing dependencies"

**Solution**: Ensure you're using NeoForge 21.1.213 or higher, not Forge. Limited Spectator is **NeoForge-only**.

### "Config file not generating"

**Solution**: The config generates on first server start. Make sure:
1. The mod JAR is in the `mods/` folder
2. The server started successfully without crashes
3. Check `logs/latest.log` for errors

### "Commands not available"

**Solution**:
1. Verify the mod loaded by checking server logs
2. Check if you have permission to use commands (see [Commands](Commands))
3. Ensure you're on a server with the mod installed

### "Client crashes when joining server"

**Solution**: Limited Spectator is server-side. If the server has the mod but your client doesn't, you can still join. If your client has the mod but the server doesn't, remove it from your client `mods/` folder.

## Updating the Mod

To update to a newer version:

1. Stop the server/client
2. Remove the old JAR from `mods/` folder
3. Download the new version
4. Place the new JAR in `mods/` folder
5. Check [Changelog](../../blob/main/CHANGELOG.md) for breaking changes
6. Update `config/limitedspectator-common.toml` if needed
7. Restart the server/client

**Note**: Always backup your config file before updating!

## Uninstallation

To remove Limited Spectator:

1. Stop the server/client
2. Delete `mods/LimitedSpectator-1.21.1-1.1.1.jar`
3. Optionally delete `config/limitedspectator-common.toml`
4. Restart the server/client

---

**Next Steps**: Check out the [Configuration Guide](Configuration-Guide) to customize Limited Spectator for your needs!
