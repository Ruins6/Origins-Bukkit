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
package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginAbilityUseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.UUID;

/**
 * The type Key listener.
 *
 * @author SwagPannekaker
 */
public class KeyListener implements Listener {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Key listener.
     *
     * @param plugin the plugin
     */
    public KeyListener(OriginsBukkit plugin) {
        this.plugin = plugin;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * On ability use.
     *
     * @param event the event
     */
    @EventHandler
    private void onAbilityUse(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

        if (player.isSneaking()) {
            if (plugin.getOrigins().contains(playerOrigin)) {
                for (String origin : plugin.getOrigins()) {
                    if (origin.equals(plugin.getStorageUtils().getPlayerOrigin(playerUUID))) {
                        PlayerOriginAbilityUseEvent playerOriginAbilityUseEvent = new PlayerOriginAbilityUseEvent(player, origin);
                        Bukkit.getPluginManager().callEvent(playerOriginAbilityUseEvent);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
