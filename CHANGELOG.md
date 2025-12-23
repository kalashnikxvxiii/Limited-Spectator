# Changelog

All notable changes to the Limited Spectator mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).



## [1.1.2] - 2025-12-23

### 🔒 Security Release

This release addresses critical security vulnerabilities in transitive dependencies and expands version compatibility.

### Security
- **Updated Netty to 4.1.125.Final** - Fixes CVE-2025-58057 (BrotliDecoder DoS vulnerability, CVSS 7.5)
  - Previous: 4.1.118.Final (CVE-2025-24970)
  - Impact: Prevents denial of service attacks via crafted compressed input
- **Updated Log4j Core to 2.25.3** - Fixes CVE-2025-68161 (TLS hostname verification, CVSS 5.4)
  - New dependency forcing for both log4j-core and log4j-api
  - Impact: Prevents man-in-the-middle attacks on log traffic
- **Updated LZ4-Java to 1.10.1** - Fixes CVE-2025-66566 (buffer disclosure vulnerability, CVSS 7.5)
  - Migrated from org.lz4:lz4-java to at.yawk.lz4:lz4-java (new official group ID)
  - Previous: 1.8.0
  - Impact: Prevents sensitive data disclosure via output buffer reuse
- **Maintained Commons Lang3 3.18.0** - Continues protection against CVE-2025-48924 (CVSS 5.3)

### Changed
- **Version scheme updated from 1.21.1-1.1.1 to 1.21.x-1.1.2**
  - Reflects compatibility with all Minecraft 1.21.x versions (1.21.1, 1.21.2, ... 1.21.11+)
  - Dependency range already configured as [1.21.1,) in neoforge.mods.toml
- **Documentation updates**:
  - Updated README.md to clarify Minecraft 1.21.1+ compatibility
  - Updated build output JAR name to LimitedSpectator-1.21.x-1.1.2.jar

### Technical
- All dependency version forcing configured via build.gradle resolutionStrategy
- Build tested successfully with clean build (no warnings or errors)
- JAR output: `build/libs/LimitedSpectator-1.21.x-1.1.2.jar`
- Gradle configuration remains at 8.10 with NeoGradle 7.0.167

### Migration Notes
Users on any 1.21.x Minecraft version can safely upgrade to this release. No configuration changes required.
All existing configs from 1.1.1 remain fully compatible.

---

## [1.1.1] - 2025-11-14

### 🎯 Stable Release

This is the **stable release** following 1.1.0-beta, with cleaned configuration and comprehensive documentation updates.

### Changed
- **Configuration Cleanup**: Removed non-functional config options that were limited by Minecraft engine behavior:
  - Removed `allow_mob_attacks` - mob attacks always blocked (mobs don't target players with `mayfly=true`)
  - Removed `allow_block_breaking` - always blocked in ADVENTURE mode (GameMode restriction)
  - Removed `allow_block_placing` - always blocked in ADVENTURE mode (GameMode restriction)
  - Removed `auto_hide_hud` - behavior hard-coded to always hide (can toggle with F1)
  - Removed `allow_f1_hud_toggle` - F1 toggle always enabled
  - Removed `auto_start_flying` - players must double-tap spacebar (ADVENTURE mode limitation)

- **Documentation Overhaul**: Complete update of all documentation to reflect actual behavior:
  - Clarified that `enable_invulnerability` does NOT prevent fall damage (Minecraft engine always prevents it with `mayfly=true`)
  - Updated `enable_invulnerability` description to specify it protects against mobs, lava, fire, cacti, drowning, etc.
  - Documented HUD behavior as hard-coded (always hides, F1 toggles)
  - Documented that players must double-tap spacebar to fly (ADVENTURE mode behavior)
  - Renamed "Known Issues" sections to "Known Limitations" with proper explanations

### Fixed
- Configuration file now generates cleanly without obsolete options
- All wiki documentation updated (Configuration-Guide, For-Server-Admins, Beta-Features, Features, Commands, FAQ)
- Updated README.md, CHANGELOG.md, Version-Comparison.md, CONTRIBUTING.md
- Removed confusing config options that appeared functional but weren't due to Minecraft limitations

### Technical
- Cleaned up `ModConfig.java` - removed unused config variables and cached values
- Updated `SpectatorMod.java` - hard-coded HUD hide behavior
- Updated `ClientEventHandler.java` - simplified HUD management (removed config checks)
- Generated config file (`limitedspectator-common.toml`) now accurate and contains only functional options

### Migration Notes
If upgrading from 1.1.0-beta, your existing config will continue to work. The removed options will be ignored.
No action required - the mod will use correct behavior regardless of old config values.

---

## [1.1.0-beta] - 2025-11-09

### ⚠️ Beta Release Notice

This is a **beta release** focused on features that work reliably within Minecraft's ADVENTURE mode limitations. Features incompatible with ADVENTURE mode have been removed to ensure stability and prevent confusion.

### Added
- **Complete Configuration System** - Comprehensive TOML-based configuration file (`limitedspectator-common.toml`)
  - **Movement Restrictions**: Configure max distance (-1 to disable), dimension travel, teleport behavior, logout position reset
  - **Player Abilities**: Toggle flight and choose between ADVENTURE/SPECTATOR gamemode
  - **Interaction Controls**: Individually toggle PvP, mob attacks, item drop/pickup, and inventory crafting
  - **Customizable Block Whitelist**: Define exactly which blocks are interactable via Minecraft block IDs
  - **Permission System**: Set required permission levels (0-4) for `/spectator` and `/survival` commands
  - **Client/HUD Settings**: Configure auto-hide HUD and F1 toggle functionality
  - **Message Settings**: Choose action bar vs chat messages, enable/disable distance warnings
- **Inventory Crafting Control** - Block 2x2 crafting grid in player inventory with automatic ingredient restoration
  - Configurable via `allow_inventory_crafting` (default: false)
  - When blocked, ingredients are automatically returned to player's inventory
  - Prevents item loss or duplication during crafting attempts
  - Works with both single-slot and full 2x2 grid recipes
  - Falls back to dropping items on ground if inventory is full
- Configuration validation with sensible defaults and range checking
- Hot-reload support for configuration changes via `/reload` command
- Performance-optimized config value caching
- **CONTRIBUTING.md** - Comprehensive contributor guide with coding standards and known issues

### Fixed
- **Critical**: `/survival` command now correctly teleports players back to original dimension (Overworld/Nether/End)
- **Critical**: Dimension tracking added - `spectatorStartDimensions` HashMap prevents cross-dimension bugs
- Distance boundary enforcement when `teleport_back_on_exceed=false` - players stopped at exact boundary
- HUD behavior improved (later hard-coded in final release)
- Message encoding fixed - removed `§` color codes causing garbled characters (À symbols)
- Messages now use `Component.literal().withStyle(ChatFormatting.XXX)` for proper rendering
- Messages visible even with HUD hidden (action bar works independently)
- Logout handler cleanup for dimension tracking

### Changed
- Distance limit is now fully configurable (default: 75 blocks, set to -1 to disable)
- Block interaction whitelist now supports any Minecraft block ID instead of hardcoded door/trapdoor/gate types
- Command permission levels can now be customized per-command
- All hardcoded values replaced with config-driven logic
- Improved server admin flexibility with granular control over all spectator restrictions
- Enhanced mod description to reflect configurability and beta status

### Technical
- Created `ModConfig.java` with NeoForge ConfigSpec API (26+ configurable options)
- Integrated `ModConfigEvent` listener for hot-reload support
- Updated `SpectatorMod.java` to use config values throughout
- Updated `ClientEventHandler.java` to respect client-side config options
- Added `PlayerEvent.ItemCraftedEvent` handler for inventory crafting control
  - Captures ingredients from crafting container before consumption
  - Clears crafting grid to prevent duplication
  - Restores ingredients with fallback to ground drop if inventory full
  - Handles both single-slot and full 2x2 grid recipes
- Added `LivingIncomingDamageEvent` handler for invulnerability control (partial)
- Added `BlockEvent.BreakEvent` and `BlockEvent.EntityPlaceEvent` handlers
- Added `ClientboundPlayerAbilitiesPacket` for flying state sync (partial)
- Removed hardcoded constants in favor of config references
- Configuration file auto-generates on first launch with detailed comments

### Documentation
- Added comprehensive configuration section with all available options and examples
- Updated Future Roadmap to reflect completed features
- Added example configuration customization scenarios
- Created CONTRIBUTING.md with developer guidelines
- Documented Known Issues section

### Known Limitations (Final Status)

**Note**: Several "issues" identified in beta testing were determined to be Minecraft engine limitations, not bugs. The final release addresses these by:

1. **Fall Damage**: Confirmed as Minecraft core behavior - fall damage is always prevented when `mayfly=true`. Config option retained for other damage types (mobs, lava, fire, etc.).

2. **Auto-Start Flying**: Removed `auto_start_flying` config option. Players must double-tap spacebar (ADVENTURE mode limitation).

3. **Block Breaking/Placing**: Confirmed as ADVENTURE mode GameMode restriction. Removed `allow_block_breaking` and `allow_block_placing` config options.

4. **HUD Behavior**: Hard-coded HUD auto-hide with F1 toggle. Removed `auto_hide_hud` and `allow_f1_hud_toggle` config options.

5. **Mob Attacks**: Always blocked (mobs don't target players with `mayfly=true` anyway). Removed `allow_mob_attacks` config option.

## [1.0.2] - 2025-11-08

### Fixed
- Fixed critical bug where items would disappear from inventory when attempting to drop them in spectator mode
  - Items are now properly returned to the player's inventory when drop is blocked
  - Resolved issue with `ItemTossEvent` that removed items before the event could be cancelled
- Improved item handling to prevent item duplication or loss

### Added
- Server-side event handler for blocking item pickup (`ItemEntityPickupEvent.Pre`)
- Server-side event handler for blocking item dropping with proper inventory restoration (`ItemTossEvent`)
- Enhanced inventory protection in limited spectator mode

### Changed
- Item drop blocking now uses smart inventory restoration to prevent item loss
- Item pickup blocking uses `TriState.FALSE` for proper NeoForge 1.21.1+ compatibility
- Updated `build.gradle` to use `configurations.configureEach` instead of deprecated `configurations.all` for better performance
- Replaced deprecated `programArguments` with `getArguments()` in run configurations (NeoGradle 7.0+ compatibility)

## [1.0.0] - 2025-11-02

### Added
- Initial release of Limited Spectator mod
- Custom `/spectator` command to enter limited spectator mode
- Custom `/survival` command to return to survival mode
- Distance-based teleportation system (75 block radius limit)
- Server-side position tracking and enforcement
- Client-server packet communication for HUD state synchronization
- F1 key toggle support for temporary HUD visibility
- Selective block interaction (doors, trapdoors, and fence gates allowed)
- PvP and mob attack prevention in spectator mode
- Dimension travel blocking while in spectator mode
- Automatic survival mode restoration on player logout
- Console logging with `[LimitedSpectator]` prefix for debugging

### Features
- Limited spectator mode using Adventure game mode with flight abilities
- HUD hidden by default with F1 toggle capability
- Player repositioning on distance limit exceeded or `/survival` command
- Flight enabled via double-space bar press
- All restrictions enforced server-side for multiplayer security
- Compatible with Minecraft 1.21.1 and NeoForge 21.1.0+

[Unreleased]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.1.2...HEAD
[1.1.2]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.1.1...v1.1.2
[1.1.1]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.1.0-beta...v1.1.1
[1.1.0-beta]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.2...v1.1.0-beta
[1.0.2]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/releases/tag/v1.0.0
