package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.UUID;

/**
 * The type Key listener.
 */
public class KeyListener implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Key listener.
     *
     * @param plugin the plugin
     */
    public KeyListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * On ability use.
     *
     * @param event the event
     */
    @EventHandler
    public void onAbilityUse(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (player.isSneaking()) {
            if (plugin.storageUtils.getPlayerOrigin(playerUUID) != null) {
                switch (plugin.storageUtils.getPlayerOrigin(playerUUID)) {
                    case HUMAN:
                        ChatUtils.sendPlayerMessage(player, "&bHuman :D");
                        break;
                    case ENDERIAN:
                        plugin.enderian.enderianEnderPearlThrow(player);
                        break;
                    case MERLING:
                        break;
                    case PHANTOM:
                        break;
                    case ELYTRIAN:
                        plugin.elytrian.elytrianLaunchIntoAir(player);
                        break;
                    case BLAZEBORN:
                        break;
                    case AVIAN:
                        break;
                    case ARACHNID:
                        break;
                    case SHULK:
                        break;
                    case FELINE:
                        break;
                }
                event.setCancelled(true);
            }
        }
    }
}
