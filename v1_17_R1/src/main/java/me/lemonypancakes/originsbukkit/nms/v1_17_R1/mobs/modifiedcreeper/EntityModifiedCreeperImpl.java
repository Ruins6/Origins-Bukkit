package me.lemonypancakes.originsbukkit.nms.v1_17_R1.mobs.modifiedcreeper;

import me.lemonypancakes.originsbukkit.api.internal.ModifiedCreeper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Creeper;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftCreeper;
import org.bukkit.entity.Entity;

/**
 * The type Entity modified creeper.
 *
 * @author SwagPannekaker
 */
public class EntityModifiedCreeperImpl implements ModifiedCreeper {

    /**
     * Summon modified creeper.
     *
     * @param location the location
     */
    @Override
    public void summonModifiedCreeper(Location location) {
        if (location.getWorld() != null) {
            ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
            EntityModifiedCreeper customCreeper = new EntityModifiedCreeper(location);
            customCreeper.setPos(location.getX(), location.getY(), location.getZ());
            worldServer.addFreshEntity(customCreeper);
        }
    }

    /**
     * Is modified creeper boolean.
     *
     * @param entity the entity
     *
     * @return the boolean
     */
    @Override
    public boolean isModifiedCreeper(Entity entity) {
        Creeper entityCreeper = ((CraftCreeper) entity).getHandle();

        return entityCreeper instanceof EntityModifiedCreeper;
    }
}
