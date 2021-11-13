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
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The type Arachnid.
 *
 * @author SwagPannekaker
 */
public class Arachnid implements Listener {

    private final Main plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = 5;

    /**
     * Instantiates a new Arachnid.
     *
     * @param plugin the plugin
     */
    public Arachnid(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Arachnid join.
     *
     * @param player the player
     */
    public void arachnidJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
            player.setHealthScale((10 - 3) * 2);
        }
    }

    /**
     * On arachnid climb toggle.
     *
     * @param event the event
     */
    @EventHandler
    public void onArachnidClimbToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
            if (!player.isSneaking()) {
                arachnidClimb(player);
            }
        }
    }

    /**
     * On arachnid attack.
     *
     * @param event the event
     */
    @EventHandler
    public void onArachnidAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        Location location = entity.getLocation();

        if (damager instanceof Player) {
            Player player = (Player) event.getDamager();
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
                Location location1 = location.add(0, 1, 0);
                Block block1 = location1.getBlock();
                Material material1 = block1.getType();

                if (material1.isAir()) {
                    if (COOLDOWN.containsKey(playerUUID)) {
                        long secondsLeft = ((COOLDOWN.get(playerUUID) / 1000) + COOLDOWNTIME - (System.currentTimeMillis() / 1000));

                        if (secondsLeft > 0) {
                            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                                    .toString()
                                    .replace("%seconds_left%", String.valueOf(secondsLeft)));
                        } else {
                            removeArachnidCobwebs(material1, block1);
                            block1.setType(Material.COBWEB);
                            COOLDOWN.put(playerUUID, System.currentTimeMillis());
                            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                                    .toString()
                                    .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                        }
                    } else {
                        removeArachnidCobwebs(material1, block1);
                        block1.setType(Material.COBWEB);
                        COOLDOWN.put(playerUUID, System.currentTimeMillis());
                        ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                                .toString()
                                .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
                    }
                }
            }
        }
    }

    /**
     * On arachnid bane of arthropods damage.
     *
     * @param event the event
     */
    @EventHandler
    public void onArachnidBaneOfArthropodsDamage(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();


        if (target instanceof Player && damager instanceof LivingEntity) {
            Player targetPlayer = (Player) target;
            LivingEntity livingDamager = (LivingEntity) damager;
            UUID targetPlayerUUID = targetPlayer.getUniqueId();
            EntityEquipment entityEquipment = livingDamager.getEquipment();

            if (entityEquipment != null) {
                ItemStack itemStack = livingDamager.getEquipment().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();

                if (plugin.storageUtils.getPlayerOrigin(targetPlayerUUID) == Origins.ARACHNID) {
                    if (itemMeta != null && itemMeta.hasEnchant(Enchantment.DAMAGE_ARTHROPODS)) {
                        int enchantLevel = itemMeta.getEnchantLevel(Enchantment.DAMAGE_ARTHROPODS);

                        event.setDamage(baseDamage + (2.5 * enchantLevel));
                    }
                }
            }
        }
    }

    /**
     * Arachnid climb.
     *
     * @param player the player
     */
    public void arachnidClimb(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
                    if (player.isOnline()) {
                        if (player.isSneaking() && !player.isGliding()) {
                            if (nextToWall(player) && !block.isLiquid()) {
                                player.setVelocity(player.getVelocity().setY(0.175));
                            }
                        } else {
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * On arachnid cobweb enter.
     *
     * @param event the event
     */
    @EventHandler
    public void onArachnidCobwebEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Location location = player.getLocation();
        Block block = location.getBlock();
        Block block1 = location.add(0, 1, 0).getBlock();
        Block block2 = location.subtract(0, 1, 0).getBlock();
        Material material = block.getType();
        Material material1 = block1.getType();
        Material material2 = block2.getType();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
            if (material == Material.COBWEB || material1 == Material.COBWEB || material2 == Material.COBWEB || nextToCobweb(player)) {
                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                    if (!player.isFlying()) {
                        player.teleport(location.add(0, 0.00001, 0));
                    }
                    player.setFlySpeed(0.04F);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                } else {
                    player.setFlySpeed(0.1F);
                }
            } else {
                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                    player.setFlySpeed(0.1F);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
        }
    }

    /**
     * Next to wall boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    public boolean nextToWall(Player player) {
        World world = player.getWorld();
        double locX = player.getLocation().getX();
        double locY = player.getLocation().getY();
        double locZ = player.getLocation().getZ();
        Location xp = new Location(world, locX + 0.30175, locY, locZ);
        Location xn = new Location(world, locX - 0.30175, locY, locZ);
        Location zp = new Location(world, locX, locY, locZ + 0.30175);
        Location zn = new Location(world, locX, locY, locZ - 0.30175);

        if (xp.getBlock().getType().isSolid()) {
            return true;
        }
        if (xn.getBlock().getType().isSolid()) {
            return true;
        }
        if (zp.getBlock().getType().isSolid()) {
            return true;
        }
        return zn.getBlock().getType().isSolid();
    }

    /**
     * Next to cobweb boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    public boolean nextToCobweb(Player player) {
        World world = player.getWorld();
        double locX = player.getLocation().getX();
        double locY = player.getLocation().getY();
        double locZ = player.getLocation().getZ();
        Location xp = new Location(world, locX + 0.3, locY, locZ);
        Location xn = new Location(world, locX - 0.3, locY, locZ);
        Location zp = new Location(world, locX, locY, locZ + 0.3);
        Location zn = new Location(world, locX, locY, locZ - 0.3);

        if (xp.getBlock().getType() == Material.COBWEB) {
            return true;
        }
        if (xn.getBlock().getType() == Material.COBWEB) {
            return true;
        }
        if (zp.getBlock().getType() == Material.COBWEB) {
            return true;
        }
        return zn.getBlock().getType() == Material.COBWEB;
    }

    /**
     * Remove arachnid cobwebs.
     *
     * @param material the material
     * @param block    the block
     */
    public void removeArachnidCobwebs(Material material, Block block) {

        new BukkitRunnable() {

            @Override
            public void run() {
                block.setType(material);
            }
        }.runTaskLater(plugin, 20L * 10);
    }

    /**
     * Arachnid eating disabilities.
     *
     * @param event the event
     */
    @EventHandler
    public void arachnidEatingDisabilities(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material material = event.getItem().getType();
        List<Material> materials = Arrays.asList(
                Material.COOKED_BEEF,
                Material.COOKED_COD,
                Material.COOKED_CHICKEN,
                Material.COOKED_MUTTON,
                Material.COOKED_RABBIT,
                Material.COOKED_PORKCHOP,
                Material.COOKED_SALMON,
                Material.BEEF,
                Material.COD,
                Material.CHICKEN,
                Material.MUTTON,
                Material.RABBIT,
                Material.PORKCHOP,
                Material.SALMON,
                Material.TROPICAL_FISH,
                Material.PUFFERFISH,
                Material.ROTTEN_FLESH);

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ARACHNID) {
            if (!materials.contains(material)) {
                event.setCancelled(true);
            }
        }
    }
}
