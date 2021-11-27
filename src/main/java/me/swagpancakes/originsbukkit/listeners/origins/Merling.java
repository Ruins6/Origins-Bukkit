/*
 * Origins-Bukkit - Origins for Bukkit and forks of Bukkit.
 * Copyright (C) 2021 SwagPannekaker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit.listeners.origins;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.MerlingTimerSessionData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Merling.
 *
 * @author SwagPannekaker
 */
public class Merling extends Origin implements Listener {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Merling.
     *
     * @param plugin the plugin
     */
    public Merling(OriginsBukkit plugin) {
        super(Config.ORIGINS_MERLING_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
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
        return "Merling";
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
        return Material.COD;
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
        return Lang.MERLING_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.MERLING_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        registerMerlingBlockDiggingPacketListener();
        registerMerlingMovePacketListener();
    }

    /**
     * Merling join.
     *
     * @param event the event
     */
    @EventHandler
    private void merlingJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.MERLING.toString())) {
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
    private int calculatePercentage(int obtained, int total) {
        return obtained * 100 / total;
    }

    /**
     * Merling underwater breathing.
     *
     * @param event the event
     */
    @EventHandler
    private void merlingUnderwaterBreathing(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

            if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
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
    private void merlingAirBreathingTimer(Player player, int timeLeft) {

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
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (player.isOnline()) {
                        bossBar.setTitle(Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                                .toString()
                                .replace("%time-left%", String.valueOf(merlingAirBreathingTime)));
                        bossBar.setProgress(merlingAirBreathingTime / Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toDouble());

                        if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) == null) {
                            plugin.getStorageUtils().createMerlingTimerSessionData(playerUUID, merlingAirBreathingTime);
                        } else {
                            plugin.getStorageUtils().updateMerlingTimerSessionData(playerUUID, new MerlingTimerSessionData(playerUUID, merlingAirBreathingTime));
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
                                    if (merlingAirBreathingTime < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime+=2;
                                    } else {
                                        if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                            plugin.getStorageUtils().deleteMerlingTimerSessionData(playerUUID);
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
                                    if (merlingAirBreathingTime < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                        bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                        bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                        merlingAirBreathingTime+=2;
                                    } else {
                                        if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                            plugin.getStorageUtils().deleteMerlingTimerSessionData(playerUUID);
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
                                        if (merlingAirBreathingTime < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                            bossBar.setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                            bossBar.setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                            merlingAirBreathingTime+=2;
                                        } else {
                                            if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                                plugin.getStorageUtils().deleteMerlingTimerSessionData(playerUUID);
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
                    if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                        plugin.getStorageUtils().deleteMerlingTimerSessionData(playerUUID);
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
    private void merlingWaterBreathing(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();
                int timeLeft = plugin.getStorageUtils().getMerlingTimerSessionDataTimeLeft(playerUUID);

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (player.isOnline()) {
                        if (!player.getWorld().hasStorm()) {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                    if (plugin.getStorageUtils().getMerlingTimerSessionDataTimeLeft(playerUUID) != 0) {
                                        merlingAirBreathingTimer(player, timeLeft);
                                    } else {
                                        merlingAirDamage(player);
                                    }
                                } else {
                                    merlingAirBreathingTimer(player, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                }
                                this.cancel();
                            } else {
                                if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                    this.cancel();
                                }
                            }
                        } else {
                            if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                        if (plugin.getStorageUtils().getMerlingTimerSessionDataTimeLeft(playerUUID) != 0) {
                                            merlingAirBreathingTimer(player, timeLeft);
                                        } else {
                                            merlingAirDamage(player);
                                        }
                                    } else {
                                        merlingAirBreathingTimer(player, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                    }
                                    this.cancel();
                                } else {
                                    if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
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
    private void merlingAirDamage(Player player) {

        new BukkitRunnable() {
            final BossBar bossBar = Bukkit.createBossBar(
                    Lang.MERLING_BOSSBAR_DROWNING_TITLE.toString(),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DROWNING.toBarColor(),
                    Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DROWNING.toBarStyle());

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();
                int timeLeft = plugin.getStorageUtils().getMerlingTimerSessionDataTimeLeft(playerUUID);

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (player.isOnline()) {
                        bossBar.setProgress(0);
                        if (!player.getWorld().hasStorm()) {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                }
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                damageMerling(player, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                bossBar.addPlayer(player);
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
                                    merlingAirBreathingTimer(player, timeLeft);
                                }
                                bossBar.removePlayer(player);
                                this.cancel();
                            } else {
                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                    damageMerling(player, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                    bossBar.addPlayer(player);
                                } else {
                                    if (plugin.getStorageUtils().findMerlingTimerSessionData(playerUUID) != null) {
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
        }.runTaskTimerAsynchronously(plugin, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_DELAY.toLong(), Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_PERIOD_DELAY.toLong());
    }

    /**
     * Damage merling.
     *
     * @param player the player
     * @param amount the amount
     */
    private void damageMerling(Player player, double amount) {

        new BukkitRunnable() {

            @Override
            public void run() {
                player.damage(amount);
            }
        }.runTask(plugin);
    }

    /**
     * On merling impaling damage.
     *
     * @param event the event
     */
    @EventHandler
    private void onMerlingImpalingDamage(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (target instanceof Player && damager instanceof LivingEntity) {
            Player targetPlayer = (Player) target;
            LivingEntity livingDamager = (LivingEntity) damager;
            UUID targetPlayerUUID = targetPlayer.getUniqueId();
            String targetPlayerOrigin = plugin.getStorageUtils().getPlayerOrigin(targetPlayerUUID);
            EntityEquipment entityEquipment = livingDamager.getEquipment();

            if (entityEquipment != null) {
                ItemStack itemStack = livingDamager.getEquipment().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();

                if (Objects.equals(targetPlayerOrigin, Origins.MERLING.toString())) {
                    if (itemMeta != null && itemMeta.hasEnchant(Enchantment.IMPALING)) {
                        int enchantLevel = itemMeta.getEnchantLevel(Enchantment.IMPALING);

                        event.setDamage(baseDamage + (2.5 * enchantLevel));
                    }
                }
            }
        }
    }

    /**
     * Register merling block digging packet listener.
     */
    private void registerMerlingBlockDiggingPacketListener() {
        plugin.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                Player player = event.getPlayer();
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = Merling.this.plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location playerLocation = player.getLocation();

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (playerLocation.getBlock().isLiquid() && playerLocation.add(0, 1, 0).getBlock().isLiquid()) {
                        if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 20, false, false));
                                }
                            }.runTask(plugin);
                        } else if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(plugin);
                        } else if (digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(plugin);
                        }
                    }
                }
            }
        });
    }

    /**
     * Register merling move packet listener.
     */
    private void registerMerlingMovePacketListener() {
        plugin.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = Merling.this.plugin.getStorageUtils().getPlayerOrigin(playerUUID);

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (player.isInWater()) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0 , false, false));
                            }
                        }.runTask(plugin);
                    } else {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }.runTask(plugin);
                    }
                }
            }
        });
    }
}
