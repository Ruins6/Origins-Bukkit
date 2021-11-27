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
package me.swagpancakes.originsbukkit.api;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The type Origins bukkit api.
 *
 * @author SwagPannekaker
 */
public class OriginsBukkitAPI {

    /**
     * Gets player origin.
     *
     * @param playerUUID the player uuid
     *
     * @return the player origin
     */
    public String getPlayerOrigin(UUID playerUUID) {
        return OriginsBukkit.getPlugin().getStorageUtils().getPlayerOrigin(playerUUID);
    }

    /**
     * Create origins player data.
     *
     * @param playerUUID the player uuid
     * @param player     the player
     * @param origin     the origin
     */
    public void createOriginsPlayerData(UUID playerUUID, Player player, String origin) {
        OriginsBukkit.getPlugin().getStorageUtils().createOriginsPlayerData(playerUUID, player, origin);
    }

    /**
     * Find origins player data origins player data.
     *
     * @param playerUUID the player uuid
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData(UUID playerUUID) {
        return OriginsBukkit.getPlugin().getStorageUtils().findOriginsPlayerData(playerUUID);
    }

    /**
     * Delete origins player data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteOriginsPlayerData(UUID playerUUID) {
        OriginsBukkit.getPlugin().getStorageUtils().deleteOriginsPlayerData(playerUUID);
    }

    /**
     * Update origins player data.
     *
     * @param playerUUID           the player uuid
     * @param newOriginsPlayerData the new origins player data
     */
    public void updateOriginsPlayerData(UUID playerUUID, OriginsPlayerData newOriginsPlayerData) {
        OriginsBukkit.getPlugin().getStorageUtils().updateOriginsPlayerData(playerUUID, newOriginsPlayerData);
    }
}
