package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Avian.
 */
public class Avian implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Avian.
     *
     * @param plugin the plugin
     */
    public Avian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Avian join.
     *
     * @param player the player
     */
    public void avianJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            player.setHealthScale((10) * 2);
            player.setWalkSpeed(0.250F);
            avianSlowFalling(player);
        }
    }

    /**
     * Avian slow falling.
     *
     * @param player the player
     */
    public void avianSlowFalling(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW_FALLING,
                    Integer.MAX_VALUE,
                    0,
                    false,
                    false));
        }
    }

    /**
     * On avian slow falling toggle.
     *
     * @param event the event
     */
    @EventHandler
    public void onAvianSlowFallingToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            if (player.isGliding()) {
                if (!player.isSneaking()) {
                    player.setVelocity(player.getVelocity().setY(0));
                    player.addPotionEffect(new PotionEffect(
                            PotionEffectType.SLOW_FALLING,
                            Integer.MAX_VALUE,
                            0,
                            false,
                            false));
                } else {
                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                }
            } else {
                if (player.isSneaking()) {
                    player.setVelocity(player.getVelocity().setY(0));
                    player.addPotionEffect(new PotionEffect(
                            PotionEffectType.SLOW_FALLING,
                            Integer.MAX_VALUE,
                            0,
                            false,
                            false));
                } else {
                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                }
            }
        }
    }

    /**
     * On avian slow falling elytra toggle.
     *
     * @param event the event
     */
    @EventHandler
    public void onAvianSlowFallingElytraToggle(EntityToggleGlideEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
                if (!player.isGliding()) {
                    if (player.isSneaking()) {
                        player.setVelocity(player.getVelocity().setY(0));
                        player.addPotionEffect(new PotionEffect(
                                PotionEffectType.SLOW_FALLING,
                                Integer.MAX_VALUE,
                                0,
                                false,
                                false));
                    } else {
                        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                } else {
                    if (!player.isSneaking()) {
                        player.setVelocity(player.getVelocity().setY(0));
                        player.addPotionEffect(new PotionEffect(
                                PotionEffectType.SLOW_FALLING,
                                Integer.MAX_VALUE,
                                0,
                                false,
                                false));
                    } else {
                        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                }
            }
        }
    }

    /**
     * On avian sleep.
     *
     * @param event the event
     */
    @EventHandler
    public void onAvianSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Block bed = event.getBed();
        Location bedLocation = bed.getLocation();
        double bedLocationY = bedLocation.getY();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            if (bedLocationY < 86) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * On avian bed respawn point set.
     *
     * @param event the event
     */
    @EventHandler
    public void onAvianBedRespawnPointSet(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (clickedBlock != null && clickedBlock.getLocation().getY() < 86 && Tag.BEDS.isTagged(clickedBlock.getType())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * On avian egg lay.
     *
     * @param event the event
     */
    @EventHandler
    public void onAvianEggLay(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        PlayerInventory playerInventory = player.getInventory();
        UUID playerUUID = player.getUniqueId();
        World world = player.getWorld();
        long getWorldTime = world.getTime();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            if (getWorldTime == 0) {
                if (playerInventory.firstEmpty() == -1) {
                    world.dropItem(location, new ItemStack(Material.EGG, 1));
                    ChatUtils.sendPlayerMessage(player, "&cYour inventory was full, so we dropped your egg on the ground.");
                } else {
                    playerInventory.addItem(new ItemStack(Material.EGG, 1));
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("You laid an egg!"));
            }
        }
    }

    /**
     * Avian anti slow falling effect remove.
     *
     * @param event the event
     */
    @EventHandler
    public void avianAntiSlowFallingEffectRemove(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID playerUUID = player.getUniqueId();
            PotionEffect oldEffect = event.getOldEffect();
            EntityPotionEffectEvent.Cause cause = event.getCause();

            if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
                if (oldEffect != null) {
                    if (oldEffect.getType().equals(PotionEffectType.SLOW_FALLING) && cause != EntityPotionEffectEvent.Cause.PLUGIN) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Avian eating disabilities.
     *
     * @param event the event
     */
    @EventHandler
    public void avianEatingDisabilities(PlayerItemConsumeEvent event) {
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

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.AVIAN) {
            if (materials.contains(material)) {
                event.setCancelled(true);
            }
        }
    }
}
