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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Phantom.
 *
 * @author SwagPannekaker
 */
public class Phantom extends Origin implements Listener {

    private final OriginsBukkit plugin;

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
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.PHANTOM.toString())) {
            player.setHealthScale(Config.ORIGINS_PHANTOM_MAX_HEALTH.toDouble());
            plugin.getGhostFactory().setGhost(player, true);
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
