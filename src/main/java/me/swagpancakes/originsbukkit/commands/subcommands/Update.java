package me.swagpancakes.originsbukkit.commands.subcommands;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.enums.Permissions;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * The type Update.
 */
public class Update {

    private final Main plugin;

    /**
     * Instantiates a new Update.
     *
     * @param plugin the plugin
     */
    public Update(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Update sub command.
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
     */
    public void UpdateSubCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.UPDATE.toString())) {
                if (args.length == 1) {
                    ChatUtils.sendCommandSenderMessage(sender, "&cNot Enough Arguments. Usage: /origins update <player> <new origin>");
                } else if (args.length == 2) {
                    ChatUtils.sendCommandSenderMessage(sender, "&cNot Enough Arguments. Usage: /origins update " + args[1] + " <new origin>");
                } else if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[1]);
                    Origins origin = Origins.valueOf(args[2]);
                    EnumSet<Origins> origins = EnumSet.allOf(Origins.class);

                    if (target != null) {
                        UUID playerUUID = target.getUniqueId();
                        String playerName = target.getName();

                        if (origins.contains(origin)) {
                            if (plugin.storageUtils.findOriginsPlayerData(playerUUID) != null) {
                                if (plugin.storageUtils.getPlayerOrigin(playerUUID) != origin) {
                                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, playerName, origin));
                                    plugin.playerOriginChecker.checkPlayerOriginData(target);
                                    ChatUtils.sendCommandSenderMessage(sender, "&aChanged " + playerName + "'s origin to " + origin);
                                } else {
                                    ChatUtils.sendCommandSenderMessage(sender, "&cNothing changed. Player's origin is already " + origin);
                                }
                            } else {
                                ChatUtils.sendCommandSenderMessage(sender, "&cCannot find " + playerName + "'s data");
                            }
                        } else {
                            ChatUtils.sendCommandSenderMessage(sender, "&cCannot find the origins " + args[2]);
                        }
                    } else {
                        ChatUtils.sendCommandSenderMessage(sender, "&cPlayer " + args[1] + " not found. Player must be online to do this.");
                    }
                } else {
                    ChatUtils.sendCommandSenderMessage(sender, "&cToo many arguments. Usage: /origins update <player> <new origin>");
                }
            } else {
                ChatUtils.sendCommandSenderMessage(sender, Lang.NO_PERMISSION_COMMAND.toString());
            }
        } else {
            if (args.length == 1) {
                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Not Enough Arguments. Usage: /origins update <player> <new origin>");
            } else if (args.length == 2) {
                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Not Enough Arguments. Usage: /origins update " + args[1] + " <new origin>");
            } else if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                Origins origin = Origins.valueOf(args[2]);
                EnumSet<Origins> origins = EnumSet.allOf(Origins.class);

                if (target != null) {
                    UUID playerUUID = target.getUniqueId();
                    String playerName = target.getName();

                    if (origins.contains(origin)) {
                        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) != null) {
                            if (!plugin.storageUtils.getPlayerOrigin(playerUUID).equals(origin)) {
                                plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, playerName, origin));
                                plugin.playerOriginChecker.checkPlayerOriginData(target);
                                ChatUtils.sendCommandSenderMessage(sender, "&a[Origins-Bukkit] Changed " + playerName + "'s origin to " + origin);
                            } else {
                                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Nothing changed. Player's origin is already " + origin);
                            }
                        } else {
                            ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Cannot find " + playerName + "'s data");
                        }
                    } else {
                        ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Cannot find the origins " + args[2]);
                    }
                } else {
                    ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Player " + args[1] + " not found. Player must be online to do this.");
                }
            } else {
                ChatUtils.sendCommandSenderMessage(sender, "&c[Origins-Bukkit] Too many arguments. Usage: /origins update <player> <new origin>");
            }
        }
    }

    /**
     * Update sub command tab complete list.
     *
     * @param sender  the sender
     * @param command the command
     * @param alias   the alias
     * @param args    the args
     *
     * @return the list
     */
    public List<String> UpdateSubCommandTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> empty = new ArrayList<>();

        if (sender instanceof Player) {
            if (sender.hasPermission(Permissions.UPDATE.toString())) {
                if (args.length == 1) {
                    List<String> subCommand = new ArrayList<>();
                    subCommand.add("update");

                    return subCommand;
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("update")) {
                        List<String> playerNames = new ArrayList<>();
                        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                        Bukkit.getServer().getOnlinePlayers().toArray(players);
                        for (Player player : players) {
                            playerNames.add(player.getName());
                        }

                        return playerNames;
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("update")) {
                        List<String> originsList = new ArrayList<>();
                        for (Origins origins : Origins.values()) {
                            if (origins.toString().startsWith(args[2].toUpperCase())) {
                                originsList.add(origins.toString().toUpperCase());
                            }
                        }

                        return originsList;
                    }
                }
            }
        } else {
            if (args.length == 1) {
                List<String> subCommand = new ArrayList<>();
                subCommand.add("update");

                return subCommand;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("update")) {
                    List<String> playerNames = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (Player player : players) {
                        playerNames.add(player.getName());
                    }

                    return playerNames;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("update")) {
                    List<String> originsList = new ArrayList<>();
                    for (Origins origins : Origins.values()) {
                        if (origins.toString().startsWith(args[2].toUpperCase())) {
                            originsList.add(origins.toString().toUpperCase());
                        }
                    }

                    return originsList;
                }
            }
        }
        return empty;
    }
}
