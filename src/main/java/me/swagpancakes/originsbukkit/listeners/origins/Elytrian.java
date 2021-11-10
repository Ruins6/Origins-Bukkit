package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * The type Elytrian.
 */
public class Elytrian implements Listener {

    private final Main plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = 30;

    /**
     * The Elytra.
     */
    public ItemStack elytra;

    /**
     * Instantiates a new Elytrian.
     *
     * @param plugin the plugin
     */
    public Elytrian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

        createElytra();
    }

    /**
     * Elytrian join.
     *
     * @param player the player
     */
    public void elytrianJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
            player.setHealthScale((10) * 2);
            elytrianElytra(player);
            elytrianClaustrophobiaTimer(player);
        }
    }

    /**
     * Create elytra.
     */
    public void createElytra() {
        ItemStack itemStack = new ItemStack(Material.ELYTRA, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.format("&dElytra"));
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        elytra = itemStack;
    }

    /**
     * Elytrian elytra.
     *
     * @param player the player
     */
    public void elytrianElytra(Player player) {
        UUID playerUUID = player.getUniqueId();
        Location location = player.getLocation();
        World world = player.getWorld();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack prevChestplate = playerInventory.getChestplate();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
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
     * Elytrian launch into air.
     *
     * @param player the player
     */
    public void elytrianLaunchIntoAir(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (COOLDOWN.containsKey(playerUUID)) {
            long secondsLeft = ((COOLDOWN.get(playerUUID) / 1000) + COOLDOWNTIME - (System.currentTimeMillis() / 1000));

            if (secondsLeft > 0) {
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                        .toString()
                        .replace("%seconds_left%", String.valueOf(secondsLeft)));
            } else {
                player.setVelocity(new Vector(0, 2.1, 0));
                COOLDOWN.put(playerUUID, System.currentTimeMillis());
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                        .toString()
                        .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
            }
        } else {
            player.setVelocity(new Vector(0, 2.1, 0));
            COOLDOWN.put(playerUUID, System.currentTimeMillis());
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
        }
    }

    /**
     * Elytrian aerial combatant.
     *
     * @param event the event
     */
    @EventHandler
    public void elytrianAerialCombatant(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        double baseDamage = event.getDamage();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
                if (player.isGliding()) {
                    event.setDamage(baseDamage + 1.5);
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
    public void elytrianBrittleBones(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
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
    public void elytrianCheckPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack prevChestplate = player.getInventory().getChestplate();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) != Origins.ELYTRIAN) {
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
    public void elytrianClaustrophobiaTimer(Player player) {

        new BukkitRunnable() {
            int timeLeft = 30;

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                double y = location.getY();
                Block block = location.getBlock();

                if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.ELYTRIAN) {
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * On click.
     *
     * @param event the event
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getAction() == Action.LEFT_CLICK_AIR) {
            event.getPlayer().sendMessage("BLEP");
        }
    }
}
