package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * The type No origin player restrict.
 */
public class NoOriginPlayerRestrict implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new No origin player restrict.
     *
     * @param plugin the plugin
     */
    public NoOriginPlayerRestrict(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Restrict player movement.
     *
     * @param player the player
     */
    public static void restrictPlayerMovement(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false));
    }

    /**
     * Unrestrict player movement.
     *
     * @param player the player
     */
    public static void unrestrictPlayerMovement(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
    }

    /**
     * On no origin player damage.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            assert player != null;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                event.setCancelled(true);
            }
        }

    }

    /**
     * On no origin player death.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        assert player != null;
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            player.spigot().respawn();
        }
    }

    /**
     * On no origin player respawn.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            plugin.playerOriginChecker.openOriginPickerGui(player);
            NoOriginPlayerRestrict.restrictPlayerMovement(player);
        }
    }

    /**
     * On no origin player item pickup.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerItemPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof  Player) {
            Player player = ((Player) entity).getPlayer();
            assert player != null;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * On no origin player block break.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On no origin player block place.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On no origin player interact.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On no origin player interact at entity.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On no origin player interact entity.
     *
     * @param event the event
     */
    @EventHandler
    public void onNoOriginPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }
}
