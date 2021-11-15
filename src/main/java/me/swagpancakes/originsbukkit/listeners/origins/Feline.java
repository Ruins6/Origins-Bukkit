/*
 *     Origins-Bukkit
 *     Copyright (C) 2021 SwagPannekaker
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * The type Feline.
 *
 * @author SwagPannekaker
 */
public class Feline implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Feline.
     *
     * @param plugin the plugin
     */
    public Feline(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Feline join.
     *
     * @param player the player
     */
    public void felineJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.FELINE) {
            player.setHealthScale(Config.ORIGINS_FELINE_MAX_HEALTH.toDouble());
        }
    }

    /**
     * Feline fall damage immunity.
     *
     * @param event the event
     */
    @EventHandler
    public void felineFallDamageImmunity(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            EntityDamageEvent.DamageCause damageCause = event.getCause();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.FELINE) {
                if (damageCause == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Feline block break.
     *
     * @param event the event
     */
    @EventHandler
    public void felineBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Block block = event.getBlock();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.FELINE) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                if (block.getType() == Material.STONE) {
                    if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                        if (blockGetAdjacent(block)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Block get adjacent boolean.
     *
     * @param block the block
     *
     * @return the boolean
     */
    public boolean blockGetAdjacent(Block block) {
        int start = 0;
        Block blockEast = block.getRelative(BlockFace.EAST);
        Block blockWest = block.getRelative(BlockFace.WEST);
        Block blockNorth = block.getRelative(BlockFace.NORTH);
        Block blockSouth = block.getRelative(BlockFace.SOUTH);
        Block blockUp = block.getRelative(BlockFace.UP);
        Block blockDown = block.getRelative(BlockFace.DOWN);

        if (blockEast.getType() == Material.STONE) {
            start++;
        }
        if (blockWest.getType() == Material.STONE) {
            start++;
        }
        if (blockNorth.getType() == Material.STONE) {
            start++;
        }
        if (blockSouth.getType() == Material.STONE) {
            start++;
        }
        if (blockUp.getType() == Material.STONE) {
            start++;
        }
        if (blockDown.getType() == Material.STONE) {
            start++;
        }
        return start > 2;
    }

    /**
     * Feline sprint jump.
     *
     * @param event the event
     */
    @EventHandler
    public void felineSprintJump(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.FELINE) {
            if (!player.isSprinting()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false));
            } else {
                player.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }
}
