package com.karashi.limitedspectator.mixin;

import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.LimitedSpectatorFabric;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Blocks item dropping (drop keybind, single item or full stack) while a player
 * is in limited spectator mode, mirroring NeoForge's {@code onItemToss} handler.
 *
 * <p>{@code ServerPlayer.drop(boolean)} is invoked directly from
 * {@code ServerGamePacketListenerImpl.handlePlayerAction} for the
 * {@code DROP_ITEM} / {@code DROP_ALL_ITEMS} player actions (verified via
 * javap on the 26.2 merged jar). Cancelling at HEAD means the item is never
 * removed from the inventory in the first place — no NeoForge-style
 * remove-then-re-add dance needed.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDropMixin {

    @Inject(method = "drop(Z)V", at = @At("HEAD"), cancellable = true)
    private void limitedspectator$blockDrop(boolean dropStack, CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        if (!LimitedSpectatorFabric.getSpectatorManager().isInSpectatorMode(self.getUUID())) {
            return;
        }

        if (!CommonConfig.getInstance().allowItemDrop) {
            ci.cancel();
        }
    }
}
