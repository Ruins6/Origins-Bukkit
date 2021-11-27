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
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginAbilityUseEvent;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.PhantomAbilityToggleData;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Phantom.
 *
 * @author SwagPannekaker
 */
public class Phantom extends Origin implements Listener {

    private final OriginsBukkit plugin;
    private final List<Player> phantomPlayers = new ArrayList<>();

    /**
     * Instantiates a new Phantom.
     *
     * @param plugin the plugin
     */
    public Phantom(OriginsBukkit plugin) {
        super(Config.ORIGINS_PHANTOM_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
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
        return "Phantom";
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
        return Material.PHANTOM_MEMBRANE;
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
        return Lang.PHANTOM_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.PHANTOM_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        registerPhantomSunlightDamageListener();
        registerPhantomInvisibilityPotionPacketListener();
    }

    /**
     * Phantom join.
     *
     * @param event the event
     */
    @EventHandler
    private void phantomJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = event.getOrigin();
        boolean isToggled = plugin.getStorageUtils().getPhantomAbilityToggleData(playerUUID);

        if (Objects.equals(origin, Origins.PHANTOM.toString())) {
            player.setHealthScale(Config.ORIGINS_PHANTOM_MAX_HEALTH.toDouble());
            if (!isToggled) {
                plugin.getGhostFactory().setGhost(player, true);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, false, false));
            phantomPlayers.add(player);
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.setInvisible(false);
        }
    }

    /**
     * Phantom ability use.
     *
     * @param event the event
     */
    @EventHandler
    private void phantomAbilityUse(PlayerOriginAbilityUseEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.PHANTOM.toString())) {
            phantomSwitchToggleAbility(player);
        }
    }

    /**
     * Register phantom sunlight damage listener.
     */
    private void registerPhantomSunlightDamageListener() {

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!phantomPlayers.isEmpty()) {
                    for (int i = 0; i < phantomPlayers.size(); i++) {
                        Player player = phantomPlayers.get(i);
                        UUID playerUUID = player.getUniqueId();
                        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                        boolean isToggled = plugin.getStorageUtils().getPhantomAbilityToggleData(playerUUID);
                        World world = player.getWorld();
                        Location location = player.getLocation();

                        if (Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
                            if (!isToggled) {
                                if (world.getTime() > 0 && world.getTime() < 13000) {
                                    if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                        player.setFireTicks(20);
                                    }
                                }
                            }
                        } else {
                            phantomPlayers.remove(player);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_DELAY.toLong(), Config.ORIGINS_BLAZEBORN_WATER_DAMAGE_PERIOD_DELAY.toLong());
    }

    /**
     * Phantom switch toggle ability.
     *
     * @param player the player
     */
    private void phantomSwitchToggleAbility(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.getStorageUtils().findPhantomAbilityToggleData(playerUUID) == null) {
            if (!(player.getFoodLevel() < 4)) {
                plugin.getStorageUtils().createPhantomAbilityToggleData(playerUUID, true);
                ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &aON");
                plugin.getGhostFactory().removePlayer(player);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setFlySpeed(0.05f);
            }
        } else {
            if (plugin.getStorageUtils().getPhantomAbilityToggleData(playerUUID)) {
                plugin.getStorageUtils().updatePhantomAbilityToggleData(playerUUID, new PhantomAbilityToggleData(playerUUID, false));
                ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &cOFF");
                plugin.getGhostFactory().addPlayer(player);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setFlySpeed(0.1f);
            } else {
                if (!(player.getFoodLevel() < 4)) {
                    plugin.getStorageUtils().updatePhantomAbilityToggleData(playerUUID, new PhantomAbilityToggleData(playerUUID, true));
                    plugin.getGhostFactory().removePlayer(player);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.setFlySpeed(0.05f);
                    ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &aON");
                } else {
                    ChatUtils.sendPlayerMessage(player, "&cCannot use ability because your hunger level is less than 4 levels.");
                }
            }
        }
    }

    /**
     * Phantom fast exhaustion.
     *
     * @param event the event
     */
    @EventHandler
    private void phantomFastExhaustion(FoodLevelChangeEvent event) {
        HumanEntity humanEntity = event.getEntity();
        Player player = (Player) humanEntity;
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
        boolean isToggled = plugin.getStorageUtils().getPhantomAbilityToggleData(playerUUID);

        if (Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
            if (isToggled) {
                if (player.getFoodLevel() < 4) {
                    phantomSwitchToggleAbility(player);
                    ChatUtils.sendPlayerMessage(player, "&cToggled your ability because your hunger level is less than 4 levels");
                }
                event.setFoodLevel(event.getFoodLevel() - 2);
            }
        }
    }

    /**
     * On non phantom join.
     *
     * @param event the event
     */
    @EventHandler
    private void onNonPhantomJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

        if (!Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
            plugin.getGhostFactory().addPlayer(player);
        }
    }

    /**
     * Phantom anti invisibility effect remove.
     *
     * @param event the event
     */
    @EventHandler
    private void phantomAntiInvisibilityEffectRemove(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
            PotionEffect oldEffect = event.getOldEffect();
            EntityPotionEffectEvent.Cause cause = event.getCause();

            if (Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
                if (oldEffect != null) {
                    if (oldEffect.getType().equals(PotionEffectType.INVISIBILITY) && cause != EntityPotionEffectEvent.Cause.PLUGIN) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Anti non phantom invisibility potion.
     *
     * @param event the event
     */
    @EventHandler
    private void antiNonPhantomInvisibilityPotion(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
            PotionEffect newEffect = event.getNewEffect();

            if (!Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
                if (newEffect != null) {
                    if (newEffect.getType().equals(PotionEffectType.INVISIBILITY)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Register phantom invisibility potion packet listener.
     */
    private void registerPhantomInvisibilityPotionPacketListener() {
        plugin.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EFFECT) {

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        Player player = event.getPlayer();
                        UUID playerUUID = player.getUniqueId();
                        String playerOrigin = Phantom.this.plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                        byte effectType = packet.getBytes().read(0);

                        if (Objects.equals(playerOrigin, Origins.PHANTOM.toString())) {
                            if (effectType == 14) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
        );
    }
}
