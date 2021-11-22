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

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.Origin;
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

import java.util.Objects;
import java.util.UUID;

/**
 * The type Feline.
 *
 * @author SwagPannekaker
 */
public class Feline extends Origin implements Listener {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Feline.
     *
     * @param plugin the plugin
     */
    public Feline(OriginsBukkit plugin) {
        super(Config.ORIGINS_FELINE_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
        this.plugin = plugin;
        init();
    }

    /**
     * Gets origin identifier.
     *
     * @return the origin identifier
     */
    @Override
    public String getOriginIdentifier() {
        return "Feline";
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    @Override
    public String getAuthor() {
        return "SwagPannekaker";
    }

    /**
     * Gets origin icon.
     *
     * @return the origin icon
     */
    @Override
    public Material getOriginIcon() {
        return Material.ORANGE_WOOL;
    }

    /**
     * Is origin icon glowing boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean isOriginIconGlowing() {
        return false;
    }

    /**
     * Gets origin title.
     *
     * @return the origin title
     */
    @Override
    public String getOriginTitle() {
        return Lang.FELINE_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.FELINE_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
    }

    /**
     * Feline join.
     *
     * @param event the event
     */
    @EventHandler
    public void felineJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.FELINE.toString())) {
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

            if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.FELINE.toString())) {
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.FELINE.toString())) {
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.FELINE.toString())) {
            if (!player.isSprinting()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false));
            } else {
                player.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }
}
