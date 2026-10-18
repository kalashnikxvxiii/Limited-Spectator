package com.karashi.limitedspectator.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;

// MC 26.1.x client HUD API. One copy per MC version under neoforge/src/compat/ (selected in build.gradle).
final class HudCompat {

    private HudCompat() {}

    static boolean isGuiHidden(Minecraft mc) {
        return mc.options.hideGui;
    }

    static void setGuiHidden(Minecraft mc, boolean hidden) {
        mc.options.hideGui = hidden;
    }

    static boolean hasScreen(Minecraft mc) {
        return mc.screen != null;
    }

    static boolean isHideGuiKeyDown(Minecraft mc) {
        return InputConstants.isKeyDown(mc.getWindow(), GLFW.GLFW_KEY_F1);
    }
}
