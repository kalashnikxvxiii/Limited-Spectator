🧭 Limited Spectator
======================

A lightweight NeoForge mod that introduces a restricted spectator mode for Minecraft players.


📖 Overview
=============

Limited Spectator allows players to enter a controlled spectator mode that limits what they can do while flying and observing the world.
Unlike the vanilla spectator mode, this version allows to press F1 to show HUD and restricts interactions to preserve game balance on servers or custom worlds.

Perfect for:

• SMP;

• Builders who want visual observation without full spectator privileges.


✨ Features
=============

✅ Limited Spectator Mode
Players can enter a custom spectator state that allows free flight but prevents inventory use, PvP, and world interactions (except doors, trapdoors, and gates).

✅ Selective HUD Visibility
HUD elements remain hidden by default but can be temporarily shown by pressing F1.

✅ Anti-Dimension Travel
Players in spectator mode cannot switch dimensions (Nether, End, etc.) until returning to survival.

✅ HUD Restoration
The HUD is restored only when switching back to /survival.

✅ Player Repositioning
The player is reset to the saved position whenever:
1. It moves too far away: 75 blocks (Customizable option in the future)
2. It executes the /survival command

✅ Flight possibility
The player in spectator mode has the ability to fly by double-pressing the space bar.

✅ PvP Disabled
All attack actions (including against mobs) are blocked while in limited spectator mode.

✅ Server-Friendly
All restrictions are handled server-side for secure multiplayer behavior.


🧠 Technical Details
=====================

| Aspect                | Value              |
| --------------------- | ------------------ |
| **Minecraft version** | 1.21.1             |
| **Mod loader**        | NeoForge           |
| **NeoForge version**  | 21.1.0+            |
| **Java version**      | 21                 |
| **Gradle wrapper**    | 8.10.2             |
| **Mod ID**            | `limitedspectator` |


⚙️ Commands
=============

| Command      | Description                                   |
| ------------ | --------------------------------------------- |
| `/spectator` | Switch to the limited spectator mode          |
| `/survival`  | Return to normal gameplay and restore the HUD |

🧩 Command feedback messages are in English (localization planned).


🔐 Restrictions in Spectator Mode
===================================

• ❌ No block breaking or placing

• ❌ No chest, bed, crafting table, or item interactions

• ❌ No dimension travel

• ❌ No PvP or mob attacks

• ✅ Doors, trapdoors, and fence gates remain interactable

• ✅ F1 toggles HUD visibility


🧩 Compatibility
==================

• ✅ Minecraft 1.21.1

• ✅ NeoForge 21.1.0+

• ✅ Multiplayer-safe

• ⚠️ Limited Spectator uses standard NeoForge event hooks and should be compatible with most mods. However, mods that deeply alter player gamemode handling or HUD rendering may interfere with its behavior.


🧰 Installation
=================

• Install NeoForge 21.1.0+

• Place limitedspectator-1.21.1-1.0.2.jar into your mods/ folder

• Launch Minecraft normally


🧪 Developer / Debug Info
===========================

For debugging, Limited Spectator includes console logs for:

• Player mode changes

• Spectator mode entry/exit

• Interaction blocking

• HUD toggle events

• Packet handling (SpectatorHudPacket)

These logs appear in the console with prefix:
[LimitedSpectator]


🌍 Localization
=================

Multi-language support will be added in future versions.


🧾 License
============

This project is licensed under the MIT License.
See the LICENSE file for details.
Copyright (c) 2025 karashi

You may use, modify, and distribute this mod freely, provided that attribution is maintained.


📦 Project Links
==================

| Platform     | Link                                                                                             |
| ------------ | ------------------------------------------------------------------------------------------------ |
| **Modrinth** | [Limited Spectator on Modrinth](https://modrinth.com/mod/limited-spectator)                      |
| **GitHub**   | [GitHub Repository](https://github.com/kalashnikxvxiii-collab/Limited-Spectator)                 |
| **Issues**   | [Report Bugs / Suggestions](https://github.com/kalashnikxvxiii-collab/Limited-Spectator/issues)  |


❤️ Credits
============

• Author: Karashi

• Development: Full custom codebase for NeoForge 1.21.1

• Icon Design: AI-generated minimalist design (OpenAI)

• Special thanks: Minecraft & NeoForge community


🚀 Future Roadmap
===================

• 🌐 Add multilingual translation files (en_us.json, it_it.json)

• ⚙️ Configurable interaction whitelist (e.g., allow more block types)

• 🧱 Optional permission system integration

• 🔍 Extended API for custom spectator events

• ⛓️‍💥 Customizable allowed distance
