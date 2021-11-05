package me.swagpancakes.originsbukkit.commands;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.commands.subcommands.Help;
import me.swagpancakes.originsbukkit.commands.subcommands.Prune;
import me.swagpancakes.originsbukkit.commands.subcommands.Reload;
import me.swagpancakes.originsbukkit.commands.subcommands.Update;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Main command.
 */
public class MainCommand implements TabExecutor {

    private final Main plugin;
    private final Help help;
    private final Prune prune;
    private final Reload reload;
    private final Update update;

    /**
     * Instantiates a new Main command.
     *
     * @param plugin the plugin
     */
    public MainCommand(Main plugin){
        Objects.requireNonNull(plugin.getCommand("origins")).setExecutor(this);
        this.plugin = plugin;
        this.help = new Help(plugin);
        this.prune = new Prune(plugin);
        this.reload = new Reload(plugin);
        this.update = new Update(plugin);
    }

    /**
     * On command boolean.
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
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
