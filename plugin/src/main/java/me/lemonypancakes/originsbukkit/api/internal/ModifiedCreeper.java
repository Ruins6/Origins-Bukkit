package me.lemonypancakes.originsbukkit.api.internal;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * The interface Modified creeper.
 */
public interface ModifiedCreeper {

    /**
     * Summon modified creeper.
     *
     * @param location the location
     */
    void summonModifiedCreeper(Location location);

    /**
     * Is modified creeper boolean.
     *
     * @param entity the entity
     *
     * @return the boolean
     */
    boolean isModifiedCreeper(Entity entity);
}
