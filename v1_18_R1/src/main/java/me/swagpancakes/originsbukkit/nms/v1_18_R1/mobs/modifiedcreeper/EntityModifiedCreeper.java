package me.swagpancakes.originsbukkit.nms.v1_18_R1.mobs.modifiedcreeper;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.internal.ModifiedCreeper;
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

public class EntityModifiedCreeper extends Creeper implements ModifiedCreeper {

    public EntityModifiedCreeper(Location location) {
        super(EntityType.CREEPER, ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle());
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, net.minecraft.world.entity.player.Player.class, 16f,  1d, 1.2d, entityLiving -> isFeline((net.minecraft.world.entity.player.Player) entityLiving)));
    }

    private boolean isFeline(net.minecraft.world.entity.player.Player entityPlayer) {
        Player player = new CraftPlayer((CraftServer) Bukkit.getServer(), (ServerPlayer) entityPlayer);
        UUID playerUUID = player.getUniqueId();

        return Objects.equals(OriginsBukkit.getPlugin().getStorageHandler().getOriginsPlayerData().getPlayerOrigin(playerUUID), OriginsBukkit.getPlugin().getListenerHandler().getOriginListenerHandler().getFeline().getOriginIdentifier());
    }

    @Override
    public void summonModifiedCreeper(Location location) {
        if (location.getWorld() != null) {
            ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
            EntityModifiedCreeper customCreeper = new EntityModifiedCreeper(location);
            customCreeper.setPos(location.getX(), location.getY(), location.getZ());
            worldServer.addFreshEntity(customCreeper);
        }
    }

    @Override
    public boolean isModifiedCreeper(Entity entity) {
        Creeper entityCreeper = ((CraftCreeper) entity).getHandle();

        return entityCreeper instanceof ModifiedCreeper;
    }
}