package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Shulk.
 */
public class Shulk implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Shulk.
     *
     * @param plugin the plugin
     */
    public Shulk(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Shulk join.
     *
     * @param player the player
     */
    public void shulkJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK)) {
            player.setHealthScale((10) * 2);
        }
    }
}
