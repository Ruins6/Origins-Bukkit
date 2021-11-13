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

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * The type Phantom.
 *
 * @author SwagPannekaker
 */
public class Phantom implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Phantom.
     *
     * @param plugin the plugin
     */
    public Phantom(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Phantom join.
     *
     * @param player the player
     */
    public void phantomJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.PHANTOM) {
            player.setHealthScale((10) * 2);
            plugin.ghostFactory.addPlayer(player);
        }
    }

    /**
     * On non phantom join.
     *
     * @param event the event
     */
    @EventHandler
    public void onNonPhantomJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) != Origins.PHANTOM) {
            plugin.ghostFactory.addPlayer(player);
        }
    }

    /**
     * Phantom anti invisibility effect remove.
     *
     * @param event the event
     */
    @EventHandler
    public void phantomAntiInvisibilityEffectRemove(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            PotionEffect oldEffect = event.getOldEffect();
            EntityPotionEffectEvent.Cause cause = event.getCause();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.PHANTOM) {
                if (oldEffect != null) {
                    if (oldEffect.getType().equals(PotionEffectType.INVISIBILITY) && cause != EntityPotionEffectEvent.Cause.PLUGIN) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Phantom switch state ability.
     *
     * @param player the player
     */
    public void phantomSwitchStateAbility(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.PHANTOM) {
            if (!DisguiseAPI.isDisguised(player) && DisguiseAPI.getDisguise(player).getType() != DisguiseType.PHANTOM) {
                plugin.ghostFactory.setGhost(player, false);
                DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.PHANTOM).setViewSelfDisguise(true));
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setFlySpeed(0.05F);
            } else if (DisguiseAPI.isDisguised(player) && DisguiseAPI.getDisguise(player).getType() == DisguiseType.PHANTOM) {
                plugin.ghostFactory.setGhost(player, true);
                DisguiseAPI.undisguiseToAll(player);
                player.setFlySpeed(0.1F);
            }
        }
    }

    /**
     * Anti non phantom invisibility potion.
     *
     * @param event the event
     */
    @EventHandler
    public void antiNonPhantomInvisibilityPotion(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            PotionEffect newEffect = event.getNewEffect();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) != Origins.PHANTOM) {
                if (newEffect != null) {
                    if (newEffect.getType().equals(PotionEffectType.INVISIBILITY)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
