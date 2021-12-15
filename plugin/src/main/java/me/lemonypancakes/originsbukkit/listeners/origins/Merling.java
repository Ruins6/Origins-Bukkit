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
package me.lemonypancakes.originsbukkit.listeners.origins;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.lemonypancakes.originsbukkit.api.events.OriginChangeEvent;
import me.lemonypancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.lemonypancakes.originsbukkit.api.util.Origin;
import me.lemonypancakes.originsbukkit.api.wrappers.OriginPlayer;
import me.lemonypancakes.originsbukkit.enums.Config;
import me.lemonypancakes.originsbukkit.enums.Impact;
import me.lemonypancakes.originsbukkit.enums.Lang;
import me.lemonypancakes.originsbukkit.enums.Origins;
import me.lemonypancakes.originsbukkit.storage.wrappers.MerlingTimerSessionDataWrapper;
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

import java.util.*;

/**
 * The type Merling.
 *
 * @author SwagPannekaker
 */
public class Merling extends Origin implements Listener {

    private final OriginListenerHandler originListenerHandler;
    private final Map<UUID, Integer> merlingAirBreathing = new HashMap<>();
    private final Map<UUID, BossBar> merlingBossbarMap = new HashMap<>();
    private final List<UUID> merlingWaterBreathing = new ArrayList<>();
    private final List<UUID> merlingAirDamage = new ArrayList<>();

    /**
     * Gets origin listener handler.
     *
     * @return the origin listener handler
     */
    public OriginListenerHandler getOriginListenerHandler() {
        return originListenerHandler;
    }

    /**
     * Gets merling air breathing.
     *
     * @return the merling air breathing
     */
    public Map<UUID, Integer> getMerlingAirBreathing() {
        return merlingAirBreathing;
    }

    /**
     * Gets merling bossbar map.
     *
     * @return the merling bossbar map
     */
    public Map<UUID, BossBar> getMerlingBossbarMap() {
        return merlingBossbarMap;
    }

    /**
     * Gets merling water breathing.
     *
     * @return the merling water breathing
     */
    public List<UUID> getMerlingWaterBreathing() {
        return merlingWaterBreathing;
    }

    /**
     * Gets merling air damage.
     *
     * @return the merling air damage
     */
    public List<UUID> getMerlingAirDamage() {
        return merlingAirDamage;
    }

    /**
     * Instantiates a new Merling.
     *
     * @param originListenerHandler the origin listener handler
     */
    public Merling(OriginListenerHandler originListenerHandler) {
        super(Config.ORIGINS_MERLING_MAX_HEALTH.toDouble(),
                Config.ORIGINS_MERLING_WALK_SPEED.toFloat(),
                Config.ORIGINS_MERLING_FLY_SPEED.toFloat());
        this.originListenerHandler = originListenerHandler;
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
     * Gets impact.
     *
     * @return the impact
     */
    @Override
    public Impact getImpact() {
        return Impact.HIGH;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    @Override
    public String getAuthor() {
        return "LemonyPancakes";
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
        getOriginListenerHandler()
                .getListenerHandler()
                .getPlugin()
                .getServer()
                .getPluginManager()
                .registerEvents(this, getOriginListenerHandler()
                        .getListenerHandler()
                        .getPlugin());
        registerOrigin(this);
        registerAirBreathingListener();
        registerWaterBreathingListener();
        registerMerlingAirDamageListener();
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
        UUID playerUUID = player.getUniqueId();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.MERLING.toString())) {
            getMerlingWaterBreathing().add(playerUUID);
        }
    }

    /**
     * On origin change.
     *
     * @param event the event
     */
    @EventHandler
    private void onOriginChange(OriginChangeEvent event) {
        Player player = event.getPlayer();
        String oldOrigin = event.getOldOrigin();

        if (Objects.equals(oldOrigin, Origins.MERLING.toString())) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
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
            OriginPlayer originPlayer = new OriginPlayer(player);
            String playerOrigin = originPlayer.getOrigin();

            if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Register air breathing listener.
     */
    private void registerAirBreathingListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!getMerlingAirBreathing().isEmpty()) {
                    for (Map.Entry<UUID, Integer> entry : getMerlingAirBreathing().entrySet()) {
                        UUID key = entry.getKey();
                        int value = entry.getValue();
                        Player player = Bukkit.getPlayer(key);

                        if (player != null) {
                            String replace = Lang.MERLING_BOSSBAR_AIR_BREATHING_TIMER_TITLE
                                    .toString()
                                    .replace("%time-left%", String.valueOf(value));
                            UUID playerUUID = player.getUniqueId();
                            if (!getMerlingBossbarMap().containsKey(key)) {
                                BossBar bossBar = Bukkit.createBossBar(
                                        replace,
                                        Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor(),
                                        Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());
                                getMerlingBossbarMap().put(key, bossBar);
                            }
                            OriginPlayer originPlayer = new OriginPlayer(player);
                            String playerOrigin = originPlayer.getOrigin();
                            Location location = player.getLocation();
                            Block block = location.getBlock();
                            Material material = block.getType();

                            if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                                if (player.isOnline()) {
                                    getMerlingBossbarMap().get(key).setTitle(replace);
                                    getMerlingBossbarMap().get(key).setProgress(value / Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toDouble());

                                    if (originPlayer.findMerlingTimerSessionData() == null) {
                                        originPlayer.createMerlingTimerSessionData(value);
                                    } else {
                                        originPlayer.updateMerlingTimerSessionData(
                                                new MerlingTimerSessionDataWrapper(playerUUID, value));
                                    }
                                    if (value <= 0) {
                                        if (!player.getWorld().hasStorm()) {
                                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                                value += 2;
                                                getMerlingAirBreathing().put(playerUUID, value);
                                            } else {
                                                getMerlingAirDamage().add(playerUUID);
                                                getMerlingAirBreathing().remove(playerUUID);
                                                getMerlingBossbarMap().get(key).setVisible(false);
                                                getMerlingBossbarMap().get(key).removePlayer(player);
                                            }
                                        } else {
                                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                                getMerlingAirBreathing().put(playerUUID, value + 2);
                                            } else {
                                                if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                                    getMerlingAirDamage().add(playerUUID);
                                                    getMerlingAirBreathing().remove(playerUUID);
                                                    getMerlingBossbarMap().get(key).setVisible(false);
                                                    getMerlingBossbarMap().get(key).removePlayer(player);
                                                } else {
                                                    value += 2;
                                                    getMerlingAirBreathing().put(playerUUID, value);
                                                }
                                            }
                                        }
                                    } else {
                                        if (!player.getWorld().hasStorm()) {
                                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                                if (value < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                                    getMerlingBossbarMap().get(key).setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                                    getMerlingBossbarMap().get(key).setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                                    value += 2;
                                                    getMerlingAirBreathing().put(playerUUID, value);
                                                } else {
                                                    if (originPlayer.findMerlingTimerSessionData() != null) {
                                                        originPlayer.deleteMerlingTimerSessionData();
                                                    }
                                                    getMerlingWaterBreathing().add(playerUUID);
                                                    getMerlingAirBreathing().remove(playerUUID);
                                                    getMerlingBossbarMap().get(key).setVisible(false);
                                                    getMerlingBossbarMap().get(key).removePlayer(player);
                                                }
                                            } else {
                                                getMerlingBossbarMap().get(key).setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor());
                                                getMerlingBossbarMap().get(key).setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());
                                            }
                                        } else {
                                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                                if (value < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                                    getMerlingBossbarMap().get(key).setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                                    getMerlingBossbarMap().get(key).setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                                    value += 2;
                                                    getMerlingAirBreathing().put(playerUUID, value);
                                                } else {
                                                    if (originPlayer.findMerlingTimerSessionData() != null) {
                                                        originPlayer.deleteMerlingTimerSessionData();
                                                    }
                                                    getMerlingWaterBreathing().add(playerUUID);
                                                    getMerlingAirBreathing().remove(playerUUID);
                                                    getMerlingBossbarMap().get(key).setVisible(false);
                                                    getMerlingBossbarMap().get(key).removePlayer(player);
                                                }
                                            } else {
                                                getMerlingBossbarMap().get(key).setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DECREASE.toBarColor());
                                                getMerlingBossbarMap().get(key).setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DECREASE.toBarStyle());
                                                if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                                    if (value < Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt()) {
                                                        getMerlingBossbarMap().get(key).setColor(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_INCREASE.toBarColor());
                                                        getMerlingBossbarMap().get(key).setStyle(Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_INCREASE.toBarStyle());
                                                        value += 2;
                                                        getMerlingAirBreathing().put(playerUUID, value);
                                                    } else {
                                                        if (originPlayer.findMerlingTimerSessionData() != null) {
                                                            originPlayer.deleteMerlingTimerSessionData();
                                                        }
                                                        getMerlingWaterBreathing().add(playerUUID);
                                                        getMerlingAirBreathing().remove(playerUUID);
                                                        getMerlingBossbarMap().get(key).setVisible(false);
                                                        getMerlingBossbarMap().get(key).removePlayer(player);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (getMerlingBossbarMap().containsKey(key)) {
                                        getMerlingBossbarMap().get(key).addPlayer(player);
                                    }
                                    if (getMerlingAirBreathing().containsKey(key)) {
                                        getMerlingBossbarMap().get(key).setVisible(true);
                                    }
                                    if (value > 0 && getMerlingAirBreathing().containsKey(key)) {
                                        value--;
                                        getMerlingAirBreathing().put(playerUUID, value);
                                    }
                                } else {
                                    getMerlingAirBreathing().remove(playerUUID);
                                }
                            } else {
                                if (originPlayer.findMerlingTimerSessionData() != null) {
                                    originPlayer.deleteMerlingTimerSessionData();
                                }
                                getMerlingAirBreathing().remove(playerUUID);
                                getMerlingBossbarMap().get(key).setVisible(false);
                                getMerlingBossbarMap().get(key).removePlayer(player);
                            }
                            if (getMerlingBossbarMap().get(key).isVisible()) {
                                getMerlingBossbarMap().get(key).setVisible(true);
                                getMerlingBossbarMap().get(key).addPlayer(player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(getOriginListenerHandler()
                .getListenerHandler()
                .getPlugin(), 0L, 20L);
    }

    /**
     * Register water breathing listener.
     */
    private void registerWaterBreathingListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!getMerlingWaterBreathing().isEmpty()) {
                    for (int i = 0; i < getMerlingWaterBreathing().size(); i++) {
                        Player player = Bukkit.getPlayer(getMerlingWaterBreathing().get(i));

                        if (player != null) {
                            UUID playerUUID = player.getUniqueId();
                            OriginPlayer originPlayer = new OriginPlayer(player);
                            String playerOrigin = originPlayer.getOrigin();
                            Location location = player.getLocation();
                            Block block = location.getBlock();
                            Material material = block.getType();
                            int timeLeft = originPlayer.getMerlingTimerSessionDataTimeLeft();

                            if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                                if (player.isOnline()) {
                                    if (!player.getWorld().hasStorm()) {
                                        if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                            if (originPlayer.findMerlingTimerSessionData() != null) {
                                                if (timeLeft != 0) {
                                                    getMerlingAirBreathing().put(playerUUID, timeLeft);
                                                    getMerlingWaterBreathing().remove(playerUUID);
                                                } else {
                                                    getMerlingAirDamage().add(playerUUID);
                                                }
                                            } else {
                                                getMerlingAirBreathing().put(playerUUID, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                            }
                                            getMerlingWaterBreathing().remove(playerUUID);
                                        }
                                    } else {
                                        if (!(player.isInWater() || material == Material.WATER_CAULDRON)) {
                                            if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                                if (originPlayer.findMerlingTimerSessionData() != null) {
                                                    if (timeLeft != 0) {
                                                        getMerlingAirBreathing().put(playerUUID, timeLeft);
                                                    } else {
                                                        getMerlingAirDamage().add(playerUUID);
                                                    }
                                                } else {
                                                    getMerlingAirBreathing().put(playerUUID, Config.ORIGINS_MERLING_AIR_BREATHING_MAX_TIME.toInt());
                                                }
                                                getMerlingWaterBreathing().remove(playerUUID);
                                            } else {
                                                if (originPlayer.findMerlingTimerSessionData() != null) {
                                                    getMerlingAirBreathing().put(playerUUID, timeLeft);
                                                    getMerlingWaterBreathing().remove(playerUUID);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    getMerlingWaterBreathing().remove(playerUUID);
                                }
                            } else {
                                getMerlingWaterBreathing().remove(playerUUID);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(getOriginListenerHandler()
                .getListenerHandler()
                .getPlugin(), 0L, 5L);
    }

    /**
     * Register merling air damage listener.
     */
    private void registerMerlingAirDamageListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!getMerlingAirDamage().isEmpty()) {
                    for (int i = 0; i < getMerlingAirDamage().size(); i++) {
                        Player player = Bukkit.getPlayer(getMerlingAirDamage().get(i));

                        if (player != null) {
                            UUID playerUUID = player.getUniqueId();
                            if (!getMerlingBossbarMap().containsKey(playerUUID)) {
                                BossBar bossBar = Bukkit.createBossBar(
                                        Lang.MERLING_BOSSBAR_DROWNING_TITLE.toString(),
                                        Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DROWNING.toBarColor(),
                                        Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DROWNING.toBarStyle());
                                getMerlingBossbarMap().put(playerUUID, bossBar);
                            } else {
                                if (!getMerlingBossbarMap().get(playerUUID).getTitle().equals(Lang.MERLING_BOSSBAR_DROWNING_TITLE.toString())) {
                                    BossBar bossBar = Bukkit.createBossBar(
                                            Lang.MERLING_BOSSBAR_DROWNING_TITLE.toString(),
                                            Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARCOLOR_ON_DROWNING.toBarColor(),
                                            Config.ORIGINS_MERLING_BOSSBAR_AIR_BREATHING_BARSTYLE_ON_DROWNING.toBarStyle());
                                    getMerlingBossbarMap().put(playerUUID, bossBar);
                                }
                            }
                            OriginPlayer originPlayer = new OriginPlayer(player);
                            String playerOrigin = originPlayer.getOrigin();
                            Location location = player.getLocation();
                            Block block = location.getBlock();
                            Material material = block.getType();
                            int timeLeft = originPlayer.getMerlingTimerSessionDataTimeLeft();

                            if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                                if (player.isOnline()) {
                                    getMerlingBossbarMap().get(playerUUID).setProgress(0);
                                    if (!player.getWorld().hasStorm()) {
                                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                            if (originPlayer.findMerlingTimerSessionData() != null) {
                                                getMerlingAirBreathing().put(playerUUID, timeLeft);
                                            }
                                            getMerlingAirDamage().remove(playerUUID);
                                        } else {
                                            damageMerling(player, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                            getMerlingBossbarMap().get(playerUUID).addPlayer(player);
                                        }
                                    } else {
                                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                            if (originPlayer.findMerlingTimerSessionData() != null) {
                                                getMerlingAirBreathing().put(playerUUID, timeLeft);
                                            }
                                            getMerlingAirDamage().remove(playerUUID);
                                        } else {
                                            if (!(location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY())) {
                                                damageMerling(player, Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_AMOUNT.toDouble());
                                                getMerlingBossbarMap().get(playerUUID).addPlayer(player);
                                            } else {
                                                if (originPlayer.findMerlingTimerSessionData() != null) {
                                                    getMerlingAirBreathing().put(playerUUID, timeLeft);
                                                }
                                                getMerlingAirDamage().remove(playerUUID);
                                            }
                                        }
                                    }
                                    if (getMerlingBossbarMap().get(playerUUID).isVisible()) {
                                        getMerlingBossbarMap().get(playerUUID).setVisible(true);
                                    }
                                } else {
                                    getMerlingAirDamage().remove(playerUUID);
                                }
                            } else {
                                getMerlingAirDamage().remove(playerUUID);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(getOriginListenerHandler()
                .getListenerHandler()
                .getPlugin(), Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_DELAY.toLong(), Config.ORIGINS_MERLING_AIR_BREATHING_DAMAGE_PERIOD_DELAY.toLong());
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
        }.runTask(getOriginListenerHandler()
                .getListenerHandler()
                .getPlugin());
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
            OriginPlayer originPlayer = new OriginPlayer(targetPlayer);
            String targetPlayerOrigin = originPlayer.getOrigin();
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
        getOriginListenerHandler().getListenerHandler().getPlugin().getProtocolManager().addPacketListener(
                new PacketAdapter(getOriginListenerHandler().getListenerHandler().getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                Player player = event.getPlayer();
                OriginPlayer originPlayer = new OriginPlayer(player);
                String playerOrigin = originPlayer.getOrigin();
                Location playerLocation = player.getLocation();

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (playerLocation.getBlock().isLiquid() && playerLocation.add(0, 1, 0).getBlock().isLiquid()) {
                        if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 20, false, false));
                                }
                            }.runTask(getPlugin());
                        } else if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(getPlugin());
                        } else if (digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(getPlugin());
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
        getOriginListenerHandler().getListenerHandler().getPlugin().getProtocolManager().addPacketListener(
                new PacketAdapter(getOriginListenerHandler().getListenerHandler().getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.POSITION) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                OriginPlayer originPlayer = new OriginPlayer(player);
                String playerOrigin = originPlayer.getOrigin();

                if (Objects.equals(playerOrigin, Origins.MERLING.toString())) {
                    if (player.isInWater()) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.addPotionEffect(
                                        new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0 , false, false));
                            }
                        }.runTask(getPlugin());
                    } else {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }.runTask(getPlugin());
                    }
                }
            }
        });
    }
}
