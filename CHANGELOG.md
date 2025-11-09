# Changelog

All notable changes to the Limited Spectator mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).



## [1.1.0-beta] - 2025-11-09

### ⚠️ Beta Release Notice

This is a **beta release** with significant new features and bug fixes. Some advanced features have known limitations that are being investigated. See "Known Issues" section below.

### Added
- **Complete Configuration System** - Comprehensive TOML-based configuration file (`limitedspectator-common.toml`)
  - **Movement Restrictions**: Configure max distance (-1 to disable), dimension travel, teleport behavior, logout position reset
  - **Player Abilities**: Toggle invulnerability, flight, auto-flying, and choose between ADVENTURE/SPECTATOR gamemode
  - **Interaction Controls**: Individually toggle PvP, mob attacks, item drop/pickup, block breaking/placing
  - **Customizable Block Whitelist**: Define exactly which blocks are interactable via Minecraft block IDs
  - **Permission System**: Set required permission levels (0-4) for `/spectator` and `/survival` commands
  - **Client/HUD Settings**: Configure auto-hide HUD and F1 toggle functionality
  - **Message Settings**: Choose action bar vs chat messages, enable/disable distance warnings
- Configuration validation with sensible defaults and range checking
- Hot-reload support for configuration changes via `/reload` command
- Performance-optimized config value caching
- **CONTRIBUTING.md** - Comprehensive contributor guide with coding standards and known issues

### Fixed
- **Critical**: `/survival` command now correctly teleports players back to original dimension (Overworld/Nether/End)
- **Critical**: Dimension tracking added - `spectatorStartDimensions` HashMap prevents cross-dimension bugs
- Distance boundary enforcement when `teleport_back_on_exceed=false` - players stopped at exact boundary
- Vanilla F1 HUD toggle now works when `auto_hide_hud=false`
- HUD flicker reduced when `auto_hide_hud=true` + `allow_f1_hud_toggle=true`
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
- Created `ModConfig.java` with NeoForge ConfigSpec API (25+ configurable options)
- Integrated `ModConfigEvent` listener for hot-reload support
- Updated `SpectatorMod.java` to use config values throughout
- Updated `ClientEventHandler.java` to respect client-side config options
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

### Known Issues

#### High Priority
1. **Fall Damage (enable_invulnerability=false)**: Players with `mayfly=true` in ADVENTURE mode don't receive fall damage even when invulnerability is disabled. This appears to be core Minecraft behavior with flying abilities.

2. **Auto-Start Flying (auto_start_flying=true)**: Players don't immediately enter flying mode when using `/spectator`. They must manually double-tap spacebar. Client-server packet sync issue under investigation.

3. **Block Interaction (allow_block_breaking/placing=true)**: Even with `mayBuild=true`, block breaking and placing don't work in ADVENTURE mode due to vanilla restrictions. Investigating alternative approaches.

#### Medium Priority
4. **HUD Edge Cases**:
   - `auto_hide_hud=false` + `allow_f1_hud_toggle=false`: Vanilla F1 still works (expected vanilla behavior)
   - `auto_hide_hud=true` + `allow_f1_hud_toggle=false`: Brief HUD flash on spectator mode entry (~1 frame)

These issues are documented in CONTRIBUTING.md and are actively being investigated. Contributions welcome!

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

[Unreleased]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.1.0-beta...HEAD
[1.1.0-beta]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.2...v1.1.0-beta
[1.0.2]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/releases/tag/v1.0.0
