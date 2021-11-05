package me.swagpancakes.originsbukkit.commands.subcommands;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Permissions;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Prune.
 */
public class Prune {

    private final Main plugin;

    /**
     * Instantiates a new Prune.
     *
     * @param plugin the plugin
     */
    public Prune(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Prune sub command.
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
     */
    public void PruneSubCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.PRUNE.toString())) {
                if (args.length == 1) {
                    ChatUtils.sendCommandSenderMessage(sender, "&cNot enough arguments. Usage: /origins prune <player>");
                } else if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);

                    if (target != null) {
                        String playerName = target.getName();
                        UUID playerUUID = target.getUniqueId();

                        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) != null) {
                            plugin.storageUtils.deleteOriginsPlayerData(playerUUID);
                            plugin.playerOriginChecker.checkPlayerOriginData(target);
                            ChatUtils.sendCommandSenderMessage(sender, "&aSuccessfully pruned " + playerName + "'s data");
                        } else {
                            ChatUtils.sendCommandSenderMessage(sender, "&cCannot find " + playerName + "'s data");
                        }
                    } else {
                        ChatUtils.sendCommandSenderMessage(sender, "&cPlayer " + args[1] + " not found. Player must be online to do this.");
                    }
                } else {
                    ChatUtils.sendCommandSenderMessage(sender, "&cToo many arguments. Usage: /origins prune <player>");
                }
            } else {
                ChatUtils.sendCommandSenderMessage(sender, Lang.NO_PERMISSION_COMMAND.toString());
            }
        } else {
            if (args.length == 1) {
                ChatUtils.sendCommandSenderMessage(sender, "&cNot enough arguments. Usage: /origins prune <player>");
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target != null) {
                    String playerName = target.getName();
                    UUID playerUUID = target.getUniqueId();

                    if (plugin.storageUtils.findOriginsPlayerData(playerUUID) != null) {
                        plugin.storageUtils.deleteOriginsPlayerData(playerUUID);
                        plugin.playerOriginChecker.checkPlayerOriginData(target);
                        ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Successfully pruned " + playerName + "'s data");
                    } else {
                        ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Cannot find " + playerName + "'s data");
                    }
                } else {
                    ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Player " + args[1] + " not found. Player must be online to do this.");
                }
            } else {
                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Too many arguments. Usage: /origins prune <player>");
            }
        }
    }

    /**
     * Prune sub command tab complete list.
     *
     * @param sender  the sender
     * @param command the command
     * @param alias   the alias
     * @param args    the args
     *
     * @return the list
     */
    public List<String> PruneSubCommandTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> empty = new ArrayList<>();

        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.PRUNE.toString())) {
                if (args.length == 1) {
                    List<String> subCommand = new ArrayList<>();
                    subCommand.add("prune");

                    return subCommand;
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("prune")) {
                        List<String> playerNames = new ArrayList<>();
                        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                        Bukkit.getServer().getOnlinePlayers().toArray(players);
                        for (Player player : players) {
                            playerNames.add(player.getName());
                        }

                        return playerNames;
                    }
                }
            }
        } else {
            if (args.length == 1) {
                List<String> subCommand = new ArrayList<>();
                subCommand.add("prune");

                return subCommand;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("prune")) {
                    List<String> playerNames = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (Player player : players) {
                        playerNames.add(player.getName());
                    }

                    return playerNames;
                }
            }
        }
        return empty;
    }
}
