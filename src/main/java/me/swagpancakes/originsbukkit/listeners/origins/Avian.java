package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Avian.
 */
public class Avian implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Avian.
     *
     * @param plugin the plugin
     */
    public Avian(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Avian.plugin = plugin;
    }
}
