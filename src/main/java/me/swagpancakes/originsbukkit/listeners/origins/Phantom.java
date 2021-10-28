package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Phantom.
 */
public class Phantom implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Phantom.
     *
     * @param plugin the plugin
     */
    public Phantom(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Phantom.plugin = plugin;
    }
}
