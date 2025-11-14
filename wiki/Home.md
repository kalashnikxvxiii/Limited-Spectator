# Limited Spectator Wiki

Welcome to the **Limited Spectator** wiki! This mod provides a restricted spectator mode for Minecraft 1.21.1 servers running NeoForge, allowing players to fly and observe the world while maintaining strict gameplay balance.

## ⚠️ Version Notice

**Current Stable Version: 1.1.1**

Limited Spectator v1.1.1 is the latest stable release with full configuration support. Previous versions (1.0.2, 1.1.0-beta) are available for legacy servers but are superseded by this release.

---

## 🎯 What is Limited Spectator?

Limited Spectator is a server-side mod that implements a controlled spectator mode. Unlike vanilla spectator mode, it prevents players from:
- Traveling unlimited distances
- Changing dimensions
- Interacting with most blocks and entities
- Dropping or picking up items
- Bypassing server rules

Perfect for server admins who want to allow temporary observation without compromising gameplay integrity.

## ✨ Key Features

- **Fully Configurable**: 20+ configuration options for distance limits, permissions, and interactions
- **Distance Limits**: Configurable maximum travel distance (default: 75 blocks, can be disabled)
- **Dimension Locking**: Optional dimension travel restrictions
- **Interaction Control**: Customizable block whitelist system
- **Auto HUD Hiding**: Cleaner spectator experience with F1 toggle support
- **Item Protection**: Configurable item drop/pickup restrictions
- **PvP Prevention**: Optional PvP blocking
- **Hot-Reload**: Configuration changes apply via `/reload` command
- **Security Hardened**: Built with updated dependencies (Netty 4.1.118, Commons Lang3 3.18.0)

## 📚 Documentation

| Page | Description |
|------|-------------|
| **[Installation](Installation)** | How to install the mod on client and server |
| **[Commands](Commands)** | Command reference for `/spectator` and `/survival` |
| **[Features](Features)** | In-depth feature documentation |
| **[Configuration Guide](Configuration-Guide)** | Complete configuration system reference |
| **[FAQ & Troubleshooting](FAQ-and-Troubleshooting)** | Common issues and solutions |
| **[For Server Admins](For-Server-Admins)** | Best practices for server deployment |
| **[Version Comparison](Version-Comparison)** | Comparison between v1.1.1, v1.0.2, and v1.1.0-beta |

## 🚀 Quick Start

1. Download **v1.1.1** from [GitHub Releases](../../releases/tag/v1.1.1)
2. Place `LimitedSpectator-1.21.1-1.1.1.jar` in your `mods/` folder
3. Start the server to generate `config/limitedspectator-common.toml`
4. Customize the config as needed (see [Configuration Guide](Configuration-Guide))
5. Use `/spectator` to enter spectator mode, `/survival` to exit
6. Changes can be applied with `/reload` command

## 🔧 Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.213 or higher
- **Java**: 21 or higher
- **Side**: Server-required, client-optional (for HUD features)

## 📖 Version Information

| Version | Release Date | Status | Download |
|---------|--------------|--------|----------|
| **1.1.1** | 2025-11-14 | ✅ Stable (Current) | [Download](../../releases/tag/v1.1.1) |
| **1.1.0-beta** | 2025-11-09 | ⚠️ Superseded | [Download](../../releases/tag/v1.1.0-beta) |
| **1.0.2** | 2025-11-08 | 📦 Legacy | [Download](../../releases/tag/v1.0.2) |

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
