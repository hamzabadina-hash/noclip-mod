package com.noclip.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NoclipMod implements ModInitializer {

    public static final String MOD_ID = "noclip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Set<UUID> NOCLIP_PLAYERS = new HashSet<>();
    public static final Map<UUID, List<Long>> SNEAK_TIMES = new HashMap<>();
    public static final Map<UUID, Boolean> LAST_SNEAK = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("NoclipMod initialized.");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NoclipCommand.register(dispatcher);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();
                boolean sneaking = player.isSneaking();
                boolean wasSneaking = LAST_SNEAK.getOrDefault(uuid, false);

                // Detect new sneak press
                if (sneaking && !wasSneaking) {
                    long now = System.currentTimeMillis();
                    SNEAK_TIMES.computeIfAbsent(uuid, k -> new ArrayList<>()).add(now);
                    SNEAK_TIMES.get(uuid).removeIf(t -> now - t > 1000);

                    if (SNEAK_TIMES.get(uuid).size() >= 3) {
                        SNEAK_TIMES.get(uuid).clear();
                        NoclipCommand.toggleNoclip(player);
                    }
                }

                LAST_SNEAK.put(uuid, sneaking);

                // Apply noclip every tick
                if (NOCLIP_PLAYERS.contains(uuid)) {
                    player.noClip = true;
                    player.fallDistance = 0f;
                } else {
                    if (player.noClip) {
                        player.noClip = false;
                    }
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.player.getUuid();
            NOCLIP_PLAYERS.remove(uuid);
            SNEAK_TIMES.remove(uuid);
            LAST_SNEAK.remove(uuid);
        });
    }
}
