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

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
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

import java.util.*;

/**
 * The type Blazeborn.
 *
 * @author SwagPannekaker
 */
public class Blazeborn extends Origin implements Listener {

    private final OriginsBukkit plugin;
    private final List<Player> blazebornPlayersInWater = new ArrayList<>();
    private final List<Player> blazebornPlayersInAir = new ArrayList<>();

    /**
     * Instantiates a new Blazeborn.
     *
     * @param plugin the plugin
     */
    public Blazeborn(OriginsBukkit plugin) {
        super(Config.ORIGINS_BLAZEBORN_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
        this.plugin = plugin;
        init();
    }

    /**
     * Gets origin identifier.
     *
     * @return the origin identifier
     */
    @Override
    public String getOriginIdentifier() {
        return "Blazeborn";
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    @Override
    public String getAuthor() {
        return "SwagPannekaker";
    }

    /**
     * Gets origin icon.
     *
     * @return the origin icon
     */
    @Override
    public Material getOriginIcon() {
        return Material.BLAZE_POWDER;
    }

    /**
     * Is origin icon glowing boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean isOriginIconGlowing() {
        return false;
    }

    /**
     * Gets origin title.
     *
     * @return the origin title
     */
    @Override
    public String getOriginTitle() {
        return Lang.BLAZEBORN_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.BLAZEBORN_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        registerBlazebornWaterDamageListener();
        registerBlazebornAirEnterListener();
    }

    /**
     * Blazeborn join.
     *
     * @param event the event
     */
    @EventHandler
    private void blazebornJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.BLAZEBORN.toString())) {
            player.setHealthScale(Config.ORIGINS_BLAZEBORN_MAX_HEALTH.toDouble());
            blazebornPlayersInWater.add(player);
            blazebornNetherSpawn(player);
            blazebornFlameParticles(player);
        }
    }

    /**
     * Register blazeborn water damage listener.
     */
    private void registerBlazebornWaterDamageListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!blazebornPlayersInWater.isEmpty()) {
                    for (int i = 0; i < blazebornPlayersInWater.size(); i++) {
                        Player player = blazebornPlayersInWater.get(i);
                        UUID playerUUID = player.getUniqueId();
                        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                        Location location = player.getLocation();
                        Block block = location.getBlock();
                        Material material = block.getType();

                        if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void registerBlazebornAirEnterListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!blazebornPlayersInAir.isEmpty()) {
                    for (int i = 0; i < blazebornPlayersInAir.size(); i++) {
                        Player player = blazebornPlayersInAir.get(i);
                        UUID playerUUID = player.getUniqueId();
                        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                        Location location = player.getLocation();
                        Block block = location.getBlock();
                        Material material = block.getType();

                        if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornFlameParticles(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                World world = player.getWorld();
                Location location = player.getLocation();

                if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornBurningWrath(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

            if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornSnowBallDamage(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (target instanceof Player && damager instanceof Snowball) {
            Player targetPlayer = (Player) target;
            UUID targetPlayerUUID = targetPlayer.getUniqueId();
            String targetPlayerOrigin = plugin.getStorageUtils().getPlayerOrigin(targetPlayerUUID);

            if (Objects.equals(targetPlayerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornDamageImmunities(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();

            if (player != null) {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

                if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornPotionDrinkingDamage(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
        ItemStack itemStack = event.getItem();
        Material material = itemStack.getType();

        if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornSplashPotionDamage(PotionSplashEvent event) {
        Collection<LivingEntity> livingEntities = event.getAffectedEntities();

        for (LivingEntity livingEntity : livingEntities) {
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

                if (Objects.equals(playerOrigin, Origins.BLAZEBORN.toString())) {
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
    private void blazebornNetherSpawn(Player player) {
        World world = player.getWorld();
        Location location = new Location(world, -77, 72, 238);
        player.teleport(location);
    }
}