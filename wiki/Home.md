# Limited Spectator Wiki

Welcome to the Limited Spectator documentation! This wiki contains comprehensive guides for users and developers.

## 🚀 Quick Start

### For Players

1. **Install**: Download from [Modrinth](https://modrinth.com/mod/limited-spectator) or [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/limited-spectator)
2. **Place**: Copy JAR to `mods/` folder
3. **Launch**: Start Minecraft
4. **Use**: Type `/spectator` to enter spectator mode

See [Installation Guide](Installation.md) for detailed steps.

### For Server Admins

1. **Install**: Place JAR in server `mods/` folder
2. **Configure**: Edit `config/limitedspectator-common.toml` (NeoForge only)
3. **Reload**: Use `/reload` command to apply changes
4. **Manage**: Use permission levels to control access

See [Server Admin Guide](For-Server-Admins.md) for details.

### For Developers

1. **Clone**: `git clone https://github.com/kalashnikxvxiii/Limited-Spectator.git`
2. **Build**: `.\gradlew.bat build`
3. **Develop**: `.\gradlew.bat :neoforge:runClient`

See [Contributing Guide](../CONTRIBUTING.md) for details.

## 📚 Documentation Index

### User Guides
- **[Installation Guide](Installation.md)** - How to install for all loaders
- **[Features Guide](Features.md)** - What Limited Spectator can do
- **[Commands Reference](Commands.md)** - Available commands
- **[Configuration Guide](Configuration-Guide.md)** - How to customize behavior
- **[FAQ & Troubleshooting](FAQ-and-Troubleshooting.md)** - Common questions and solutions

### Admin Guides
- **[Server Admin Guide](For-Server-Admins.md)** - Server setup and management
- **[Beta Features](Beta-Features.md)** - Experimental features

### Developer Guides
- **[Contributing Guide](../CONTRIBUTING.md)** - How to contribute code
- **[Developer Guide](../DEVELOPER.md)** - Development setup and architecture

## 🎯 Key Features

✅ **Multi-Loader Support**
- NeoForge (full support)
- Fabric (full support)
- Quilt (production ready)

✅ **Fully Configurable**
- Distance limits
- Block interactions
- Permission levels
- HUD behavior

✅ **Server-Friendly**
- All restrictions server-side
- Hot-reloadable config
- Permission system

✅ **Cross-Version Compatible**
- Minecraft 1.21.1 → 1.21.11+
- Works on all 1.21.x versions

## 📋 Mod Information

| Property | Value |
|----------|-------|
| **Version** | 2.0.0 |
| **Minecraft** | 1.21.1+ |
| **Loaders** | NeoForge • Fabric • Quilt |
| **License** | MIT |
| **Author** | Karashi |

## 🔗 Links

- **GitHub**: [Limited Spectator Repository](https://github.com/kalashnikxvxiii/Limited-Spectator)
- **Modrinth**: [Limited Spectator on Modrinth](https://modrinth.com/mod/limited-spectator)
- **CurseForge**: [Limited Spectator on CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/limited-spectator)
- **Issues**: [Report Bugs](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Discussions**: [Ask Questions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)

## ⚠️ Important Notes

### Quilt Development Limitation

Quilt works perfectly in **production** but has issues in the **development environment**:

- ✅ Build: `.\gradlew.bat :quilt:build` works
- ✅ Production: JAR works perfectly in Minecraft
- ❌ Dev: `.\gradlew.bat :quilt:runClient` not supported

**Workaround**: Use Fabric for development (identical code), or test Quilt JAR in production Minecraft.

See [Contributing Guide](../CONTRIBUTING.md#quilt-is-legacy) for details.

### Minecraft Engine Limitations

These features are **not possible** due to Minecraft's core behavior:

- **Fall Damage**: Always prevented when flying
- **Block Breaking/Placing**: Always blocked in ADVENTURE mode
- **Auto-Start Flying**: Players must double-press spacebar
- **Mob Attacks**: Always blocked (mobs don't target flying players)

These are **not bugs** - they are fundamental Minecraft limitations.

## 🆘 Getting Help

### Common Issues

- **Mod not loading?** → See [Installation Guide](Installation.md#troubleshooting)
- **Commands not working?** → See [FAQ](FAQ-and-Troubleshooting.md)
- **Config not applying?** → See [Configuration Guide](Configuration-Guide.md)
- **Quilt dev environment failing?** → See [Contributing Guide](../CONTRIBUTING.md#quilt-is-legacy)

### Contact

- **Report Bugs**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Ask Questions**: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)
- **Suggest Features**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)

## 📖 Navigation

**Start Here:**
- New to Limited Spectator? → [Installation Guide](Installation.md)
- Want to know what it does? → [Features Guide](Features.md)
- Running a server? → [Server Admin Guide](For-Server-Admins.md)
- Want to contribute? → [Contributing Guide](../CONTRIBUTING.md)

**Specific Topics:**
- How do I use commands? → [Commands Reference](Commands.md)
- How do I configure it? → [Configuration Guide](Configuration-Guide.md)
- I have a question → [FAQ](FAQ-and-Troubleshooting.md)
- I found a bug → [Report Issue](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)

---

**Last Updated**: 2026-01-23  
**Version**: 2.0.0
