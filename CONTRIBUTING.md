# Contributing to Limited Spectator

Thank you for your interest in contributing to Limited Spectator! This document provides guidelines and information for contributors.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Submitting Changes](#submitting-changes)
- [Known Issues](#known-issues)

## 🤝 Code of Conduct

- Be respectful and constructive in all interactions
- Welcome newcomers and help them get started
- Focus on what is best for the community
- Show empathy towards other community members

## 🛠️ How to Contribute

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates.

**When reporting bugs, include:**
- Minecraft version
- NeoForge version
- Mod version
- Steps to reproduce
- Expected behavior
- Actual behavior
- Relevant logs (with `[LimitedSpectator]` prefix)
- Configuration file (`limitedspectator-common.toml`)

### Suggesting Features

Feature requests are welcome! Please provide:
- Clear description of the feature
- Use cases and examples
- How it fits with existing functionality
- Potential implementation approach (optional)

### Contributing Code

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following our coding standards
4. Test thoroughly (see Testing Guidelines)
5. Commit with clear messages
6. Push to your fork
7. Open a Pull Request

## 💻 Development Setup

### Prerequisites

- **Java 21** (JDK 21 or higher)
- **Gradle 8.10+** (wrapper included)
- **Git**
- **IDE**: IntelliJ IDEA (recommended) or Eclipse

### Initial Setup

```bash
# Clone the repository
git clone https://github.com/kalashnikxvxiii/Limited-Spectator.git
cd Limited-Spectator

# Build the project
./gradlew build

# Run development client
./gradlew runClient

# Run development server
./gradlew runServer
```

### Project Structure

```
Limited-Spectator/
├── src/main/java/com/karashi/limitedspectator/
│   ├── SpectatorMod.java          # Main mod class, server-side logic
│   ├── ModConfig.java              # Configuration system
│   ├── client/
│   │   └── ClientEventHandler.java # Client-side events, HUD management
│   └── network/
│       ├── NetworkHandler.java     # Packet registration
│       └── SpectatorHudPacket.java # HUD state packet
├── src/main/resources/
│   ├── META-INF/neoforge.mods.toml # Mod metadata
│   ├── assets/limitedspectator/    # Mod assets
│   └── lang/                       # (Future) Translation files
└── build.gradle                    # Build configuration
```

## 📝 Coding Standards

### Java Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: Maximum 120 characters
- **Naming conventions**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

### Code Quality

- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and concise
- Avoid code duplication
- Handle exceptions properly

### Example Code

```java
// Good: Clear method name, proper error handling
@SubscribeEvent
public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;

    if (inSpectatorMode.getOrDefault(player.getUUID(), false)) {
        if (!ModConfig.allowBlockInteractions) {
            event.setCanceled(true);
            LOGGER.debug("Blocked interaction for player: {}", player.getName().getString());
        }
    }
}

// Bad: Unclear naming, no error handling
@SubscribeEvent
public void onClick(PlayerInteractEvent.RightClickBlock e) {
    ServerPlayer p = (ServerPlayer)e.getEntity();
    if (map.get(p.getUUID())) e.setCanceled(true);
}
```

### Configuration

When adding new config options:
1. Add to `ModConfig.java` with proper documentation
2. Add cached static variable for performance
3. Update `onLoad()` method to cache the value
4. Use the cached value in event handlers
5. Document in `README.md`

## 🧪 Testing Guidelines

### Required Tests

Before submitting a PR, test the following scenarios:

#### Single-Player Testing
- [ ] Enter/exit spectator mode with `/spectator` and `/survival`
- [ ] Distance limit enforcement (both teleport back and boundary stop)
- [ ] Dimension travel restrictions
- [ ] Block interaction whitelist
- [ ] Item drop/pickup restrictions
- [ ] PvP/mob attack blocking
- [ ] HUD hiding/showing with F1
- [ ] Configuration hot-reload with `/reload`

#### Multiplayer Testing (if applicable)
- [ ] Multiple players in spectator mode simultaneously
- [ ] Permission levels work correctly
- [ ] Server restart preserves correct state
- [ ] Network packet synchronization

#### Configuration Testing
Test with various config combinations:
- [ ] `max_distance = -1` (disabled)
- [ ] `allow_dimension_travel = true`
- [ ] `enable_invulnerability = false` (other damage types only, fall damage always prevented)
- [ ] Custom `interactable_blocks` list
- [ ] `allow_inventory_crafting = true`

### Testing Checklist

```markdown
**Test Environment:**
- Minecraft: 1.21.1
- NeoForge: 21.1.217
- Mod Version: [your version]

**Tests Performed:**
- [ ] Commands work correctly
- [ ] Config changes apply properly
- [ ] No console errors/warnings
- [ ] Performance is acceptable
- [ ] Multiplayer sync works (if applicable)

**Notes:**
[Any observations or issues]
```

## 📤 Submitting Changes

### Pull Request Process

1. **Update documentation**
   - Update `CHANGELOG.md` with your changes
   - Update `README.md` if adding features

2. **Write clear commit messages**
   ```
   feat: Add configurable spectator time limits

   - Add config option for max spectator duration
   - Implement timer system with warnings
   - Add /spectator time command for admins

   Closes #123
   ```

3. **Keep PRs focused**
   - One feature/fix per PR
   - Avoid unrelated changes
   - Rebase on latest main before submitting

4. **PR Description Template**
   ```markdown
   ## Description
   Brief description of changes

   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Breaking change
   - [ ] Documentation update

   ## Testing
   - [ ] Tested in single-player
   - [ ] Tested in multiplayer
   - [ ] Tested config changes

   ## Checklist
   - [ ] Code follows style guidelines
   - [ ] Documentation updated
   - [ ] CHANGELOG.md updated
   - [ ] No new warnings
   ```

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples:**
```
feat(config): Add permission system integration

fix(teleport): Correctly restore dimension on /survival
docs(readme): Update installation instructions
```

## 🐛 Known Limitations (Resolved)

The following were initially considered "issues" but are now documented as **Minecraft engine limitations** (not bugs):

### Minecraft Core Behavior (Cannot Be Changed)
1. **Fall Damage**: Always prevented when `mayfly=true` - Minecraft engine behavior. Config option `enable_invulnerability` retained for other damage types (mobs, lava, fire, etc.).

2. **Auto-Start Flying**: Players must double-tap spacebar - ADVENTURE mode limitation. Config option removed.

3. **Block Breaking/Placing**: Always blocked in ADVENTURE mode at GameMode level. Config options removed.

4. **HUD Behavior**: Hard-coded to auto-hide with F1 toggle for consistent UX. Config options removed.

5. **Mob Attacks**: Always blocked (mobs don't target `mayfly` players anyway). Config option removed.

### Areas for Contribution
- Cross-dimension spectator state persistence across server restarts
- Integration with third-party permission plugins (LuckPerms, FTB Chunks)
- Spectator time limits (max duration in spectator mode)
- Particle effects or visual boundaries for distance limits

## 📚 Resources

- [NeoForge Documentation](https://docs.neoforged.net/)
- [Minecraft Forge Forums](https://forums.minecraftforge.net/)
- [NeoForge Discord](https://discord.neoforged.net/)

## 📞 Contact

- **Issues**: [GitHub Issues](https://github.com/kalashnikxvxiii/Limited-Spectator/issues)
- **Discussions**: [GitHub Discussions](https://github.com/kalashnikxvxiii/Limited-Spectator/discussions)

## 📜 License

By contributing to Limited Spectator, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Limited Spectator! 🎉
