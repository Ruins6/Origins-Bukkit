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
package me.swagpancakes.originsbukkit.nms.mobs.modifiedcreeper;

import me.swagpancakes.originsbukkit.nms.mobs.NMSMobHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * The type Modified creeper.
 *
 * @author SwagPannekaker
 */
public class ModifiedCreeper {

    private final NMSMobHandler nmsMobHandler;
    private ModifiedCreeperInterface modifiedCreeperInterface;

    /**
     * Instantiates a new Modified creeper.
     *
     * @param nmsMobHandler the nms mob handler
     */
    public ModifiedCreeper(NMSMobHandler nmsMobHandler) {
        this.nmsMobHandler = nmsMobHandler;
    }

    /**
     * Summon modified creeper.
     *
     * @param location the location
     */
    public void summonModifiedCreeper(Location location) {
        String serverVersion = nmsMobHandler.getNMSHandler().getNMSServerVersion();

        if (serverVersion != null) {
            switch (serverVersion) {
                case "v1_17_R1":
                    modifiedCreeperInterface = new ModifiedCreeper_1_17_R1(location);
                    modifiedCreeperInterface.spawnModifiedCreeper(location);
                    break;
            }
        }
    }

    /**
     * Is modified creeper boolean.
     *
     * @param location the location
     * @param entity   the entity
     *
     * @return the boolean
     */
    public boolean isModifiedCreeper(Location location, Entity entity) {
        String serverVersion = nmsMobHandler.getNMSHandler().getNMSServerVersion();

        if (serverVersion != null) {
            switch (serverVersion) {
                case "v1_17_R1":
                    modifiedCreeperInterface = new ModifiedCreeper_1_17_R1(location);
                    return modifiedCreeperInterface.isModifiedCreeper(entity);
            }
        }
        return false;
    }
}
