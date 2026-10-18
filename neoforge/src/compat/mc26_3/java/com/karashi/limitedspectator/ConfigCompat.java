package com.karashi.limitedspectator;

import net.neoforged.fml.config.ModConfig;

// NeoForge 26.3 (FML 12) renamed COMMON to LOCAL; the file name stays limitedspectator-common.toml.
final class ConfigCompat {

    static final ModConfig.Type TYPE = ModConfig.Type.LOCAL;

    private ConfigCompat() {}
}
