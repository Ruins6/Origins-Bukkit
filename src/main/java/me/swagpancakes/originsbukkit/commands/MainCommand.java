package me.swagpancakes.originsbukkit.commands;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.items.ItemManager;
import me.swagpancakes.originsbukkit.listeners.PlayerOriginChecker;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * The type Main command.
 */
public class MainCommand implements CommandExecutor {

    private final Main plugin;

    /**
     * Instantiates a new Main command.
     *
     * @param plugin the plugin
     */
    public MainCommand(Main plugin){
        Objects.requireNonNull(plugin.getCommand("origins")).setExecutor(this);
        this.plugin = plugin;
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
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (sender.hasPermission("origins.admin")) {
                if (args.length == 1) {
                    switch (args[0].toUpperCase()) {
                        case "RELOAD":
                            try {
                                plugin.configHandler.setup();
                                ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Reloading the config...");
                            } catch (Exception event) {
                                event.printStackTrace();
                                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] There was an error reloading the config. Please check console.");
                            }
                            ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Successfully reloaded the config.");
                            break;
                        case "TEST":
                            player.getInventory().addItem(ItemManager.abilitySceptre);
                            break;
                        case "TEST2":
                            PlayerOriginChecker.openOriginPickerGui(player);
                            break;
                        case "TEST3":
                            player.sendMessage(Lang.HUMAN_DESCRIPTION.toString());
                    }
                }
            }
        } else {
            if (args.length == 1) {
                switch (args[0].toUpperCase()) {
                    case "RELOAD":
                        try {
                            plugin.configHandler.setup();
                            ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Reloading the config...");
                        } catch (Exception event) {
                            event.printStackTrace();
                            ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] There was an error reloading the config. Please check console.");
                        }
                        ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Successfully reloaded the config.");
                        break;
                    case "TEST":
                        ChatUtils.sendCommandSenderMessage(sender, "HI :D");
                        break;
                }
            }
        }
        return true;
    }
}
