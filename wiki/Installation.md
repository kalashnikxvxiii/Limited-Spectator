# Installation Guide

## 📦 Choose Your Mod Loader

Limited Spectator supports **three mod loaders** with full feature parity:

| Loader | JAR File | Status | Notes |
|--------|----------|--------|-------|
| **NeoForge** | `LimitedSpectator-neoforge-2.0.0.jar` | ✅ Full Support | Complete config system |
| **Fabric** | `LimitedSpectator-fabric-2.0.0.jar` | ✅ Full Support | Hardcoded defaults |
| **Quilt** | `LimitedSpectator-quilt-2.0.0.jar` | ✅ Production Ready | Uses Fabric API |

## 🔧 System Requirements

- **Minecraft**: 1.21.1 or higher (tested up to 1.21.11)
- **Java**: 21 or higher
- **RAM**: 2GB minimum (4GB+ recommended)

## 📥 Installation Steps

### Step 1: Install Your Mod Loader

Choose ONE of the following:

#### NeoForge
1. Download [NeoForge 21.1.217+](https://neoforged.net/)
2. Run the installer
3. Select "Install Client" or "Install Server"
4. Choose your Minecraft installation

#### Fabric
1. Download [Fabric Loader 0.16.5+](https://fabricmc.net/use/installer/)
2. Run the installer
3. Select "Install Client" or "Install Server"
4. Choose your Minecraft installation

#### Quilt
1. Download [Quilt Loader 0.26.4+](https://quiltmc.org/en/install/client/)
2. Run the installer
3. Select "Install Client" or "Install Server"
4. Choose your Minecraft installation

### Step 2: Download Limited Spectator

Download the correct JAR for your loader from:
- **Modrinth**: [Limited Spectator](https://modrinth.com/mod/limited-spectator)
- **CurseForge**: [Limited Spectator](https://legacy.curseforge.com/minecraft/mc-mods/limited-spectator)
- **GitHub Releases**: [GitHub](https://github.com/kalashnikxvxiii/Limited-Spectator/releases)

### Step 3: Place JAR in Mods Folder

1. Locate your Minecraft installation folder:
   - **Windows**: `%APPDATA%\.minecraft`
   - **macOS**: `~/Library/Application Support/minecraft`
   - **Linux**: `~/.minecraft`

2. Navigate to the `mods` folder (create if it doesn't exist)

3. Copy the downloaded JAR file into the `mods` folder

### Step 4: Launch Minecraft

1. Open Minecraft Launcher
2. Select your mod loader profile
3. Click "Play"
4. Wait for Minecraft to load

### Step 5: Verify Installation

1. Launch a world (single-player or server)
2. Open chat and type: `/spectator`
3. You should enter spectator mode
4. Type `/survival` to exit

If you see messages like "You are now in spectator mode", the mod is working!

## ⚙️ Configuration (NeoForge Only)

After first launch, a config file is automatically created at:

**Location**: `config/limitedspectator-common.toml`

### Quick Configuration

Edit the file to customize:
- Distance limits
- Allowed interactions
- Permission levels
- HUD behavior

See [Configuration Guide](Configuration-Guide.md) for detailed options.

### Applying Changes

Changes are applied automatically when you:
1. Edit the config file and save
2. Run `/reload` command in-game

No server restart needed!

## 🖥️ Server Installation

### Single-Player World
Follow the same steps as above. Config file appears in your world's `config` folder.

### Multiplayer Server

1. **Stop the server** (if running)

2. **Install mod loader** on the server:
   ```bash
   # For NeoForge
   java -jar neoforge-installer.jar --installServer
   
   # For Fabric
   java -jar fabric-installer.jar server
   
   # For Quilt
   java -jar quilt-installer.jar install server
   ```

3. **Place JAR in mods folder**:
   ```bash
   cp LimitedSpectator-*.jar server/mods/
   ```

4. **Start the server**:
   ```bash
   ./start.sh  # Linux/macOS
   # or
   start.bat   # Windows
   ```

5. **Configure** (NeoForge only):
   - Edit `config/limitedspectator-common.toml`
   - Run `/reload` to apply changes

## 🐛 Troubleshooting

### Mod Not Loading

**Problem**: Mod doesn't appear in mod list

**Solutions**:
1. Verify JAR is in correct `mods` folder
2. Check Minecraft version matches (1.21.1+)
3. Verify mod loader is installed correctly
4. Check console for error messages

### Commands Not Working

**Problem**: `/spectator` command not recognized

**Solutions**:
1. Verify mod loaded (check mod list)
2. Ensure you have permission level 0+ (default)
3. Check config file for permission settings
4. Restart Minecraft

### Config File Not Generated

**Problem**: No `limitedspectator-common.toml` file

**Solutions**:
1. This is **NeoForge only** - Fabric/Quilt use hardcoded defaults
2. Create world/server and launch
3. Config should generate automatically
4. If not, check folder permissions

### Quilt Not Working in Dev Environment

**Problem**: `.\gradlew.bat :quilt:runClient` fails

**Solution**: This is a known limitation. Quilt works in production but not in dev environment.

**Workaround**:
1. Build the JAR: `.\gradlew.bat :quilt:build`
2. Copy to `.minecraft/mods/`
3. Launch Minecraft with Quilt Loader
4. Test the production JAR

See [Contributing Guide](../CONTRIBUTING.md#quilt-development-environment-limitation) for details.

## 📋 Compatibility

### Minecraft Versions
- ✅ 1.21.1 (baseline)
- ✅ 1.21.10
- ✅ 1.21.11+
- ✅ All 1.21.x versions

### Mod Loaders
- ✅ NeoForge 21.1.217+
- ✅ Fabric Loader 0.16.5+
- ✅ Quilt Loader 0.26.4+

### Other Mods
- ✅ Generally compatible with most mods
- ⚠️ May conflict with mods that heavily modify:
  - Player gamemode handling
  - HUD rendering
  - Permission systems

## 🆘 Getting Help

If you encounter issues:

1. **Check logs**: Look for `[LimitedSpectator]` messages
2. **Report bug**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
3. **Ask questions**: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)

Include:
- Minecraft version
- Mod loader and version
- Mod version
- Error messages
- Steps to reproduce

## ✅ Next Steps

After installation:
1. Read [Features Guide](Features.md) to learn what you can do
2. Check [Configuration Guide](Configuration-Guide.md) to customize behavior
3. See [Commands](Commands.md) for available commands
4. Review [FAQ](FAQ-and-Troubleshooting.md) for common questions
