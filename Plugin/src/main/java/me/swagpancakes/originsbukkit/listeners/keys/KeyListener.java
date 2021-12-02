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
package me.swagpancakes.originsbukkit.listeners.keys;

import me.swagpancakes.originsbukkit.api.events.PlayerOriginAbilityUseEvent;
import me.swagpancakes.originsbukkit.api.wrappers.OriginPlayer;
import me.swagpancakes.originsbukkit.listeners.ListenerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Objects;

/**
 * The type Key listener.
 *
 * @author SwagPannekaker
 */
public class KeyListener implements Listener {

    private final ListenerHandler listenerHandler;

    /**
     * Gets listener handler.
     *
     * @return the listener handler
     */
    public ListenerHandler getListenerHandler() {
        return listenerHandler;
    }

    /**
     * Instantiates a new Key listener.
     *
     * @param listenerHandler the listener handler
     */
    public KeyListener(ListenerHandler listenerHandler) {
        this.listenerHandler = listenerHandler;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        getListenerHandler()
                .getPlugin()
                .getServer()
                .getPluginManager()
                .registerEvents(this, getListenerHandler().getPlugin());
    }

    /**
     * On ability use.
     *
     * @param event the event
     */
    @EventHandler
    private void onAbilityUse(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        OriginPlayer originPlayer = new OriginPlayer(player);
        String playerOrigin = originPlayer.getOrigin();

        if (player.isSneaking()) {
            if (getListenerHandler().getPlugin().getOrigins().contains(playerOrigin)) {
                for (String origin : getListenerHandler().getPlugin().getOrigins()) {
                    if (Objects.equals(origin, playerOrigin)) {
                        PlayerOriginAbilityUseEvent playerOriginAbilityUseEvent = new PlayerOriginAbilityUseEvent(player, origin);
                        Bukkit.getPluginManager().callEvent(playerOriginAbilityUseEvent);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
