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
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The type Blazeborn.
 *
 * @author SwagPannekaker
 */
public class Blazeborn implements Listener {

    private final Main plugin;
    private final List<Player> blazebornPlayersInWater = new ArrayList<>();
    private final List<Player> blazebornPlayersInAir = new ArrayList<>();

    /**
     * Instantiates a new Blazeborn.
     *
     * @param plugin the plugin
     */
    public Blazeborn(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        registerBlazebornWaterDamageListener();
        registerBlazebornAirEnterListener();
    }

    /**
     * Blazeborn join.
     *
     * @param player the player
     */
    public void blazebornJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
            player.setHealthScale(Config.ORIGINS_BLAZEBORN_MAX_HEALTH.toDouble());
            blazebornPlayersInWater.add(player);
            blazebornFlameParticles(player);
        }
    }

    /**
     * Register blazeborn water damage listener.
     */
    public void registerBlazebornWaterDamageListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!blazebornPlayersInWater.isEmpty()) {
                    for (int i = 0; i < blazebornPlayersInWater.size(); i++) {
                        Player player = blazebornPlayersInWater.get(i);
                        UUID playerUUID = player.getUniqueId();
                        Location location = player.getLocation();
                        Block block = location.getBlock();
                        Material material = block.getType();

                        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                            if (player.isOnline()) {
                                if (player.getWorld().hasStorm()) {
                                    if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                        damageBlazeborn(player, Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_AMOUNT.toDouble());
                                    } else if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                        damageBlazeborn(player, Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_AMOUNT.toDouble());
                                    } else {
                                        blazebornPlayersInWater.remove(player);
                                        blazebornPlayersInAir.add(player);
                                    }
                                } else {
                                    if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                        damageBlazeborn(player, Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_AMOUNT.toDouble());
                                    } else {
                                        blazebornPlayersInWater.remove(player);
                                        blazebornPlayersInAir.add(player);
                                    }
                                }
                            } else {
                                blazebornPlayersInWater.remove(player);
                            }
                        } else {
                            blazebornPlayersInWater.remove(player);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_DELAY.toLong(), Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_PERIOD_DELAY.toLong());
    }

    /**
     * Damage blazeborn.
     *
     * @param player the player
     * @param amount the amount
     */
    private void damageBlazeborn(Player player, double amount) {

        new BukkitRunnable() {

            @Override
            public void run() {
                player.damage(amount);
            }
        }.runTask(plugin);
     }

    /**
     * Register blazeborn air enter listener.
     */
    public void registerBlazebornAirEnterListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!blazebornPlayersInAir.isEmpty()) {
                    for (int i = 0; i < blazebornPlayersInAir.size(); i++) {
                        Player player = blazebornPlayersInAir.get(i);
                        UUID playerUUID = player.getUniqueId();
                        Location location = player.getLocation();
                        Block block = location.getBlock();
                        Material material = block.getType();

                        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                            if (player.isOnline()) {
                                if (player.getWorld().hasStorm()) {
                                    if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                        blazebornPlayersInAir.remove(player);
                                        blazebornPlayersInWater.add(player);
                                    } else if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                        blazebornPlayersInAir.remove(player);
                                        blazebornPlayersInWater.add(player);
                                    }
                                } else {
                                    if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                        blazebornPlayersInAir.remove(player);
                                        blazebornPlayersInWater.add(player);
                                    }
                                }
                            } else {
                                blazebornPlayersInAir.remove(player);
                            }
                        } else {
                            blazebornPlayersInAir.remove(player);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 5L);
    }

    /**
     * Blazeborn flame particles.
     *
     * @param player the player
     */
    public void blazebornFlameParticles(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                World world = player.getWorld();
                Location location = player.getLocation();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                    if (player.isOnline()) {
                        world.spawnParticle(Particle.SMALL_FLAME, location.add(0, 1, 0), 5);
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    /**
     * Blazeborn burning wrath.
     *
     * @param event the event
     */
    @EventHandler
    public void blazebornBurningWrath(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                if (player.getFireTicks() > 0) {
                    event.setDamage(baseDamage + 1.5);
                }
            }
        }
    }

    /**
     * Blazeborn snow ball damage.
     *
     * @param event the event
     */
    @EventHandler
    public void blazebornSnowBallDamage(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (target instanceof Player && damager instanceof Snowball) {
            Player targetPlayer = (Player) target;
            UUID targetPlayerUUID = targetPlayer.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(targetPlayerUUID) == Origins.BLAZEBORN) {
                event.setDamage(baseDamage + 1.5);
            }
        }
    }

    /**
     * Blazeborn damage immunities.
     *
     * @param event the event
     */
    @EventHandler
    public void blazebornDamageImmunities(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();

            if (player != null) {
                UUID playerUUID = player.getUniqueId();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                    if (damageCause == EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.FIRE || damageCause == EntityDamageEvent.DamageCause.FIRE_TICK || damageCause == EntityDamageEvent.DamageCause.HOT_FLOOR || damageCause == EntityDamageEvent.DamageCause.POISON || damageCause == EntityDamageEvent.DamageCause.STARVATION) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Blazeborn potion drinking damage.
     *
     * @param event the event
     */
    @EventHandler
    public void blazebornPotionDrinkingDamage(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack itemStack = event.getItem();
        Material material = itemStack.getType();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
            if (material == Material.POTION) {
                player.damage(2);
            }
        }
    }

    /**
     * Blazeborn splash potion damage.
     *
     * @param event the event
     */
    @EventHandler
    public void blazebornSplashPotionDamage(PotionSplashEvent event) {
        Collection<LivingEntity> livingEntities = event.getAffectedEntities();

        for (LivingEntity livingEntity : livingEntities) {
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                UUID playerUUID = player.getUniqueId();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.BLAZEBORN) {
                    player.damage(2);
                }
            }
        }
    }

    /**
     * Blazeborn nether spawn.
     *
     * @param player the player
     */
    public void blazebornNetherSpawn(Player player) {

    }
}