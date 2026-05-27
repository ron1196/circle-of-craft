package io.github.ron1196.circleofcraft.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChatHelper {

    private static final String NPC_PREFIX = "§e<%s> §f%s";

    public static String formatNpcMessage(String name, String message) {
        return String.format(NPC_PREFIX, name, message);
    }

    public static void sendNpcMessage(Player player, String name, String message) {
        player.sendSystemMessage(Component.literal(String.format(NPC_PREFIX, name, message)));
    }

    public static void broadcastNpcMessage(Level level, String name, String message) {
        Component msg = Component.literal(String.format(NPC_PREFIX, name, message));
        for (Player player : level.players()) {
            player.sendSystemMessage(msg);
        }
    }
}
