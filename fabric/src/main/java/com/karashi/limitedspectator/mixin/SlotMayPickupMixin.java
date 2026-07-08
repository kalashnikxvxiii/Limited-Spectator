package com.karashi.limitedspectator.mixin;

import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.LimitedSpectatorFabric;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blocks taking the crafted output while a player is in limited spectator
 * mode, mirroring NeoForge's {@code onItemCrafted} handler but far more
 * cleanly: since {@code mayPickup} runs before the recipe is consumed,
 * returning {@code false} means the ingredients simply stay put — no
 * duplication bookkeeping needed.
 *
 * <p>javap on the 26.2 merged jar confirmed {@code ResultSlot} does NOT
 * override {@code mayPickup(Player)} — only its base class {@link Slot}
 * declares it. Mixin requires the target method to actually exist in the
 * mixed class's own bytecode, so this mixes into {@code Slot} and scopes the
 * restriction with an {@code instanceof ResultSlot} guard rather than mixing
 * into {@code ResultSlot} directly (which would fail to apply — the method
 * isn't declared there).
 */
@Mixin(Slot.class)
public abstract class SlotMayPickupMixin {

    @Inject(method = "mayPickup(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true)
    private void limitedspectator$blockCraftingPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof ResultSlot)) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!LimitedSpectatorFabric.getSpectatorManager().isInSpectatorMode(serverPlayer.getUUID())) {
            return;
        }

        if (!CommonConfig.getInstance().allowInventoryCrafting) {
            cir.setReturnValue(false);
        }
    }
}
