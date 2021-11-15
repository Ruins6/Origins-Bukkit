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
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * The type Elytrian.
 *
 * @author SwagPannekaker
 */
public class Elytrian implements Listener {

    private final Main plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = Config.ORIGINS_ELYTRIAN_ABILITY_COOLDOWN.toInt();

    public ItemStack elytra;

    /**
     * Instantiates a new Elytrian.
     *
     * @param plugin the plugin
     */
    public Elytrian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

        createElytra();
    }

    /**
     * Elytrian join.
     *
     * @param player the player
     */
    public void elytrianJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
            player.setHealthScale(Config.ORIGINS_ELYTRIAN_MAX_HEALTH.toDouble());
            elytrianElytra(player);
            elytrianClaustrophobiaTimer(player);
        }
    }

    /**
     * Create elytra.
     */
    public void createElytra() {
        ItemStack itemStack = new ItemStack(Material.ELYTRA, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.format("&dElytra"));
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        elytra = itemStack;
    }

    /**
     * Elytrian elytra.
     *
     * @param player the player
     */
    public void elytrianElytra(Player player) {
        UUID playerUUID = player.getUniqueId();
        Location location = player.getLocation();
        World world = player.getWorld();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack prevChestplate = playerInventory.getChestplate();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
            if (prevChestplate != null && !prevChestplate.equals(elytra)) {
                if (playerInventory.firstEmpty() == -1) {
                    world.dropItem(location, prevChestplate);
                } else {
                    playerInventory.addItem(prevChestplate);
                }
            }
            playerInventory.setChestplate(elytra);
        }
    }

    /**
     * Elytrian launch into air.
     *
     * @param player the player
     */
    public void elytrianLaunchIntoAir(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (COOLDOWN.containsKey(playerUUID)) {
            long secondsLeft = ((COOLDOWN.get(playerUUID) / 1000) + COOLDOWNTIME - (System.currentTimeMillis() / 1000));

            if (secondsLeft > 0) {
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                        .toString()
                        .replace("%seconds_left%", String.valueOf(secondsLeft)));
            } else {
                player.setVelocity(new Vector(0, Config.ORIGINS_ELYTRIAN_ABILITY_Y_VELOCITY.toDouble(), 0));
                COOLDOWN.put(playerUUID, System.currentTimeMillis());
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                        .toString()
                        .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
            }
        } else {
            player.setVelocity(new Vector(0, Config.ORIGINS_ELYTRIAN_ABILITY_Y_VELOCITY.toDouble(), 0));
            COOLDOWN.put(playerUUID, System.currentTimeMillis());
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
        }
    }

    /**
     * Elytrian aerial combatant.
     *
     * @param event the event
     */
    @EventHandler
    public void elytrianAerialCombatant(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            UUID playerUUID = player.getUniqueId();
            double additionalDamage = baseDamage * 0.5;

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
                if (player.isGliding()) {
                    event.setDamage(baseDamage + additionalDamage);
                }
            }
        }
    }

    /**
     * Elytrian brittle bones.
     *
     * @param event the event
     */
    @EventHandler
    public void elytrianBrittleBones(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
                EntityDamageEvent.DamageCause damageCause = event.getCause();

                if (damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
                    double baseDamage = event.getDamage();
                    double halfBaseDamage = baseDamage * 0.5;

                    event.setDamage(baseDamage + halfBaseDamage);
                }
            }
        }
    }

    /**
     * Elytrian check player.
     *
     * @param event the event
     */
    @EventHandler
    public void elytrianCheckPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack prevChestplate = player.getInventory().getChestplate();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) != Origins.ELYTRIAN) {
            if (prevChestplate != null && prevChestplate.equals(elytra)) {
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
            }
        }
    }

    /**
     * Elytrian claustrophobia timer.
     *
     * @param player the player
     */
    public void elytrianClaustrophobiaTimer(Player player) {

        new BukkitRunnable() {
            int timeLeft = 30;

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                double y = location.getY();
                Block block = location.getBlock();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
