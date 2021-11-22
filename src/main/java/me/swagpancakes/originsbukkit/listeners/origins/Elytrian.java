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
import me.swagpancakes.originsbukkit.api.events.PlayerOriginAbilityUseEvent;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.api.util.Origin;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Elytrian.
 *
 * @author SwagPannekaker
 */
public class Elytrian extends Origin implements Listener {

    private final OriginsBukkit plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = Config.ORIGINS_ELYTRIAN_ABILITY_COOLDOWN.toInt();

    public static ItemStack elytra;

    /**
     * Instantiates a new Elytrian.
     *
     * @param plugin the plugin
     */
    public Elytrian(OriginsBukkit plugin) {
        super(Config.ORIGINS_ELYTRIAN_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
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
        return "Elytrian";
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
        return Material.ELYTRA;
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
        return Lang.ELYTRIAN_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.ELYTRIAN_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        createElytra();
    }

    /**
     * Elytrian join.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.ELYTRIAN.toString())) {
            player.setHealthScale(Config.ORIGINS_ELYTRIAN_MAX_HEALTH.toDouble());
            elytrianElytra(player);
            elytrianClaustrophobiaTimer(player);
        }
    }

    /**
     * Elytrian ability use.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianAbilityUse(PlayerOriginAbilityUseEvent event) {
        Player player = event.getPlayer();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.ELYTRIAN.toString())) {
            elytrianLaunchIntoAir(player);
        }
    }

    /**
     * Create elytra.
     */
    private void createElytra() {
        ItemStack itemStack = new ItemStack(Material.ELYTRA, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatUtils.format("&dElytra"));
            itemMeta.setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemStack.setItemMeta(itemMeta);
        }

        elytra = itemStack;
    }

    /**
     * Elytrian elytra.
     *
     * @param player the player
     */
    private void elytrianElytra(Player player) {
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
        Location location = player.getLocation();
        World world = player.getWorld();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack prevChestplate = playerInventory.getChestplate();

        if (Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
            if (prevChestplate != null && !prevChestplate.equals(elytra)) {
                if (playerInventory.firstEmpty() == -1) {
                    world.dropItem(location, prevChestplate);
                } else {
                    playerInventory.addItem(prevChestplate);
                }
            }
            playerInventory.setChestplate(elytra);
        }
    }

    /**
     * Elytrian armor equip.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianArmorEquip(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        Player player = (Player) humanEntity;
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
        int clickedSlot = event.getRawSlot();
        ItemStack cursorItemStack = event.getCursor();
        ItemStack clickedItemStack = event.getCurrentItem();

        if (Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
            if (clickedSlot == 6) {
                if (clickedItemStack != null && clickedItemStack.isSimilar(elytra)) {
                    if (cursorItemStack != null && cursorItemStack.getItemMeta() != null) {
                        ChatUtils.sendPlayerMessage(player, "&dpeek-a-boo!");
                    }
                }
            }
        }
    }

    /**
     * Elytrian launch into air.
     *
     * @param player the player
     */
    private void elytrianLaunchIntoAir(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (COOLDOWN.containsKey(playerUUID)) {
            long secondsLeft = ((COOLDOWN.get(playerUUID) / 1000) + COOLDOWNTIME - (System.currentTimeMillis() / 1000));

            if (secondsLeft > 0) {
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                        .toString()
                        .replace("%seconds_left%", String.valueOf(secondsLeft)));
            } else {
                player.setVelocity(new Vector(0, Config.ORIGINS_ELYTRIAN_ABILITY_Y_VELOCITY.toDouble(), 0));
                COOLDOWN.put(playerUUID, System.currentTimeMillis());
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                        .toString()
                        .replace("%player_current_origin%", String.valueOf(plugin.getStorageUtils().getPlayerOrigin(playerUUID))));
            }
        } else {
            player.setVelocity(new Vector(0, Config.ORIGINS_ELYTRIAN_ABILITY_Y_VELOCITY.toDouble(), 0));
            COOLDOWN.put(playerUUID, System.currentTimeMillis());
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(plugin.getStorageUtils().getPlayerOrigin(playerUUID))));
        }
    }

    /**
     * Elytrian aerial combatant.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianAerialCombatant(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
            double additionalDamage = baseDamage * 0.5;

            if (Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
                if (player.isGliding()) {
                    event.setDamage(baseDamage + additionalDamage);
                }
            }
        }
    }

    /**
     * Elytrian brittle bones.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianBrittleBones(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);

            if (Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
                EntityDamageEvent.DamageCause damageCause = event.getCause();

                if (damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
                    double baseDamage = event.getDamage();
                    double halfBaseDamage = baseDamage * 0.5;

                    event.setDamage(baseDamage + halfBaseDamage);
                }
            }
        }
    }

    /**
     * Elytrian check player.
     *
     * @param event the event
     */
    @EventHandler
    private void elytrianCheckPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
        ItemStack prevChestplate = player.getInventory().getChestplate();

        if (!Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
            if (prevChestplate != null && prevChestplate.equals(elytra)) {
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
            }
        }
    }

    /**
     * Elytrian claustrophobia timer.
     *
     * @param player the player
     */
    private void elytrianClaustrophobiaTimer(Player player) {

        new BukkitRunnable() {
            int timeLeft = 30;

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                String playerOrigin = plugin.getStorageUtils().getPlayerOrigin(playerUUID);
                Location location = player.getLocation();
                double y = location.getY();
                Block block = location.getBlock();

                if (Objects.equals(playerOrigin, Origins.ELYTRIAN.toString())) {
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
