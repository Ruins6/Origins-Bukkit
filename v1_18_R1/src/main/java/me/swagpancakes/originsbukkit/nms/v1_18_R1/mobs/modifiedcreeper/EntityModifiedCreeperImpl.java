package me.swagpancakes.originsbukkit.nms.v1_18_R1.mobs.modifiedcreeper;

import me.swagpancakes.originsbukkit.api.internal.ModifiedCreeper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Creeper;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftCreeper;
import org.bukkit.entity.Entity;

public class EntityModifiedCreeperImpl implements ModifiedCreeper {

    @Override
    public void summonModifiedCreeper(Location location) {
        if (location.getWorld() != null) {
            ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
            me.swagpancakes.originsbukkit.nms.v1_18_R1.mobs.modifiedcreeper.EntityModifiedCreeper customCreeper = new me.swagpancakes.originsbukkit.nms.v1_18_R1.mobs.modifiedcreeper.EntityModifiedCreeper(location);
            customCreeper.setPos(location.getX(), location.getY(), location.getZ());
            worldServer.addFreshEntity(customCreeper);
        }
    }

    @Override
    public boolean isModifiedCreeper(Entity entity) {
        Creeper entityCreeper = ((CraftCreeper) entity).getHandle();

        return entityCreeper instanceof EntityModifiedCreeper;
    }
}
