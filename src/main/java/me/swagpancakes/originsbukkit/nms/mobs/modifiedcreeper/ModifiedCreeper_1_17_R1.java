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

import me.swagpancakes.originsbukkit.OriginsBukkit;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.monster.EntityCreeper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Modified creeper 1 17 r 1.
 *
 * @author SwagPannekaker
 */
public class ModifiedCreeper_1_17_R1 extends EntityCreeper implements ModifiedCreeperInterface {

    /**
     * Instantiates a new Modified creeper 1 17 r 1.
     *
     * @param location the location
     */
    public ModifiedCreeper_1_17_R1(Location location) {
        super(EntityTypes.o, ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle());
        this.bP.a(0, new PathfinderGoalAvoidTarget<>(this, EntityPlayer.class, 16f,  1d, 1.2d, entityLiving -> isFeline((EntityPlayer) entityLiving)));
    }

    /**
     * Is feline boolean.
     *
     * @param entityPlayer the entity player
     *
     * @return the boolean
     */
    private boolean isFeline(EntityPlayer entityPlayer) {
        Player player = new CraftPlayer((CraftServer) Bukkit.getServer(), entityPlayer);
        UUID playerUUID = player.getUniqueId();

        return Objects.equals(OriginsBukkit.getPlugin().getStorageUtils().getPlayerOrigin(playerUUID), OriginsBukkit.getPlugin().getFeline().getOriginIdentifier());
    }

    /**
     * Spawn modified creeper.
     *
     * @param location the location
     */
    @Override
    public void spawnModifiedCreeper(Location location) {
        if (location.getWorld() != null) {
            WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
            ModifiedCreeper_1_17_R1 customCreeper = new ModifiedCreeper_1_17_R1(location);
            customCreeper.setPosition(location.getX(), location.getY(), location.getZ());
            worldServer.addEntity(customCreeper);
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
        EntityCreeper entityCreeper = ((CraftCreeper) entity).getHandle();

        return entityCreeper instanceof ModifiedCreeperInterface;
    }
}
