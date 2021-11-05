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
import org.bukkit.scheduler.BukkitRunnable;

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
            checkPlayerOriginData(player);
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
            plugin.noOriginPlayerRestrict.restrictPlayerMovement(player);

            new BukkitRunnable() {

                @Override
                public void run() {
                    openOriginPickerGui(player);
                }
            }.runTaskLater(plugin, 30L);
        } else {
            initiatePlayerOrigin(player);
        }
    }

    /**
     * Check player origin data.
     *
     * @param player the player
     */
    public void checkPlayerOriginData(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            openOriginPickerGui(player);
            plugin.noOriginPlayerRestrict.restrictPlayerMovement(player);
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

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) != null) {
            switch (plugin.storageUtils.getPlayerOrigin(playerUUID)) {
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
        inv.setItem(13, createGuiItem(Material.PLAYER_HEAD, 1,
                Lang.HUMAN_TITLE.toString(),
                Lang.HUMAN_DESCRIPTION.toStringList()));
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
     * Open origin picker gui.
     *
     * @param humanEntity the human entity
     */
    public void openOriginPickerGui(HumanEntity humanEntity) {
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

        switch (event.getRawSlot()) {
            case 13:
                executeOriginPickerGuiOriginOption(player, Origins.HUMAN);
                break;
            case 19:
                executeOriginPickerGuiOriginOption(player, Origins.ENDERIAN);
                break;
            case 22:
                executeOriginPickerGuiOriginOption(player, Origins.MERLING);
                break;
            case 25:
                executeOriginPickerGuiOriginOption(player, Origins.PHANTOM);
                break;
            case 28:
                executeOriginPickerGuiOriginOption(player, Origins.ELYTRIAN);
                break;
            case 31:
                executeOriginPickerGuiOriginOption(player, Origins.BLAZEBORN);
                break;
            case 34:
                executeOriginPickerGuiOriginOption(player, Origins.AVIAN);
                break;
            case 37:
                executeOriginPickerGuiOriginOption(player, Origins.ARACHNID);
                break;
            case 40:
                executeOriginPickerGuiOriginOption(player, Origins.SHULK);
                break;
            case 43:
                executeOriginPickerGuiOriginOption(player, Origins.FELINE);
                break;
            case 49:
                player.kickPlayer(ChatUtils.format("&7You logged out of the game."));
        }
    }

    /**
     * Execute origin picker gui origin option.
     *
     * @param player the player
     * @param origin the origin
     */
    public void executeOriginPickerGuiOriginOption(Player player, Origins origin) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            plugin.storageUtils.createOriginsPlayerData(playerUUID, player, origin);
            initiatePlayerOrigin(player);
            player.closeInventory();
            plugin.noOriginPlayerRestrict.unrestrictPlayerMovement(player);
        } else if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), origin)) {
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ALREADY_SELECTED
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
        } else {
            plugin.storageUtils.updateOriginsPlayerData(playerUUID, new OriginsPlayerData(playerUUID, player.getName(), origin));
            initiatePlayerOrigin(player);
            player.closeInventory();
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_UPDATE
                    .toString()
                    .replace("%player_selected_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
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
