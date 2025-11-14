# Limited Spectator Wiki

Welcome to the **Limited Spectator** wiki! This mod provides a restricted spectator mode for Minecraft 1.21.1 servers running NeoForge, allowing players to fly and observe the world while maintaining strict gameplay balance.

## ⚠️ Version Notice

This wiki documents **two versions** of Limited Spectator:

### 🟢 **Stable Version: 1.0.2** (Recommended)
- **Status**: Production-ready
- **Features**: Core spectator functionality with hardcoded settings
- **Stability**: Fully tested, no known critical bugs
- **Best for**: Production servers, users wanting stability

### 🟡 **Beta Version: 1.1.0-beta** (Testing)
- **Status**: Beta testing
- **Features**: Complete configuration system with 25+ configurable options
- **Stability**: Some features have known issues (see [Beta Features](Beta-Features))
- **Best for**: Testing environments, advanced users

**⚡ Quick Recommendation**: Use **v1.0.2** for production servers. Try **v1.1.0-beta** on test servers.

---

## 🎯 What is Limited Spectator?

Limited Spectator is a server-side mod that implements a controlled spectator mode. Unlike vanilla spectator mode, it prevents players from:
- Traveling unlimited distances
- Changing dimensions
- Interacting with most blocks and entities
- Dropping or picking up items
- Bypassing server rules

Perfect for server admins who want to allow temporary observation without compromising gameplay integrity.

## ✨ Key Features (Stable v1.0.2)

- **Distance Limits**: 75 block maximum travel distance from entry point
- **Dimension Locking**: Prevents travel to Nether, End, or custom dimensions
- **Interaction Control**: Can only interact with doors, trapdoors, and fence gates
- **Auto HUD Hiding**: Cleaner spectator experience with F1 toggle support
- **Item Protection**: Cannot drop or pick up items
- **PvP Prevention**: Cannot attack players or mobs
- **Security Hardened**: Built with updated dependencies (Netty 4.1.118, Commons Lang3 3.18.0)

## 📚 Documentation

### For Stable Version (v1.0.2)

| Page | Description |
|------|-------------|
| **[Installation](Installation)** | How to install the mod on client and server |
| **[Commands](Commands)** | Command reference for `/spectator` and `/survival` |
| **[Features](Features)** | In-depth feature documentation |
| **[FAQ & Troubleshooting](FAQ-and-Troubleshooting)** | Common issues and solutions |
| **[For Server Admins](For-Server-Admins)** | Best practices for server deployment |

### For Beta Version (v1.1.0-beta)

| Page | Description |
|------|-------------|
| **[Beta Features](Beta-Features)** | New features in 1.1.0-beta + known issues |
| **[Configuration Guide](Configuration-Guide)** | Complete configuration system (beta only) |
| **[Version Comparison](Version-Comparison)** | Detailed comparison between stable and beta |

## 🚀 Quick Start (Stable v1.0.2)

1. Download **v1.0.2** from [GitHub Releases](../../releases/tag/v1.0.2)
2. Place `LimitedSpectator-1.21.1-1.0.2.jar` in your `mods/` folder
3. Start the server
4. Use `/spectator` to enter spectator mode, `/survival` to exit
5. No configuration needed - everything works out of the box!

## 🚀 Quick Start (Beta v1.1.0-beta)

1. Download **v1.1.0-beta** from [GitHub Releases](../../releases/tag/v1.1.0-beta)
2. Place `LimitedSpectator-1.21.1-1.1.0-beta.jar` in your `mods/` folder
3. Start the server to generate `config/limitedspectator-common.toml`
4. Customize the config as needed (see [Configuration Guide](Configuration-Guide))
5. Use `/spectator` to enter spectator mode, `/survival` to exit
6. **Read [Beta Features](Beta-Features) for known issues!**

## 🔧 Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.213 or higher
- **Java**: 21 or higher
- **Side**: Server-required, client-optional (for HUD features)

## 📖 Version Information

| Version | Release Date | Status | Download |
|---------|--------------|--------|----------|
| **1.0.2** | 2025-11-08 | ✅ Stable | [Download](../../releases/tag/v1.0.2) |
| **1.1.0-beta** | 2025-11-09 | ⚠️ Beta | [Download](../../releases/tag/v1.1.0-beta) |

**Mod ID**: `limitedspectator`
**Package**: `com.karashi.limitedspectator`

## 🔗 Links

- [GitHub Repository](../../)
- [Issue Tracker](../../issues)
- [Changelog](../../blob/main/CHANGELOG.md)

## 📝 License

This mod is distributed under the terms specified in the [LICENSE](../../blob/main/LICENSE) file.

---

**Need help?** Check the [FAQ & Troubleshooting](FAQ-and-Troubleshooting) page or [open an issue](../../issues/new)!
