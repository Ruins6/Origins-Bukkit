package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Elytrian.
 */
public class Elytrian implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Elytrian.
     *
     * @param plugin the plugin
     */
    public Elytrian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Elytrian.plugin = plugin;
    }
}
