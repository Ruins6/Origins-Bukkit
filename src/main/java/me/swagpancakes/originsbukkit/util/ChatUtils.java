package me.swagpancakes.originsbukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The type Chat utils.
 */
public class ChatUtils {

    /**
     * Format string.
     *
     * @param format the format
     * @return the string
     */
    public static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    /**
     * Format list string [ ].
     *
     * @param format the format
     * @return the string [ ]
     */
    public static String[] formatList(String[] format) {
        String[] result;
        result = new String[format.length];
        for(int i = 0; i < format.length ; i++) {
            result[i] = ChatColor.translateAlternateColorCodes('&', format[i]);
        }
        return result;
    }

    /**
     * Send console message.
     *
     * @param message the message
     */
    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(format(message));
    }

    /**
     * Send player message.
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendPlayerMessage(Player player, String message) {
        player.sendMessage(format(message));
    }

    /**
     * Send command sender message.
     *
     * @param sender  the sender
     * @param message the message
     */
    public static void sendCommandSenderMessage(CommandSender sender, String message) {
        sender.sendMessage(format(message));
    }
}
