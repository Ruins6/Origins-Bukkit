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
package me.swagpancakes.originsbukkit.api;

import com.sun.istack.internal.NotNull;
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

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Origins bukkit api.
     *
     * @param plugin the plugin
     */
    public OriginsBukkitAPI(OriginsBukkit plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets player origin.
     *
     * @param playerUUID the player uuid
     *
     * @return the player origin
     */
    public String getPlayerOrigin(@NotNull UUID playerUUID) {
        return plugin.storageUtils.getPlayerOrigin(playerUUID);
    }

    /**
     * Create origins player data.
     *
     * @param playerUUID the player uuid
     * @param player     the player
     * @param origin     the origin
     */
    public void createOriginsPlayerData(@NotNull UUID playerUUID, @NotNull Player player, @NotNull String origin) {
        plugin.storageUtils.createOriginsPlayerData(playerUUID, player, origin);
    }

    /**
     * Find origins player data origins player data.
     *
     * @param playerUUID the player uuid
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData(@NotNull UUID playerUUID) {
        return plugin.storageUtils.findOriginsPlayerData(playerUUID);
    }

    /**
     * Delete origins player data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteOriginsPlayerData(@NotNull UUID playerUUID) {
        plugin.storageUtils.deleteOriginsPlayerData(playerUUID);
    }

    /**
     * Update origins player data.
     *
     * @param playerUUID           the player uuid
     * @param newOriginsPlayerData the new origins player data
     */
    public void updateOriginsPlayerData(@NotNull UUID playerUUID, @NotNull OriginsPlayerData newOriginsPlayerData) {
        plugin.storageUtils.updateOriginsPlayerData(playerUUID, newOriginsPlayerData);
    }
}
