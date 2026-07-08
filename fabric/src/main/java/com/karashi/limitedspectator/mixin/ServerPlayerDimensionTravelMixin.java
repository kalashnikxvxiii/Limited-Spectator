package com.karashi.limitedspectator.mixin;

import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.LimitedSpectatorFabric;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blocks dimension travel while a player is in limited spectator mode,
 * mirroring NeoForge's {@code onPlayerChangeDimension} handler (which uses
 * {@code EntityTravelToDimensionEvent} — an event that no longer maps onto
 * any single vanilla method in 26.2).
 *
 * <p>javap + bytecode inspection of the 26.2 merged jar showed:
 * <ul>
 *   <li>{@code Entity.changeDimension(...)} no longer exists.</li>
 *   <li>{@code Entity.teleport(TeleportTransition)} branches internally
 *       (comparing {@code newLevel.dimension()} to the current level's
 *       dimension) into private {@code teleportCrossDimension} /
 *       {@code teleportSameDimension} helpers.</li>
 *   <li>{@code ServerPlayer} overrides {@code teleport(TeleportTransition)}
 *       with its own covariant-return implementation (returns
 *       {@code ServerPlayer}, not {@code Entity}) that does NOT delegate to
 *       those Entity-private helpers — it handles both the same-dimension
 *       and cross-dimension cases itself in one method body, branching on
 *       the identical dimension-key comparison.</li>
 * </ul>
 * This mixin targets that {@code ServerPlayer} override directly (using the
 * full descriptor to disambiguate it from the synthetic bridge method with
 * return type {@code Entity} that the compiler also emits) and re-derives the
 * same "is this actually a dimension change" check the vanilla method uses,
 * so same-dimension teleports (e.g. respawn-anchor explosions that don't
 * change level) are left untouched.
 *
 * <p>Cancelling with a {@code null} return value is safe: the vanilla method
 * itself already returns {@code null} from its {@code isRemoved()} early-out,
 * so every call site already tolerates a null result.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDimensionTravelMixin {

    @Inject(
        method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void limitedspectator$blockDimensionTravel(TeleportTransition transition, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        ServerLevel newLevel = transition.newLevel();
        boolean isDimensionChange = !newLevel.dimension().equals(self.level().dimension());
        if (!isDimensionChange) {
            return;
        }

        if (!LimitedSpectatorFabric.getSpectatorManager().isInSpectatorMode(self.getUUID())) {
            return;
        }

        if (!CommonConfig.getInstance().allowDimensionTravel) {
            cir.setReturnValue(null);
        }
    }
}
