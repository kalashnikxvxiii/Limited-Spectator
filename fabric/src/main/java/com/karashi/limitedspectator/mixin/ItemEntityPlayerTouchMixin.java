package com.karashi.limitedspectator.mixin;

import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.LimitedSpectatorFabric;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Blocks item pickup while a player is in limited spectator mode, mirroring
 * NeoForge's {@code onItemPickup} handler (which uses
 * {@code ItemEntityPickupEvent.Pre}). Fabric has no equivalent event, so this
 * intercepts {@code ItemEntity.playerTouch(Player)} directly — verified via
 * javap on the 26.2 merged jar (single method, void return, one Player arg).
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityPlayerTouchMixin {

    @Inject(method = "playerTouch(Lnet/minecraft/world/entity/player/Player;)V", at = @At("HEAD"), cancellable = true)
    private void limitedspectator$blockPickup(Player player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!LimitedSpectatorFabric.getSpectatorManager().isInSpectatorMode(serverPlayer.getUUID())) {
            return;
        }

        if (!CommonConfig.getInstance().allowItemPickup) {
            ci.cancel();
        }
    }
}
