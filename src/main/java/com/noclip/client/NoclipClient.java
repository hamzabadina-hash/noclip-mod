package com.noclip.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import io.netty.buffer.Unpooled;

public class NoclipClient implements ClientModInitializer {

    private static KeyBinding noclipKey;

    @Override
    public void onInitializeClient() {
        // Register keybind — default key is N, category "Noclip"
        noclipKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.noclip.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.noclip"
        ));

        // Every client tick, check if key was pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (noclipKey.wasPressed()) {
                // Send toggle packet to server
                ClientPlayNetworking.send(
                    new NoclipTogglePayload()
                );
            }
        });
    }
}
