# Changelog

All notable changes to the Limited Spectator mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.1] - 2025-11-05

### Changed
- Updated mod initialization to use `IEventBus` parameter in constructor instead of deprecated `@EventBusSubscriber(bus = ...)` annotation
- Modernized event registration for NeoForge 1.21.1+ compatibility
- Replaced all `System.out.println()` and `System.err.println()` calls with proper SLF4J logging
- Replaced `printStackTrace()` calls with `LOGGER.error()` with exception parameter for better error tracking
- **Updated NeoForge from 21.1.209 to 21.1.213** for bug fixes and security improvements

### Fixed
- Suppressed IDE warnings for event handler methods that are called via reflection
- Suppressed deprecation warnings for `mayfly` ability field (awaiting NeoForge API replacement)
- Removed redundant null check in `/survival` command after `getPlayerOrException()` call
- Removed always-true condition checking `player.connection != null && player.server != null` in `/survival` command
- Removed always-false null check for `BlockState` in `onPlayerRightClickBlock` event handler

### Security
- **Resolved CVE-2025-24970** (Netty vulnerability, Score 7.5) by forcing Netty upgrade to 4.1.118.Final
- Added dependency resolution strategy in build.gradle to force Netty 4.1.118.Final (from transitive 4.1.97.Final)
- Netty handler vulnerability that could cause native crash with malformed SSL packets has been fully patched
- **Resolved CVE-2025-48924** (Apache Commons Lang3 vulnerability, Score 5.3) by forcing upgrade to 3.18.0
- Fixed uncontrolled recursion vulnerability in ClassUtils.getClass() that could cause StackOverflowError with very long inputs
- Upgraded from Commons Lang3 3.14.0 to 3.18.0 (from transitive dependency)

### Technical
- Removed deprecated `@EventBusSubscriber` with `bus` parameter
- Replaced inner `ModEvents` class with direct `IEventBus.addListener()` registration
- Added `@SuppressWarnings("unused")` to all event handler methods in `SpectatorMod` and `ClientEventHandler`
- Added `@SuppressWarnings("deprecation")` to methods using deprecated `mayfly` ability field
- Added SLF4J `Logger` instances to `SpectatorMod`, `NetworkHandler`, and `ClientEventHandler` classes
- Converted console output to proper log levels: `LOGGER.info()`, `LOGGER.error()`, and `LOGGER.debug()`
- Updated commented debug statements to use SLF4J logger with parameterized messages
- Added Gradle dependency resolution strategy to force specific dependency versions:
  - Netty 4.1.118.Final (handler, common, buffer, transport, codec)
  - Apache Commons Lang3 3.18.0

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

[Unreleased]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kalashnikxvxiii-collab/Limited-Spectator/releases/tag/v1.0.0
