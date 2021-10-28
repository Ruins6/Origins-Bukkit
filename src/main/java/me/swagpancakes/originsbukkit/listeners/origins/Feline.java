package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Feline.
 */
public class Feline implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Feline.
     *
     * @param plugin the plugin
     */
    public Feline(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Feline.plugin = plugin;
    }
}
