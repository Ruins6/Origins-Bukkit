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
public class ModifiedCreeperHandler {

    private final NMSMobHandler nmsMobHandler;
    private String SERVER_VERSION;

    public NMSMobHandler getNMSMobHandler() {
        return nmsMobHandler;
    }

    /**
     * Instantiates a new Modified creeper.
     *
     * @param nmsMobHandler the nms mob handler
     */
    public ModifiedCreeperHandler(NMSMobHandler nmsMobHandler) {
        this.nmsMobHandler = nmsMobHandler;
        init();
    }

    private void init() {
        SERVER_VERSION = getNMSMobHandler().getNMSHandler().getNMSServerVersion();
    }

    /**
     * Summon modified creeper.
     *
     * @param location the location
     */
    public void summonModifiedCreeper(Location location) {
        if (SERVER_VERSION != null) {
            switch (SERVER_VERSION) {
                case "v1_17_R1":
                    ModifiedCreeper_1_17_R1 modifiedCreeper_1_17_r1 = new ModifiedCreeper_1_17_R1(location);
                    modifiedCreeper_1_17_r1.spawnModifiedCreeper(location);
                    break;
                case "v1_18_R1":
                    ModifiedCreeper_1_18_R1 modifiedCreeper_1_18_r1 = new ModifiedCreeper_1_18_R1(location);
                    modifiedCreeper_1_18_r1.spawnModifiedCreeper(location);
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
        if (SERVER_VERSION != null) {
            switch (SERVER_VERSION) {
                case "v1_17_R1":
                    ModifiedCreeper_1_17_R1 modifiedCreeper_1_17_r1 = new ModifiedCreeper_1_17_R1(location);
                    return modifiedCreeper_1_17_r1.isModifiedCreeper(entity);
                case "v1_18_R1":
                    ModifiedCreeper_1_18_R1 modifiedCreeper_1_18_r1 = new ModifiedCreeper_1_18_R1(location);
                    return modifiedCreeper_1_18_r1.isModifiedCreeper(entity);
            }
        }
        return false;
    }
}
