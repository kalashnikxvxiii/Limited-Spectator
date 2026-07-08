package com.karashi.limitedspectator.mixin;

import com.karashi.limitedspectator.CommonConfig;
import com.karashi.limitedspectator.LimitedSpectatorFabric;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blocks item dropping while a player is in limited spectator mode, mirroring
 * NeoForge's {@code onItemToss} handler (cancel the toss <em>and</em> restore the
 * stack, since by the time the drop fires the item is already gone from the
 * inventory).
 *
 * <p>Injects into the low-level {@code ServerPlayer.drop(ItemStack, boolean, boolean)}
 * rather than the keybind-only {@code drop(boolean)}, because BOTH player-initiated
 * drop paths funnel through it (verified with {@code javap -c} on the 26.2 jar):
 * <ul>
 *   <li>Drop keybind (Q / Ctrl+Q): {@code ServerPlayer.drop(boolean)} →
 *       {@code Inventory.removeFromSelected} → {@code drop(ItemStack,ZZ)}</li>
 *   <li>Dragging a stack out of the inventory GUI: {@code AbstractContainerMenu} →
 *       {@code Player.drop(ItemStack, boolean)} → {@code drop(ItemStack,ZZ)}</li>
 * </ul>
 * The earlier attempt injected at HEAD of {@code drop(boolean)} and cancelled: that
 * both missed the drag-out path entirely and left the client desynced (it had already
 * predicted the item leaving the slot, and the server never resynced), making the
 * item appear to vanish. By this low-level point the stack has already been removed
 * from the inventory/cursor, so we add it back and resync the open container before
 * cancelling the world-drop — no item loss, no desync.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDropMixin {

    @Inject(
        method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void limitedspectator$blockDrop(ItemStack itemStack, boolean throwRandomly, boolean retainOwnership,
                                            CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        if (itemStack == null || itemStack.isEmpty()) {
            return;
        }
        if (!LimitedSpectatorFabric.getSpectatorManager().isInSpectatorMode(self.getUUID())) {
            return;
        }
        if (!CommonConfig.getInstance().allowItemDrop) {
            // The stack was already pulled from the inventory (Q path) or the cursor
            // (inventory drag-out) before this low-level drop runs. Put it back and
            // resync the container so the client doesn't render the slot as empty,
            // then cancel the world-drop by returning no spawned ItemEntity.
            self.getInventory().add(itemStack);
            self.containerMenu.broadcastChanges();
            cir.setReturnValue(null);
        }
    }
}
