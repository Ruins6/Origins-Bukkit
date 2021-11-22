/*
 *     Origins-Bukkit
 *     Copyright (C) 2021 SwagPannekaker
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit.util;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The type Chat utils.
 *
 * @author SwagPannekaker
 */
public class ChatUtils {

    /**
     * Format string.
     *
     * @param format the format
     *
     * @return the string
     */
    public static String format(@NotNull String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    /**
     * Format list string [ ].
     *
     * @param format the format
     *
     * @return the string [ ]
     */
    public static String[] formatList(@NotNull String[] format) {
        String[] result;
        result = new String[format.length];
        for (int i = 0; i < format.length; i++) {
            result[i] = ChatColor.translateAlternateColorCodes('&', format[i]);
        }
        return result;
    }

    /**
     * Send console message.
     *
     * @param message the message
     */
    public static void sendConsoleMessage(@NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(format(message));
    }

    /**
     * Send player message.
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendPlayerMessage(@NotNull Player player, @NotNull String message) {
        player.sendMessage(format(message));
    }

    /**
     * Send command sender message.
     *
     * @param sender  the sender
     * @param message the message
     */
    public static void sendCommandSenderMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(format(message));
    }
}
