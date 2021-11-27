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
package me.swagpancakes.originsbukkit.listeners.origins;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginAbilityUseEvent;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.ArachnidAbilityToggleData;
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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * The type Arachnid.
 *
 * @author SwagPannekaker
 */
public class Arachnid extends Origin implements Listener {

    private final OriginsBukkit plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = Config.ORIGINS_ARACHNID_ABILITY_SPIDER_WEB_COOLDOWN.toInt();

    /**
     * Instantiates a new Arachnid.
     *
     * @param plugin the plugin
     */
    public Arachnid(OriginsBukkit plugin) {
        super(Config.ORIGINS_ARACHNID_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
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
        return "Arachnid";
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
        return Material.COBWEB;
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
        return Lang.ARACHNID_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.ARACHNID_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        registerArachnidCobwebMovePacketListener();
    }

    /**
     * Arachnid join.
     *
     * @param event the event
     */
    @EventHandler
    private void arachnidJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.ARACHNID.toString())) {
            player.setHealthScale(getMaxHealth());
        } else {
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                player.setFlySpeed(0.1F);
                player.setAllowFlight(false);
                player.setFlying(false);
            } else {
                player.setFlySpeed(0.1F);
                player.setAllowFlight(true);
            }
        }
    }

    /**
     * Arachnid ability use.
     *
     * @param event the event
     */
    @EventHandler
    private void arachnidAbilityUse(PlayerOriginAbilityUseEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.ARACHNID.toString())) {
            arachnidClimbToggleAbility(player);
        }
    }

    /**
     * On arachnid climb toggle.
     *
     * @param event the event
     */
    @EventHandler
    private void onArachnidClimbToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

        if (Objects.equals(playerOrigin, Origins.ARACHNID.toString())) {
            if (plugin.getStorageUtils().findArachnidAbilityToggleData(playerUUID) == null) {
                plugin.getStorageUtils().createArachnidAbilityToggleData(playerUUID, false);
            }
            if (plugin.getStorageUtils().getArachnidAbilityToggleData(playerUUID)) {
                if (!player.isSneaking()) {
                    arachnidClimb(player);
                }
            }
        }
    }

    /**
     * On arachnid attack.
     *
     * @param event the event
     */
    @EventHandler
    private void onArachnidAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        Location location = entity.getLocation();

        if (damager instanceof Player) {
            Player player = (Player) event.getDamager();
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

            if (Objects.equals(playerOrigin, Origins.ARACHNID.toString())) {
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
                                    .replace("%player_current_origin%", String.valueOf(plugin.getStorageUtils().getPlayerOrigin(playerUUID))));
                        }
                    } else {
                        removeArachnidCobwebs(material1, block1);
                        block1.setType(Material.COBWEB);
                        COOLDOWN.put(playerUUID, System.currentTimeMillis());
                        ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                                .toString()
                                .replace("%player_current_origin%", String.valueOf(plugin.getStorageUtils().getPlayerOrigin(playerUUID))));
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
    private void onArachnidBaneOfArthropodsDamage(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (target instanceof Player && damager instanceof LivingEntity) {
            Player targetPlayer = (Player) target;
            LivingEntity livingDamager = (LivingEntity) damager;
            UUID targetPlayerUUID = targetPlayer.getUniqueId();
            String targetPlayerOrigin = plugin.getStorageUtils().getPlayerOrigin(targetPlayerUUID);
            EntityEquipment entityEquipment = livingDamager.getEquipment();

            if (entityEquipment != null) {
                ItemStack itemStack = livingDamager.getEquipment().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();

                if (Objects.equals(targetPlayerOrigin, Origins.ARACHNID.toString())) {
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
    private void arachnidClimb(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location location = player.getLocation();
                Block block = location.getBlock();

                if (Objects.equals(playerOrigin, Origins.ARACHNID.toString())) {
                    if (player.isOnline()) {
                        if (plugin.getStorageUtils().getArachnidAbilityToggleData(playerUUID)) {
                            if (player.isSneaking() && !player.isGliding()) {
                                if (nextToWall(player) && !block.isLiquid()) {
                                    player.setVelocity(player.getVelocity().setY(Config.ORIGINS_ARACHNID_ABILITY_CLIMBING_Y_VELOCITY.toDouble()));
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
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    /**
     * Arachnid climb toggle ability.
     *
     * @param player the player
     */
    private void arachnidClimbToggleAbility(Player player) {
        UUID playerUUID = player.getUniqueId();
        Location location = player.getLocation();
        Block block = location.getBlock();

        if (plugin.getStorageUtils().findArachnidAbilityToggleData(playerUUID) == null) {
            plugin.getStorageUtils().createArachnidAbilityToggleData(playerUUID, false);
            ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &cOFF");
        } else {
            if (plugin.getStorageUtils().getArachnidAbilityToggleData(playerUUID)) {
                plugin.getStorageUtils().updateArachnidAbilityToggleData(playerUUID, new ArachnidAbilityToggleData(playerUUID, false));
                ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &cOFF");
            } else {
                plugin.getStorageUtils().updateArachnidAbilityToggleData(playerUUID, new ArachnidAbilityToggleData(playerUUID, true));
                if (nextToWall(player) && !block.isLiquid()) {
                    arachnidClimb(player);
                }
                ChatUtils.sendPlayerMessage(player, "&7Ability Toggled &aON");
            }
        }
    }

    /**
     * Register arachnid cobweb move packet listener.
     */
    private void registerArachnidCobwebMovePacketListener() {
        plugin.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                Location playerLocation = player.getLocation();
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = Arachnid.this.plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                double x = packet.getDoubles().read(0);
                double y = packet.getDoubles().read(1);
                double z = packet.getDoubles().read(2);
                World world = player.getWorld();
                Location location = new Location(world, x, y, z);
                Block block = location.getBlock();
                Block block1 = location.add(0, 1, 0).getBlock();
                Block block2 = location.subtract(0, 1, 0).getBlock();
                Material material = block.getType();
                Material material1 = block1.getType();
                Material material2 = block2.getType();

                if (Objects.equals(playerOrigin, Origins.ARACHNID.toString())) {
                    if (material == Material.COBWEB || material1 == Material.COBWEB || material2 == Material.COBWEB || nextToCobweb(player)) {
                        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                            if (!player.isFlying()) {
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        player.teleport(playerLocation.add(0, 0.00001, 0));
                                    }
                                }.runTask(plugin);
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
        });
    }

    /**
     * Next to wall boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    private boolean nextToWall(Player player) {
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
    private boolean nextToCobweb(Player player) {
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
    private void removeArachnidCobwebs(Material material, Block block) {

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
    private void arachnidEatingDisabilities(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
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

        if (Objects.equals(playerOrigin, Origins.ARACHNID.toString())) {
            if (!materials.contains(material)) {
                event.setCancelled(true);
            }
        }
    }
}
