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
package me.swagpancakes.originsbukkit.nms;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.nms.mobs.NMSMobs;
import org.bukkit.Bukkit;

/**
 * The type Nms handler.
 *
 * @author SwagPannekaker
 */
public class NMSHandler {

    private final OriginsBukkit plugin;
    private NMSMobs nmsMobs;

    /**
     * Gets nms mobs.
     *
     * @return the nms mobs
     */
    public NMSMobs getNMSMobs() {
        return this.nmsMobs;
    }

    /**
     * Instantiates a new Nms handler.
     *
     * @param plugin the plugin
     */
    public NMSHandler(OriginsBukkit plugin) {
        this.plugin = plugin;
        init();
    }

    /**
     * Gets nms server version.
     *
     * @return the nms server version
     */
    public String getNMSServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    /**
     * Init.
     */
    private void init() {
        nmsMobs = new NMSMobs(this);
    }
}
