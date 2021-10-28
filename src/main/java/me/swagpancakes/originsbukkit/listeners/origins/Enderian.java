package me.swagpancakes.originsbukkit.listeners.origins;

import com.inkzzz.spigot.armorevent.PlayerArmorEquipEvent;
import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Enderian.
 */
public class Enderian implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Enderian.
     *
     * @param plugin the plugin
     */
    public Enderian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Enderian.plugin = plugin;
    }

    private static final HashMap<UUID, Long> cooldown = new HashMap<>();
    private static final int cooldowntime = 2;

    /**
     * Enderian join.
     *
     * @param player the player
     */
    public static void enderianJoin(Player player) {
        enderianWaterDamage(player);
    }

    /**
     * Enderian water damage.
     *
     * @param player the player
     */
    public static void enderianWaterDamage(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                    if (player.getWorld().hasStorm()) {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            player.damage(1);
                        } else if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                            player.damage(1);
                        } else {
                            enderianAirEnter(player);
                            this.cancel();
                        }
                    } else {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            player.damage(1);
                        } else {
                            enderianAirEnter(player);
                            this.cancel();
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    /**
     * Enderian air enter.
     *
     * @param player the player
     */
    public static void enderianAirEnter(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                    if (player.getWorld().hasStorm()) {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            enderianWaterDamage(player);
                            this.cancel();
                        } else if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                            enderianWaterDamage(player);
                            this.cancel();
                        }
                    } else {
                        if (player.isInWater() || material == Material.WATER_CAULDRON) {
                            enderianWaterDamage(player);
                            this.cancel();
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    /**
     * Enderian ender pearl throw.
     *
     * @param player the player
     */
    public static void enderianEnderPearlThrow(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (cooldown.containsKey(playerUUID)) {
            long secondsLeft = ((cooldown.get(playerUUID) / 1000) + cooldowntime - (System.currentTimeMillis() / 1000));

            if (secondsLeft > 0) {
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                        .toString()
                        .replace("%seconds_left%", String.valueOf(secondsLeft)));
            } else {
                player.launchProjectile(EnderPearl.class);
                cooldown.put(playerUUID, System.currentTimeMillis());
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                        .toString()
                        .replace("%player_current_origin%", String.valueOf(StorageUtils.getPlayerOrigin(playerUUID))));
            }
        } else {
            player.launchProjectile(EnderPearl.class);
            cooldown.put(playerUUID, System.currentTimeMillis());
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(StorageUtils.getPlayerOrigin(playerUUID))));
        }
    }

    /**
     * Enderian ender pearl damage immunity.
     *
     * @param event the event
     */
    @EventHandler
    public void enderianEnderPearlDamageImmunity(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();

        if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
            if (teleportCause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                event.setCancelled(true);
                player.setNoDamageTicks(1);
                player.teleport(Objects.requireNonNull(event.getTo()));
            }
        }
    }

    /**
     * Enderian pumpkin helmet wearing disability.
     *
     * @param event the event
     */
    @EventHandler
    public void enderianPumpkinHelmetWearingDisability(PlayerArmorEquipEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack itemStack = event.getItemStack();
        Material material = itemStack.getType();
        player.sendMessage("s");

        if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
            if (material == Material.CARVED_PUMPKIN) {
                player.sendMessage("a");
            }
        }
    }

    /**
     * Enderian pumpkin pickup disability.
     *
     * @param event the event
     */
    @EventHandler
    public void enderianPumpkinPickupDisability(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            assert player != null;
            UUID playerUUID = player.getUniqueId();
            Material material = event.getItem().getItemStack().getType();

            if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                if (material == Material.PUMPKIN || material == Material.CARVED_PUMPKIN || material == Material.JACK_O_LANTERN || material == Material.PUMPKIN_PIE || material == Material.PUMPKIN_SEEDS) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Enderian pumpkin pie eating disability.
     *
     * @param event the event
     */
    @EventHandler
    public void enderianPumpkinPieEatingDisability(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material material = event.getItem().getType();

        if (Objects.equals(StorageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
            if (material == Material.PUMPKIN_PIE) {
                event.setCancelled(true);
            }
        }
    }
}
