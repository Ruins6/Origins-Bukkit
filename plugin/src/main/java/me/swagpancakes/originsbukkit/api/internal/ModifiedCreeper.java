package me.swagpancakes.originsbukkit.api.internal;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface ModifiedCreeper {

    void summonModifiedCreeper(Location location);

    boolean isModifiedCreeper(Entity entity);
}
