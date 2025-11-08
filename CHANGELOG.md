# Changelog

All notable changes to the Limited Spectator mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).



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

[Unreleased]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.2...HEAD
[1.0.2]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/releases/tag/v1.0.0
