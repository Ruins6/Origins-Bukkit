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
package me.swagpancakes.originsbukkit.commands.subcommands;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Permissions;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Reload.
 *
 * @author SwagPannekaker
 */
public class Reload {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Reload.
     *
     * @param plugin the plugin
     */
    public Reload(OriginsBukkit plugin) {
        this.plugin = plugin;
    }

    /**
     * Reload sub command.
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
     */
    public void ReloadSubCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.RELOAD.toString())) {
                if (args.length == 1) {
                    ChatUtils.sendCommandSenderMessage(sender, "&3Reloading the plugin files...");
                    try {
                        plugin.getConfigHandler().reloadFiles();
                    } catch (Exception event) {
                        event.printStackTrace();
                    }
                    ChatUtils.sendCommandSenderMessage(sender, "&aSuccessfully reloaded the plugin files.");
                } else {
                    ChatUtils.sendCommandSenderMessage(sender, "&cToo many arguments. Usage: /origins reload");
                }
            } else {
                ChatUtils.sendCommandSenderMessage(sender, Lang.NO_PERMISSION_COMMAND.toString());
            }
        } else {
            if (args.length == 1) {
                ChatUtils.sendCommandSenderMessage(sender, "&3[Origins-Bukkit] Reloading the plugin files...");
                try {
                    plugin.getConfigHandler().reloadFiles();
                } catch (Exception event) {
                    event.printStackTrace();
                }
                ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Successfully reloaded the plugin files.");
            } else {
                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Too many arguments. Usage: /origins reload");
            }
        }
    }

    /**
     * Reload sub command tab complete list.
     *
     * @param sender  the sender
     * @param command the command
     * @param alias   the alias
     * @param args    the args
     *
     * @return the list
     */
    public List<String> ReloadSubCommandTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> empty = new ArrayList<>();

        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.RELOAD.toString())) {
                if (args.length == 1) {
                    List<String> subCommand = new ArrayList<>();
                    subCommand.add("reload");

                    return subCommand;
                }
            }
        } else {
            if (args.length == 1) {
                List<String> subCommand = new ArrayList<>();
                subCommand.add("reload");

                return subCommand;
            }
        }
        return empty;
    }
}
