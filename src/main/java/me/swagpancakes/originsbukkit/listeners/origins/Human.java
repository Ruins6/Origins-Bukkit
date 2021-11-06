package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * The type Human.
 */
public class Human implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Human.
     *
     * @param plugin the plugin
     */
    public Human(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Human join.
     *
     * @param player the player
     */
    public void humanJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (plugin.storageUtils.getPlayerOrigin(playerUUID) == Origins.HUMAN) {
            player.setHealthScale((10) * 2);
        }
    }
}
