package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Phantom.
 */
public class Phantom implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Phantom.
     *
     * @param plugin the plugin
     */
    public Phantom(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Phantom join.
     *
     * @param player the player
     */
    public void phantomJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.PHANTOM)) {
            player.setHealthScale((10)*2);
        }
    }
}
