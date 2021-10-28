package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Arachnid.
 */
public class Arachnid implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Arachnid.
     *
     * @param plugin the plugin
     */
    public Arachnid(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Arachnid.plugin = plugin;
    }
}
