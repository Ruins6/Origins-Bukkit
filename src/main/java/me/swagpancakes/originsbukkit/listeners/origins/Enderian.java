package me.swagpancakes.originsbukkit.listeners.origins;

import com.inkzzz.spigot.armorevent.PlayerArmorEquipEvent;
import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
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

    private final Main plugin;
    private final HashMap<UUID, Long> COOLDOWN = new HashMap<>();
    private final int COOLDOWNTIME = Config.ORIGINS_ENDERIAN_ABILITY_COOLDOWN.toInt();

    /**
     * Instantiates a new Enderian.
     *
     * @param plugin the plugin
     */
    public Enderian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Enderian join.
     *
     * @param player the player
     */
    public void enderianJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
            player.setHealthScale(Config.ORIGINS_ENDERIAN_MAX_HEALTH.toDouble());
            enderianWaterDamage(player);
        }
    }

    /**
     * Enderian water damage.
     *
     * @param player the player
     */
    public void enderianWaterDamage(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                    if (player.isOnline()) {
                        if (player.getWorld().hasStorm()) {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                player.damage(Config.ORIGINS_ENDERIAN_WATER_DAMAGE_AMOUNT.toDouble());
                            } else if (location.getBlockY() > player.getWorld().getHighestBlockAt(location).getLocation().getBlockY()) {
                                player.damage(Config.ORIGINS_ENDERIAN_WATER_DAMAGE_AMOUNT.toDouble());
                            } else {
                                enderianAirEnter(player);
                                this.cancel();
                            }
                        } else {
                            if (player.isInWater() || material == Material.WATER_CAULDRON) {
                                player.damage(Config.ORIGINS_ENDERIAN_WATER_DAMAGE_AMOUNT.toDouble());
                            } else {
                                enderianAirEnter(player);
                                this.cancel();
                            }
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, Config.ORIGINS_ENDERIAN_WATER_DAMAGE_DELAY.toLong(), Config.ORIGINS_ENDERIAN_WATER_DAMAGE_PERIOD_DELAY.toLong());
    }

    /**
     * Enderian air enter.
     *
     * @param player the player
     */
    public void enderianAirEnter(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                UUID playerUUID = player.getUniqueId();
                Location location = player.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();

                if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
                    if (player.isOnline()) {
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
    public void enderianEnderPearlThrow(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (COOLDOWN.containsKey(playerUUID)) {
            long secondsLeft = ((COOLDOWN.get(playerUUID) / 1000) + COOLDOWNTIME - (System.currentTimeMillis() / 1000));

            if (secondsLeft > 0) {
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_COOLDOWN
                        .toString()
                        .replace("%seconds_left%", String.valueOf(secondsLeft)));
            } else {
                player.launchProjectile(EnderPearl.class);
                COOLDOWN.put(playerUUID, System.currentTimeMillis());
                ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                        .toString()
                        .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
            }
        } else {
            player.launchProjectile(EnderPearl.class);
            COOLDOWN.put(playerUUID, System.currentTimeMillis());
            ChatUtils.sendPlayerMessage(player, Lang.PLAYER_ORIGIN_ABILITY_USE
                    .toString()
                    .replace("%player_current_origin%", String.valueOf(plugin.storageUtils.getPlayerOrigin(playerUUID))));
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
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

            if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
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

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ENDERIAN)) {
            if (material == Material.PUMPKIN_PIE) {
                event.setCancelled(true);
            }
        }
    }
}
