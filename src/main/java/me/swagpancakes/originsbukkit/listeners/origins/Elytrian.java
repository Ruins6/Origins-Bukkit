package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Elytrian.
 */
public class Elytrian implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Elytrian.
     *
     * @param plugin the plugin
     */
    public Elytrian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Elytrian join.
     *
     * @param player the player
     */
    public void elytrianJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.ELYTRIAN)) {
            player.setHealthScale((10)*2);
        }
    }
}
