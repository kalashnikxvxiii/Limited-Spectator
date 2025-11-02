package com.karashi.limitedspectator.network;

import com.karashi.limitedspectator.SpectatorMod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    public static final ResourceLocation HUD_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(SpectatorMod.MODID, "hud_toggle");

    // Packet Registration
    public static void register(RegisterPayloadHandlersEvent event) {
        System.out.println("[LimitedSpectator] Network packet logging...");

        // Correct method (modid only)
        PayloadRegistrar registrar = event.registrar(SpectatorMod.MODID);

        registrar.playToClient(
                SpectatorHudPacket.TYPE,
                SpectatorHudPacket.STREAM_CODEC,
                SpectatorHudPacket::handle
        );

        System.out.println("[LimitedSpectator] SpectatorHudPacket packet successfully registered.");
    }

    // Customized packet
    public static class SpectatorHudPacket implements CustomPacketPayload {
        public static final Type<SpectatorHudPacket> TYPE = new Type<>(HUD_PACKET_ID);

        // Fixed StreamCodec for NeoForge 1.21+
        public static final StreamCodec<FriendlyByteBuf, SpectatorHudPacket> STREAM_CODEC =
                StreamCodec.ofMember(SpectatorHudPacket::write, SpectatorHudPacket::new);

        private final boolean hideHud;

        public SpectatorHudPacket(boolean hideHud) {
            this.hideHud = hideHud;
        }

        // Constructor used by the codec
        public SpectatorHudPacket(FriendlyByteBuf buf) {
            this.hideHud = buf.readBoolean();
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(hideHud);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        // Fixed handler for NeoForge 1.21+
        public static void handle(SpectatorHudPacket msg, IPayloadContext ctx) {
            ctx.enqueueWork(() -> applyHudHidden(msg.hideHud));

            // FOR DEBUGGING
            // System.out.println("[LimitedSpectator][Client] HUD packet received → HUD " + (msg.hideHud ? "hidden" : "visible"));
        }

        @OnlyIn(Dist.CLIENT)
        private static void applyHudHidden(boolean hidden) {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.player != null && mc.level != null && mc.options != null) {
                mc.options.hideGui = hidden;
            }
        }
    }

    // Sending the packet from the server to the client
    public static void sendHudState(ServerPlayer player, boolean hideHud) {
        if (player != null) {
            PacketDistributor.sendToPlayer(player, new SpectatorHudPacket(hideHud));
        }
    }
}
