package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.util.StorageUtils;
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
     * On player damage.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            assert player != null;
            UUID playerUUID = player.getUniqueId();

            if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
                event.setCancelled(true);
            }
        }

    }

    /**
     * On player death.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        assert player != null;
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            player.spigot().respawn();
        }
    }

    /**
     * On player respawn.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            PlayerOriginChecker.openOriginPickerGui(player);
            NoOriginPlayerRestrict.restrictPlayerMovement(player);
        }
    }

    /**
     * On player item pickup.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerItemPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof  Player) {
            Player player = ((Player) entity).getPlayer();
            assert player != null;
            UUID playerUUID = player.getUniqueId();

            if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * On player block break.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On player block place.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On player interact.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On player interact at entity.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }

    /**
     * On player interact entity.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (StorageUtils.findOriginsPlayerData(playerUUID) == null) {
            event.setCancelled(true);
        }
    }
}
