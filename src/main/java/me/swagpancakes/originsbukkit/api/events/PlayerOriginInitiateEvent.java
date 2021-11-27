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
package me.swagpancakes.originsbukkit.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The type Player origin initiate event.
 *
 * @author SwagPannekaker
 */
public class PlayerOriginInitiateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String origin;

    /**
     * Instantiates a new Player origin initiate event.
     *
     * @param player the player
     * @param origin the origin
     */
    public PlayerOriginInitiateEvent(Player player, String origin) {
        this.player = player;
        this.origin = origin;
    }

    /**
     * Gets handlers.
     *
     * @return the handlers
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets handler list.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return this.origin;
    }
}
