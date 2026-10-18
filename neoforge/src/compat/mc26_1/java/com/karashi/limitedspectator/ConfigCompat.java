package com.karashi.limitedspectator;

import net.neoforged.fml.config.ModConfig;

// MC/NeoForge 26.1: COMMON = loaded on both sides, not synced.
final class ConfigCompat {

    static final ModConfig.Type TYPE = ModConfig.Type.COMMON;

    private ConfigCompat() {}
}
