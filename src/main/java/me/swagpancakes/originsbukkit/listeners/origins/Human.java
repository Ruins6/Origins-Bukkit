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

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * The type Human.
 *
 * @author SwagPannekaker
 */
public class Human implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Human.
     *
     * @param plugin the plugin
     */
    public Human(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Human join.
     *
     * @param player the player
     */
    public void humanJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.HUMAN) {
            player.setHealthScale((10) * 2);
        }
    }
}
