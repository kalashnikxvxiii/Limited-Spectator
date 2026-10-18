package com.karashi.limitedspectator.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;

// MC 26.2 client HUD API: Options.hideGui moved to Gui.hud (toggle-only), Minecraft.screen to Gui.screen().
final class HudCompat {

    private HudCompat() {}

    static boolean isGuiHidden(Minecraft mc) {
        return mc.gui != null && mc.gui.hud.isHidden();
    }

    static void setGuiHidden(Minecraft mc, boolean hidden) {
        if (mc.gui != null && mc.gui.hud.isHidden() != hidden) {
            mc.gui.hud.toggle();
        }
    }

    static boolean hasScreen(Minecraft mc) {
        return mc.gui != null && mc.gui.screen() != null;
    }

    static boolean isHideGuiKeyDown(Minecraft mc) {
        return InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_F1);
    }
}
