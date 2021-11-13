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
import me.swagpancakes.originsbukkit.storage.MerlingTimerSessionData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * The type Merling.
 *
 * @author SwagPannekaker
 */
public class Merling implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Merling.
     *
     * @param plugin the plugin
     */
    public Merling(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Merling join.
     *
     * @param player the player
     */
    public void merlingJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.MERLING) {
            player.setHealthScale(Config.ORIGINS_MERLING_MAX_HEALTH.toDouble());
            merlingWaterBreathing(player);
        }
    }

    /**
     * Calculate percentage int.
     *
     * @param obtained the obtained
     * @param total    the total
     *
     * @return the int
     */
    public int calculatePercentage(int obtained, int total) {
        return obtained * 100 / total;
    }

    /**
     * Merling underwater breathing.
     *
     * @param event the event
     */
    @EventHandler
    public void merlingUnderwaterBreathing(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.MERLING) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Merling air breathing timer.
     *
     * @param player   the player
     * @param timeLeft the time left
     */
    public void merlingAirBreathingTimer(Player player, int timeLeft) {

        new BukkitRunnable() {
            int merlingAirBreathingTime = timeLeft;
            final BossBar bossBar = Bukkit.createBossBar(
                    Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                            .toString()
                            .replace("%time-left%", String.valueOf(merlingAirBreathingTime)),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor(),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.MERLING) {
                    if (player.isOnline()) {
                        bossBar.setTitle(Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                                .toString()
                                .replace("%time-left%", String.valueOf(merlingAirBreathingTime)));
                        bossBar.setProgress(merlingAirBreathingTime / Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toDouble());

                        if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) == null) {
                            plugin.storageUtils.createMerlingTimerSessionData(playerUUID, merlingAirBreathingTime);
                        } else {
                            plugin.storageUtils.updateMerlingTimerSessionData(playerUUID, new MerlingTimerSessionData(playerUUID, merlingAirBreathingTime));
                        }

                        if (merlingAirBreathingTime <= 0) {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime+=2;
                                } else {
                                    merlingAirDamage(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime+=2;
                                } else {
                                    if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                        merlingAirDamage(player);
                                        bossBar.setVisible(false);
                                        bossBar.removePlayer(player);
                                        this.cancel();
                                    } else {
                                        merlingAirBreathingTime+=2;
                                    }
                                }
                            }
                        } else {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    if (merlingAirBreathingTime <= Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt() - 1) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime+=2;
                                    } else {
                                        if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                            plugin.storageUtils.deleteMerlingTimerSessionData(playerUUID);
                                        }
                                        merlingWaterBreathing(player);
                                        bossBar.setVisible(false);
                                        bossBar.removePlayer(player);
                                        this.cancel();
                                    }
                                } else {
                                    bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor());
                                    bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());
                                }
                            } else {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    if (merlingAirBreathingTime <= Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt() - 1) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime+=2;
                                    } else {
                                        if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                            plugin.storageUtils.deleteMerlingTimerSessionData(playerUUID);
                                        }
                                        merlingWaterBreathing(player);
                                        bossBar.setVisible(false);
                                        bossBar.removePlayer(player);
                                        this.cancel();
                                    }
                                } else {
                                    bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor());
                                    bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());
                                    if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                        if (merlingAirBreathingTime <= Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt() - 1) {
                                            bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                            bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                            merlingAirBreathingTime+=2;
                                        } else {
                                            if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                                plugin.storageUtils.deleteMerlingTimerSessionData(playerUUID);
                                            }
                                            merlingWaterBreathing(player);
                                            bossBar.setVisible(false);
                                            bossBar.removePlayer(player);
                                            this.cancel();
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                        plugin.storageUtils.deleteMerlingTimerSessionData(playerUUID);
                    }
                    bossBar.setVisible(false);
                    bossBar.removePlayer(player);
                    this.cancel();
                }
                if (bossBar.isVisible()) {
                    bossBar.setVisible(true);
                    bossBar.addPlayer(player);
                }
                if (merlingAirBreathingTime > 0) {
                    merlingAirBreathingTime--;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    /**
     * Merling water breathing.
     *
     * @param player the player
     */
    public void merlingWaterBreathing(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();
                int timeLeft = plugin.storageUtils.getMerlingTimerSessionDataTimeLeft(playerUUID);

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.MERLING) {
                    if (player.isOnline()) {
                        if (!player.getWorld().hasStorm()) {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                    if (plugin.storageUtils.getMerlingTimerSessionDataTimeLeft(playerUUID) != 0) {
                                        merlingAirBreathingTimer(player, timeLeft);
                                    } else {
                                        merlingAirDamage(player);
                                    }
                                } else {
                                    merlingAirBreathingTimer(player, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                }
                                this.cancel();
                            } else {
                                if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                    this.cancel();
                                }
                            }
                        } else {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                        if (plugin.storageUtils.getMerlingTimerSessionDataTimeLeft(playerUUID) != 0) {
                                            merlingAirBreathingTimer(player, timeLeft);
                                        } else {
                                            merlingAirDamage(player);
                                        }
                                    } else {
                                        merlingAirBreathingTimer(player, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                    }
                                    this.cancel();
                                } else {
                                    if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                        merlingAirBreathingTimer(player, timeLeft);
                                        this.cancel();
                                    }
                                }
                            }
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 5L);
    }

    /**
     * Merling air damage.
     *
     * @param player the player
     */
    public void merlingAirDamage(Player player) {

        new BukkitRunnable() {
            final BossBar bossBar = Bukkit.createBossBar(
                    Lang.MERLING_BOSSBAR_DROWNING_TITLE.toString(),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DROWNING.toBarColor(),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DROWNING.toBarStyle());

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();
                int timeLeft = plugin.storageUtils.getMerlingTimerSessionDataTimeLeft(playerUUID);

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.MERLING) {
                    if (player.isOnline()) {
                        bossBar.setProgress(0);
                        if (!player.getWorld().hasStorm()) {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                }
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                player.damage(Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                bossBar.addPlayer(player);
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                }
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    player.damage(Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                    bossBar.addPlayer(player);
                                } else {
                                    if (plugin.storageUtils.findMerlingTimerSessionData(playerUUID) != null) {
                                        merlingAirBreathingTimer(player, timeLeft);
                                    }
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            }
                        }
                        if (bossBar.isVisible()) {
                            bossBar.setVisible(true);
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    bossBar.removePlayer(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_DELAY.toLong(), Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_PERIOD_DELAY.toLong());
    }
}
