package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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

    private static Main plugin;

    /**
     * Instantiates a new Merling.
     *
     * @param plugin the plugin
     */
    public Merling(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Merling.plugin = plugin;
    }

    /**
     * Merling join.
     *
     * @param player the player
     */
    public static void merlingJoin(Player player) {
        merlingWaterBreathing(player);
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

            if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Merling air breathing timer.
     *
     * @param player the player
     */
    public static void merlingAirBreathingTimer(Player player) {

        new BukkitRunnable() {
            final BossBar bossBar = Bukkit.createBossBar("Air Breathing", BarColor.BLUE, BarStyle.SOLID);
            int merlingAirBreathingTime = 10;

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    bossBar.setProgress(merlingAirBreathingTime / 10D);
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
                                if (merlingAirBreathingTime <= 10 - 1) {
                                    bossBar.setColor(BarColor.GREEN);
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingWaterBreathing(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                bossBar.setColor(BarColor.BLUE);
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (merlingAirBreathingTime <= 10 - 1) {
                                    bossBar.setColor(BarColor.GREEN);
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingWaterBreathing(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                bossBar.setColor(BarColor.BLUE);
                                if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                    if (merlingAirBreathingTime <= 10 - 1) {
                                        bossBar.setColor(BarColor.GREEN);
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
    public static void merlingAirBreathingTimerZero(Player player) {

        new BukkitRunnable() {
            final BossBar bossBar = Bukkit.createBossBar("Air Breathing", BarColor.BLUE, BarStyle.SOLID);
            int merlingAirBreathingTime = 1;

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    bossBar.setProgress(merlingAirBreathingTime / 10D);
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
                                if (merlingAirBreathingTime <= 10 - 1) {
                                    bossBar.setColor(BarColor.GREEN);
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingWaterBreathing(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                bossBar.setColor(BarColor.BLUE);
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (merlingAirBreathingTime <= 10 - 1) {
                                    bossBar.setColor(BarColor.GREEN);
                                    merlingAirBreathingTime++;
                                    merlingAirBreathingTime++;
                                } else {
                                    merlingWaterBreathing(player);
                                    bossBar.setVisible(false);
                                    bossBar.removePlayer(player);
                                    this.cancel();
                                }
                            } else {
                                bossBar.setColor(BarColor.BLUE);
                                if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                    if (merlingAirBreathingTime <= 10 - 1) {
                                        bossBar.setColor(BarColor.GREEN);
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
    public static void merlingWaterBreathing(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
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
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    /**
     * Merling air damage.
     *
     * @param player the player
     */
    public static void merlingAirDamage(Player player) {

        new BukkitRunnable() {
            final BossBar bossBar = Bukkit.createBossBar(ChatUtils.format("&cWarning: You're Drowning!"), BarColor.RED, BarStyle.SOLID);

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.MERLING)) {
                    bossBar.setProgress(0);
                    if (!player.getWorld().hasStorm()) {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            merlingAirBreathingTimerZero(player);
                            bossBar.removePlayer(player);
                            this.cancel();
                        } else {
                            player.damage(1);
                            bossBar.addPlayer(player);
                        }
                    } else {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            merlingAirBreathingTimerZero(player);
                            bossBar.removePlayer(player);
                            this.cancel();
                        } else {
                            if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                player.damage(1);
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
                    bossBar.removePlayer(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
}
