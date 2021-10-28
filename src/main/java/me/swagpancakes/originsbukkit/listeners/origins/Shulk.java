package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Shulk.
 */
public class Shulk implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Shulk.
     *
     * @param plugin the plugin
     */
    public Shulk(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Shulk.plugin = plugin;
    }
}
