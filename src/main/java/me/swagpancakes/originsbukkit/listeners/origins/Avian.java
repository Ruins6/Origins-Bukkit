package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
        }
    }
}
