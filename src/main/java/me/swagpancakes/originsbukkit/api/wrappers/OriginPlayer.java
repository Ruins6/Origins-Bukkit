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
package me.swagpancakes.originsbukkit.api.wrappers;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import org.bukkit.entity.Player;

/**
 * The type Origin player.
 *
 * @author SwagPannekaker
 */
public class OriginPlayer {

    private final Player player;

    /**
     * Instantiates a new Origin player.
     *
     * @param player the player
     */
    public OriginPlayer(Player player) {
        this.player = player;
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
        return OriginsBukkit.getPlugin().getStorageUtils().getPlayerOrigin(this.player.getUniqueId());
    }

    /**
     * Find origins player data origins player data.
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData() {
        return OriginsBukkit.getPlugin().getStorageUtils().findOriginsPlayerData(this.player.getUniqueId());
    }
}
