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
package me.swagpancakes.originsbukkit.commands;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.commands.subcommands.Help;
import me.swagpancakes.originsbukkit.commands.subcommands.Prune;
import me.swagpancakes.originsbukkit.commands.subcommands.Reload;
import me.swagpancakes.originsbukkit.commands.subcommands.Update;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Main command.
 *
 * @author SwagPannekaker
 */
public class MainCommand implements TabExecutor {

    private final OriginsBukkit plugin;
    private final Help help;
    private final Prune prune;
    private final Reload reload;
    private final Update update;

    /**
     * Instantiates a new Main command.
     *
     * @param plugin the plugin
     */
    public MainCommand(OriginsBukkit plugin) {
        this.plugin = plugin;
        this.help = new Help(plugin);
        this.prune = new Prune(plugin);
        this.reload = new Reload(plugin);
        this.update = new Update(plugin);
        init();
    }

    /**
     * Init.
     */
    private void init() {
        PluginCommand pluginMainCommand = plugin.getCommand("origins");

        if (pluginMainCommand != null) {
            pluginMainCommand.setExecutor(this);
        }
    }

    /**
     * On command boolean.
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
     *
     * @return the boolean
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            help.HelpSubCommand(sender, command, label, args);
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                help.HelpSubCommand(sender, command, label, args);
            } else if (args[0].equalsIgnoreCase("reload")) {
                reload.ReloadSubCommand(sender, command, label, args);
            } else if (args[0].equalsIgnoreCase("prune")) {
                prune.PruneSubCommand(sender, command, label, args);
            } else if (args[0].equalsIgnoreCase("update")) {
                update.UpdateSubCommand(sender, command, label, args);
            } else {
                help.HelpSubCommand(sender, command, label, args);
            }
        }
        return true;
    }

    /**
     * On tab complete list.
     *
     * @param sender  the sender
     * @param command the command
     * @param alias   the alias
     * @param args    the args
     *
     * @return the list
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 1) {
            List<String> subCommands = new ArrayList<>();

            subCommands.addAll(help.HelpSubCommandTabComplete(sender, command, alias, args));
            subCommands.addAll(prune.PruneSubCommandTabComplete(sender, command, alias, args));
            subCommands.addAll(reload.ReloadSubCommandTabComplete(sender, command, alias, args));
            subCommands.addAll(update.UpdateSubCommandTabComplete(sender, command, alias, args));

            return subCommands;
        }
        return null;
    }
}
