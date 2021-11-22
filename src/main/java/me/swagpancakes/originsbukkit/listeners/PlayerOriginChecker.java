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
package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.OriginChangeEvent;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.listeners.origins.Elytrian;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Player origin checker.
 *
 * @author SwagPannekaker
 */
public class PlayerOriginChecker implements Listener {

    private final OriginsBukkit plugin;
    private final Map<UUID, Integer> originPickerGUIViewers = new HashMap<>();

    /**
     * Instantiates a new Player origin checker.
     *
     * @param plugin the plugin
     */
    public PlayerOriginChecker(OriginsBukkit plugin) {
        this.plugin = plugin;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        originPickerGui();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (plugin.storageUtils.isOriginsPlayerDataLoaded()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        checkPlayerOriginData(player);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

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
            if (plugin.origins.contains(plugin.storageUtils.getPlayerOrigin(playerUUID))) {
                for (String origin : plugin.origins) {
                    if (origin.equals(plugin.storageUtils.getPlayerOrigin(playerUUID))) {
                        PlayerOriginInitiateEvent playerOriginInitiateEvent = new PlayerOriginInitiateEvent(player, origin);
                        Bukkit.getPluginManager().callEvent(playerOriginInitiateEvent);
                    }
                }
            } else {
                ChatUtils.sendPlayerMessage(player, "&cYour origin (" + plugin.storageUtils.getPlayerOrigin(playerUUID) + ") doesn't exist so we pruned your player data.");
                plugin.storageUtils.deleteOriginsPlayerData(playerUUID);
                checkPlayerOriginData(player);
            }
        }
    }

    /**
     * Origin picker gui.
     */
    public void originPickerGui() {
        if (!plugin.originsInventoryGUI.isEmpty()) {
            for (Inventory inv : plugin.originsInventoryGUI) {
                ItemStack previous = new ItemStack(Material.ARROW, 1);
                ItemMeta previousMeta = previous.getItemMeta();
                if (previousMeta != null) {
                    previousMeta.setDisplayName(ChatColor.GOLD + "<< Previous Page");
                    previous.setItemMeta(previousMeta);
                }

                ItemStack close = new ItemStack(Material.BARRIER, 1);
                ItemMeta closeMeta = close.getItemMeta();
                if (closeMeta != null) {
                    closeMeta.setDisplayName(ChatColor.RED + "Close Menu");
                    close.setItemMeta(closeMeta);
                }

                ItemStack next = new ItemStack(Material.ARROW, 1);
                ItemMeta nextMeta = next.getItemMeta();
                if (nextMeta != null) {
                    nextMeta.setDisplayName(ChatColor.GOLD + "Next Page >>");
                    next.setItemMeta(nextMeta);
                }

                inv.setItem(48, previous);
                inv.setItem(49, close);
                inv.setItem(50, next);
            }
        }
    }

    /**
     * Open origin picker gui.
     *
     * @param humanEntity the human entity
     */
    public void openOriginPickerGui(HumanEntity humanEntity) {
        UUID playerUUID = humanEntity.getUniqueId();

        humanEntity.openInventory(plugin.originsInventoryGUI.get(0));
        originPickerGUIViewers.put(playerUUID, 0);
    }

    /**
     * On origin picker gui click.
     *
     * @param event the event
     */
    @EventHandler
    public void onOriginPickerGuiClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (plugin.originsInventoryGUI.contains(event.getClickedInventory())) {
            if (clickedItem != null) {
                if (!clickedItem.getType().isAir()) {
                    if (event.getRawSlot() == 48) {
                        if (plugin.originsInventoryGUI.indexOf(event.getClickedInventory()) != 0) {
                            player.openInventory(plugin.originsInventoryGUI.get(plugin.originsInventoryGUI.indexOf(event.getClickedInventory()) - 1));
                            if (originPickerGUIViewers.containsKey(player.getUniqueId())) {
                                originPickerGUIViewers.put(player.getUniqueId(), originPickerGUIViewers.get(player.getUniqueId()) - 1);
                            }
                        }
                    }
                    if (event.getRawSlot() == 50) {
                        if (!plugin.originsInventoryGUI.get(plugin.originsInventoryGUI.indexOf(event.getClickedInventory())).equals(plugin.originsInventoryGUI.get(plugin.originsInventoryGUI.size() - 1))) {
                            player.openInventory(plugin.originsInventoryGUI.get(plugin.originsInventoryGUI.indexOf(event.getClickedInventory()) + 1));
                            if (originPickerGUIViewers.containsKey(player.getUniqueId())) {
                                originPickerGUIViewers.put(player.getUniqueId(), originPickerGUIViewers.get(player.getUniqueId()) + 1);
                            }
                        }
                    }
                    if (event.getRawSlot() == 22){
                        for (String origin : plugin.origins) {
                            if (clickedItem.getItemMeta() != null) {
                                if (origin.equals(clickedItem.getItemMeta().getLocalizedName())) {
                                    executeOriginPickerGuiOriginOption(player, origin);
                                }
                            }
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    /**
     * Execute origin picker gui origin option.
     *
     * @param player the player
     * @param origin the origin
     */
    public void executeOriginPickerGuiOriginOption(Player player, String origin) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            plugin.storageUtils.createOriginsPlayerData(playerUUID, player, origin);
            initiatePlayerOrigin(player);
            originPickerGUIViewers.remove(playerUUID);
            player.closeInventory();
            plugin.noOriginPlayerRestrict.unrestrictPlayerMovement(player);
        } else if (plugin.storageUtils.getPlayerOrigin(playerUUID).equals(origin)) {
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
            UUID playerUUID = player.getUniqueId();

            if (originPickerGUIViewers.containsKey(playerUUID)) {
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
        if (plugin.originsInventoryGUI.contains(event.getInventory())) {
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
        Inventory inventory = event.getInventory();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            if (plugin.originsInventoryGUI.contains(inventory)) {

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!player.getOpenInventory().getTopInventory().equals(plugin.originsInventoryGUI.get(originPickerGUIViewers.get(playerUUID)))) {
                            player.openInventory(plugin.originsInventoryGUI.get(originPickerGUIViewers.get(playerUUID)));
                        }
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }

    /**
     * On player origin change.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerOriginChange(OriginChangeEvent event) {
        Player player = event.getPlayer();
        String newOrigin = event.getNewOrigin();

        if (!Objects.equals(newOrigin, Origins.AVIAN.toString())) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            player.setWalkSpeed(0.2F);
        }
        if (!Objects.equals(newOrigin, Origins.ARACHNID.toString())) {
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                player.setFlySpeed(0.1F);
                player.setAllowFlight(false);
                player.setFlying(false);
            } else {
                player.setFlySpeed(0.1F);
                player.setAllowFlight(true);
            }
        }
        if (!Objects.equals(newOrigin, Origins.ELYTRIAN.toString())) {
            ItemStack prevChestplate = player.getInventory().getChestplate();

            if (prevChestplate != null && prevChestplate.equals(Elytrian.elytra)) {
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
            }
        }
        if (!Objects.equals(newOrigin, Origins.FELINE.toString())) {
            player.removePotionEffect(PotionEffectType.JUMP);
        }
        if (!Objects.equals(newOrigin, Origins.PHANTOM.toString())) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            //plugin.ghostFactory.setGhost(player, false);
        }
        if (!Objects.equals(newOrigin, Origins.SHULK.toString())) {
            AttributeInstance genericArmorAttribute = player.getAttribute(Attribute.GENERIC_ARMOR);

            if (genericArmorAttribute != null) {
                double modifiedBaseValue = genericArmorAttribute.getBaseValue();

                if (modifiedBaseValue == 8) {
                    genericArmorAttribute.setBaseValue(modifiedBaseValue - 8);
                }
            }
        }
    }
}
