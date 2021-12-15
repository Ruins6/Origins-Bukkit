package me.lemonypancakes.originsbukkit.nms.v1_18_R1.mobs.modifiedcreeper;

import me.lemonypancakes.originsbukkit.OriginsBukkit;
import me.lemonypancakes.originsbukkit.api.internal.ModifiedCreeper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Entity modified creeper.
 *
 * @author SwagPannekaker
 */
public class EntityModifiedCreeper extends Creeper implements ModifiedCreeper {

    /**
     * Instantiates a new Entity modified creeper.
     *
     * @param location the location
     */
    public EntityModifiedCreeper(Location location) {
        super(EntityType.CREEPER, ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle());
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, net.minecraft.world.entity.player.Player.class, 16f,  1d, 1.2d, entityLiving -> isFeline((net.minecraft.world.entity.player.Player) entityLiving)));
    }

    /**
     * Is feline boolean.
     *
     * @param entityPlayer the entity player
     *
     * @return the boolean
     */
    private boolean isFeline(net.minecraft.world.entity.player.Player entityPlayer) {
        Player player = new CraftPlayer((CraftServer) Bukkit.getServer(), (ServerPlayer) entityPlayer);
        UUID playerUUID = player.getUniqueId();

        return Objects.equals(OriginsBukkit.getPlugin().getStorageHandler().getOriginsPlayerData().getPlayerOrigin(playerUUID), OriginsBukkit.getPlugin().getListenerHandler().getOriginListenerHandler().getFeline().getOriginIdentifier());
    }

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

        return entityCreeper instanceof ModifiedCreeper;
    }
}