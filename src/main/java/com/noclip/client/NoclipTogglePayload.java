package com.noclip.client;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;

public record NoclipTogglePayload() implements CustomPayload {

    public static final CustomPayload.Id<NoclipTogglePayload> ID =
        new CustomPayload.Id<>(Identifier.of("noclip", "toggle"));

    public static final PacketCodec<PacketByteBuf, NoclipTogglePayload> CODEC =
        PacketCodec.unit(new NoclipTogglePayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
