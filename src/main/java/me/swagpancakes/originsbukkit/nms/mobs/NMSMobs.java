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
package me.swagpancakes.originsbukkit.nms.mobs;

import me.swagpancakes.originsbukkit.nms.NMSHandler;
import me.swagpancakes.originsbukkit.nms.mobs.modifiedcreeper.ModifiedCreeper;

/**
 * The type Nms mobs.
 *
 * @author SwagPannekaker
 */
public class NMSMobs {

    private final NMSHandler nmsHandler;
    private ModifiedCreeper modifiedCreeper;

    /**
     * Gets nms handler.
     *
     * @return the nms handler
     */
    public NMSHandler getNMSHandler() {
        return this.nmsHandler;
    }

    /**
     * Gets modified creeper.
     *
     * @return the modified creeper
     */
    public ModifiedCreeper getModifiedCreeper() {
        return this.modifiedCreeper;
    }

    /**
     * Instantiates a new Nms mobs.
     *
     * @param nmsHandler the nms handler
     */
    public NMSMobs(NMSHandler nmsHandler) {
        this.nmsHandler = nmsHandler;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        modifiedCreeper = new ModifiedCreeper(this);
    }
}
