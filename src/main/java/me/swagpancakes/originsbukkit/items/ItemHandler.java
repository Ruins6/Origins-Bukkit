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
package me.swagpancakes.originsbukkit.items;

import me.swagpancakes.originsbukkit.OriginsBukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Item handler.
 *
 * @author SwagPannekaker
 */
public class ItemHandler {

    private final OriginsBukkit plugin;
    private final List<String> items = new ArrayList<>();
    private OriginBall originBall;

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public OriginsBukkit getPlugin() {
        return this.plugin;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * Gets origin ball.
     *
     * @return the origin ball
     */
    public OriginBall getOriginBall() {
        return this.originBall;
    }

    /**
     * Instantiates a new Item handler.
     *
     * @param plugin the plugin
     */
    public ItemHandler(OriginsBukkit plugin) {
        this.plugin = plugin;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        originBall = new OriginBall(this);
    }
}
