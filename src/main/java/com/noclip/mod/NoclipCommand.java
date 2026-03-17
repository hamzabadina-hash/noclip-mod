package com.noclip.mod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class NoclipCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("noclip")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(context -> {
                ServerCommandSource source = context.getSource();
                ServerPlayerEntity player = source.getPlayer();
                if (player == null) {
                    source.sendError(Text.literal("Only players can use this."));
                    return 0;
                }
                toggleNoclip(player);
                return 1;
            })
            .then(CommandManager.argument("target", StringArgumentType.word())
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    String targetName = StringArgumentType.getString(context, "target");
                    ServerPlayerEntity target = source.getServer()
                            .getPlayerManager().getPlayer(targetName);
                    if (target == null) {
                        source.sendError(Text.literal("Player '" + targetName + "' not found."));
                        return 0;
                    }
                    toggleNoclip(target);
                    return 1;
                })
            )
        );
    }

    public static void toggleNoclip(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        if (NoclipMod.NOCLIP_PLAYERS.contains(uuid)) {
            NoclipMod.NOCLIP_PLAYERS.remove(uuid);
            player.noClip = false;
            player.sendMessage(Text.literal("§cNoclip disabled."), true);
        } else {
            NoclipMod.NOCLIP_PLAYERS.add(uuid);
            player.noClip = true;
            player.fallDistance = 0f;
            player.sendMessage(Text.literal("§aNoclip enabled."), true);
        }
    }
}
