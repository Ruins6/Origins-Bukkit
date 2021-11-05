package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Feline.
 */
public class Feline implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Feline.
     *
     * @param plugin the plugin
     */
    public Feline(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * Feline join.
     *
     * @param player the player
     */
    public void felineJoin(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.FELINE)) {
            player.setHealthScale((9) * 2);
        }
    }
}
