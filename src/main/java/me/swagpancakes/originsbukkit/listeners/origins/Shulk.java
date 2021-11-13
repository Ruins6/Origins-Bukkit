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
package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.UUID;

/**
 * The type Shulk.
 *
 * @author SwagPannekaker
 */
public class Shulk implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Shulk.
     *
     * @param plugin the plugin
     */
    public Shulk(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Shulk join.
     *
     * @param player the player
     */
    public void shulkJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.SHULK) {
            AttributeInstance genericArmorAttribute = player.getAttribute(Attribute.GENERIC_ARMOR);

            if (genericArmorAttribute != null) {
                double defaultBaseValue = genericArmorAttribute.getBaseValue();

                if (!(defaultBaseValue >= 8))
                genericArmorAttribute.setBaseValue(defaultBaseValue + 8);
            }
            player.setHealthScale((10) * 2);
            if (!plugin.storageUtils.getShulkPlayerStorageData().containsKey(playerUUID)) {
                try {
                    plugin.storageUtils.loadShulkPlayerStorageData(playerUUID);
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }
    }

    /**
     * Shulk shield disability.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkShieldDisability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();
        World world = player.getWorld();
        Location location = player.getLocation();
        EquipmentSlot equipmentSlot = event.getHand();
        PlayerInventory playerInventory = player.getInventory();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.SHULK) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if (itemStack != null) {
                    Material material = itemStack.getType();

                    if (material == Material.SHIELD) {
                        if (equipmentSlot == EquipmentSlot.HAND) {
                            playerInventory.remove(itemStack);
                            world.dropItemNaturally(location, itemStack);
                        } else if (equipmentSlot == EquipmentSlot.OFF_HAND) {
                            playerInventory.setItemInOffHand(null);
                            world.dropItemNaturally(location, itemStack);
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Shulk fast exhaustion.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkFastExhaustion(FoodLevelChangeEvent event) {
        HumanEntity humanEntity = event.getEntity();
        Player player = (Player) humanEntity;
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.SHULK) {
            event.setFoodLevel(event.getFoodLevel() - 1);
        }
    }

    /**
     * Shulk inventory ability.
     *
     * @param player the player
     */
    public void shulkInventoryAbility(Player player) {
        UUID playerUUID = player.getUniqueId();
        Inventory inv = Bukkit.createInventory(player, InventoryType.DROPPER, player.getName() + "'s Vault");

        if (plugin.storageUtils.getShulkPlayerStorageData().containsKey(playerUUID)) {
            inv.setContents(plugin.storageUtils.getShulkPlayerStorageData().get(playerUUID));
        }
        player.openInventory(inv);
    }

    /**
     * On shulk custom inventory close.
     *
     * @param event the event
     */
    @EventHandler
    public void onShulkCustomInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getView().getTitle().contains(player.getName() + "'s Vault")) {
            plugin.storageUtils.getShulkPlayerStorageData().remove(playerUUID);
            plugin.storageUtils.getShulkPlayerStorageData().put(playerUUID, event.getInventory().getContents());
            try {
                plugin.storageUtils.saveShulkPlayerStorageData(playerUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
