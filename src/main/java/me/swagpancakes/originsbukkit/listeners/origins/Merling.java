package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
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

import java.util.Objects;
import java.util.UUID;

/**
 * The type Merling.
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
            player.setHealthScale(Config.ORIGINS_MERLING_MAX_HEALTH.toDouble());
            merlingWaterBreathing(player);
        }
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

            if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Merling air breathing timer.
     *
     * @param player the player
     */
    public void merlingAirBreathingTimer(Player player) {

        new BukkitRunnable() {
            int merlingAirBreathingTime = Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt();
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

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    if (player.isOnline()) {
                        bossBar.setTitle(Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                                .toString()
                                .replace("%time-left%", String.valueOf(merlingAirBreathingTime)));
                        bossBar.setProgress(merlingAirBreathingTime / Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toDouble());
                        if (merlingAirBreathingTime <= 0) {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingAirDamage(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                        merlingAirDamage(player);
                                        bossBar.setVisible(false);
                                        bossBar.removePlayer(player);
                                        this.cancel();
                                    } else {
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    }
                                }
                            }
                        } else {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    if (merlingAirBreathingTime <= Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt() - 1) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    } else {
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
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    } else {
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
                                            merlingAirBreathingTime++;
                                            merlingAirBreathingTime++;
                                        } else {
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
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * Merling air breathing timer zero.
     *
     * @param player the player
     */
    public void merlingAirBreathingTimerZero(Player player) {

        new BukkitRunnable() {
            int merlingAirBreathingTime = 1;
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

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    if (player.isOnline()) {
                        bossBar.setTitle(Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                                .toString()
                                .replace("%time-left%", String.valueOf(merlingAirBreathingTime)));
                        bossBar.setProgress(merlingAirBreathingTime / Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toDouble());
                        if (merlingAirBreathingTime <= 0) {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingAirDamage(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                        merlingAirDamage(player);
                                        bossBar.setVisible(false);
                                        bossBar.removePlayer(player);
                                        this.cancel();
                                    } else {
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    }
                                }
                            }
                        } else {
                            if (!player.getWorld().hasStorm()) {
                                if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                    if (merlingAirBreathingTime <= Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt() - 1) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    } else {
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
                                        merlingAirBreathingTime++;
                                        merlingAirBreathingTime++;
                                    } else {
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
                                            merlingAirBreathingTime++;
                                            merlingAirBreathingTime++;
                                        } else {
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
        }.runTaskTimer(plugin, 0L, 20L);
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

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    if (player.isOnline()) {
                        if (!player.getWorld().hasStorm()) {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                merlingAirBreathingTimer(player);
                                this.cancel();
                            }
                        } else {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    merlingAirBreathingTimer(player);
                                    this.cancel();
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
        }.runTaskTimer(plugin, 0L, 5L);
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

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    if (player.isOnline()) {
                        bossBar.setProgress(0);
                        if (!player.getWorld().hasStorm()) {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                merlingAirBreathingTimerZero(player);
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                player.damage(Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                bossBar.addPlayer(player);
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                merlingAirBreathingTimerZero(player);
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    player.damage(Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                    bossBar.addPlayer(player);
                                } else {
                                    merlingAirBreathingTimerZero(player);
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
