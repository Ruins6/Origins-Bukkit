package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Player origin checker.
 */
public class PlayerOriginChecker implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Player origin checker.
     *
     * @param plugin the plugin
     */
    public PlayerOriginChecker(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

        originPickerGui();
        for (Player player : Bukkit.getOnlinePlayers()) {
            checkAllOnlinePlayersOriginData(player);
        }
    }

    private Inventory inv;

    /**
     * On player origin check.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerOriginCheck(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            openOriginPickerGui(player);
            NoOriginPlayerRestrict.restrictPlayerMovement(player);
        } else {
            initiatePlayerOrigin(player);
        }
    }

    /**
     * Check all online players origin data.
     *
     * @param player the player
     */
    public void checkAllOnlinePlayersOriginData(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            openOriginPickerGui(player);
            NoOriginPlayerRestrict.restrictPlayerMovement(player);
        } else {
            initiatePlayerOrigin(player);
        }
    }

    /**
     * Initiate player origin.
     *
     * @param player the player
     */
    public void initiatePlayerOrigin(Player player) {
        UUID playerUUID = player.getUniqueId();

        switch (Objects.requireNonNull(plugin.storageUtils.getPlayerOrigin(playerUUID))) {
            case HUMAN:
                plugin.human.humanJoin(player);
                break;
            case ENDERIAN:
                plugin.enderian.enderianJoin(player);
                break;
            case MERLING:
                plugin.merling.merlingJoin(player);
                break;
            case PHANTOM:
                plugin.phantom.phantomJoin(player);
                break;
            case ELYTRIAN:
                plugin.elytrian.elytrianJoin(player);
                break;
            case BLAZEBORN:
                plugin.blazeborn.blazebornJoin(player);
                break;
            case AVIAN:
                plugin.avian.avianJoin(player);
                break;
            case ARACHNID:
                plugin.arachnid.arachnidJoin(player);
                break;
            case SHULK:
                plugin.shulk.shulkJoin(player);
                break;
            case FELINE:
                plugin.feline.felineJoin(player);
                break;
        }
    }

    /**
     * Origin picker gui.
     */
    public void originPickerGui() {
        inv = Bukkit.createInventory(null, 54, ChatUtils.format("&0Choose your Origin."));

        initializeItems();
    }

    /**
     * Initialize items.
     */
    public void initializeItems() {
        inv.setItem(19, createGuiItem(Material.ENDER_PEARL, 1,
                Lang.ENDERIAN_TITLE.toString(),
                Lang.ENDERIAN_DESCRIPTION.toStringList()));
        inv.setItem(22, createGuiItem(Material.COD, 1,
                Lang.MERLING_TITLE.toString(),
                Lang.MERLING_DESCRIPTION.toStringList()));
        inv.setItem(25, createGuiItem(Material.PHANTOM_MEMBRANE, 1,
                Lang.PHANTOM_TITLE.toString(),
                Lang.PHANTOM_DESCRIPTION.toStringList()));
        inv.setItem(28, createGuiItem(Material.ELYTRA, 1,
                Lang.ELYTRIAN_TITLE.toString(),
                Lang.ELYTRIAN_DESCRIPTION.toStringList()));
        inv.setItem(31, createGuiItem(Material.BLAZE_POWDER, 1,
                Lang.BLAZEBORN_TITLE.toString(),
                Lang.BLAZEBORN_DESCRIPTION.toStringList()));
        inv.setItem(34, createGuiItem(Material.FEATHER, 1,
                Lang.AVIAN_TITLE.toString(),
                Lang.AVIAN_DESCRIPTION.toStringList()));
        inv.setItem(37, createGuiItem(Material.COBWEB, 1,
                Lang.ARACHNID_TITLE.toString(),
                Lang.ARACHNID_DESCRIPTION.toStringList()));
        inv.setItem(40, createGuiItem(Material.SHULKER_SHELL, 1,
                Lang.SHULK_TITLE.toString(),
                Lang.SHULK_DESCRIPTION.toStringList()));
        inv.setItem(43, createGuiItem(Material.ORANGE_WOOL, 1,
                Lang.FELINE_TITLE.toString(),
                Lang.FELINE_DESCRIPTION.toStringList()));
        inv.setItem(49, createGuiItem(Material.BARRIER, 1,
                ChatUtils.format("&cQuit Game"),
                ChatUtils.format("&7Logs you off the game"),
                ChatUtils.format(""),
                ChatUtils.format("&eClick to log out")));
    }

    /**
     * Initialize player skull items.
     *
     * @param player the player
     */
    public void initializePlayerSkullItems(Player player) {
        inv.setItem(13, createGuiPlayerSkullItem(player, 1,
                Lang.HUMAN_TITLE.toString(),
                Lang.HUMAN_DESCRIPTION.toStringList()));
    }

    /**
     * Create gui item item stack.
     *
     * @param material the material
     * @param amount   the amount
     * @param itemName the item name
     * @param itemLore the item lore
     * @return the item stack
     */
    ItemStack createGuiItem(Material material, Integer amount, String itemName, String... itemLore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(Arrays.asList(itemLore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Create gui player skull item item stack.
     *
     * @param player   the player
     * @param amount   the amount
     * @param itemName the item name
     * @param itemLore the item lore
     * @return the item stack
     */
    ItemStack createGuiPlayerSkullItem(Player player, Integer amount, String itemName, String... itemLore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName(itemName);
        skullMeta.setOwningPlayer(player);
        skullMeta.setLore(Arrays.asList(itemLore));
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

    /**
     * Open origin picker gui.
     *
     * @param humanEntity the human entity
     */
    public void openOriginPickerGui(HumanEntity humanEntity) {
        Player player = (Player) humanEntity;

        initializePlayerSkullItems(player);
        humanEntity.openInventory(inv);
    }

    /**
     * On origin picker gui click.
     *
     * @param event the event
     */
    @EventHandler
    public void onOriginPickerGuiClick(InventoryClickEvent event) {
        if (event.getInventory() != inv) return;
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        switch (event.getRawSlot()) {
            case 13:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.HUMAN);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.HUMAN)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.HUMAN));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 19:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.ENDERIAN);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.ENDERIAN));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 22:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.MERLING);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.MERLING));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 25:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.PHANTOM);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.PHANTOM)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.PHANTOM));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 28:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.ELYTRIAN);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ELYTRIAN)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.ELYTRIAN));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 31:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.BLAZEBORN);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.BLAZEBORN)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.BLAZEBORN));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 34:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.AVIAN);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.AVIAN)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.AVIAN));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 37:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.ARACHNID);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ARACHNID)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.ARACHNID));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 40:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.SHULK);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.SHULK));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 43:
                if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                    plugin.storageUtils.createOriginsPlayerData(playerUUID, player, Origins.FELINE);
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    NoOriginPlayerRestrict.unrestrictPlayerMovement(player);
                } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.FELINE)) {
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                            .toString()
                            .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                } else {
                    plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), Origins.FELINE));
                    initiatePlayerOrigin(player);
                    player.closeInventory();
                    ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                            .toString()
                            .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                }
                break;
            case 49:
                player.kickPlayer("&7You logged out of the game.");
        }
    }

    /**
     * Close all origin picker gui.
     */
    public void closeAllOriginPickerGui() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().equals(inv)) {
                player.closeInventory();
            }
        }
    }

    /**
     * On inventory click.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClick(InventoryDragEvent event) {
        if (event.getInventory().equals(inv)) {
            event.setCancelled(true);
        }
    }

    /**
     * On inventory close.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getInventory().equals(inv)) {
            if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(inv), 0L);
            }
        }
    }
}
