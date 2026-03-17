package com.noclip.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NoclipMod implements ModInitializer {

    public static final String MOD_ID = "noclip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<UUID> NOCLIP_PLAYERS = new HashSet<>();

    // Payload record for receiving toggle packet
    public record NoclipTogglePayload() implements CustomPayload {
        public static final CustomPayload.Id<NoclipTogglePayload> ID =
            new CustomPayload.Id<>(Identifier.of("noclip", "toggle"));
        public static final PacketCodec<PacketByteBuf, NoclipTogglePayload> CODEC =
            PacketCodec.unit(new NoclipTogglePayload());
        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    @Override
    public void onInitialize() {
        LOGGER.info("NoclipMod initialized.");

        // Register the packet type
        PayloadTypeRegistry.playC2S().register(
            NoclipTogglePayload.ID,
            NoclipTogglePayload.CODEC
        );

        // Listen for keybind packet from client
        ServerPlayNetworking.registerGlobalReceiver(
            NoclipTogglePayload.ID,
            (payload, context) -> {
                ServerPlayerEntity player = context.player();
                context.server().execute(() -> NoclipCommand.toggleNoclip(player));
            }
        );

        // Keep /noclip command for ops
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NoclipCommand.register(dispatcher);
        });

        // Apply noclip every tick
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();
                if (NOCLIP_PLAYERS.contains(uuid)) {
                    player.noClip = true;
                    player.fallDistance = 0f;
                } else {
                    if (player.noClip) player.noClip = false;
                }
            }
        });

        // Clean up on disconnect
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            NOCLIP_PLAYERS.remove(handler.player.getUuid());
        });
    }
}
