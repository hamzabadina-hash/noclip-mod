package com.noclip.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NoclipMod implements ModInitializer {

    public static final String MOD_ID = "noclip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Set<UUID> NOCLIP_PLAYERS = new HashSet<>();

    @Override
    public void onInitialize() {
        LOGGER.info("NoclipMod initialized.");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NoclipCommand.register(dispatcher);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (NOCLIP_PLAYERS.contains(player.getUuid())) {
                    player.noClip = true;
                    player.fallDistance = 0f;
                } else {
                    if (player.noClip) {
                        player.noClip = false;
                    }
                }
            }
        });
    }
}
