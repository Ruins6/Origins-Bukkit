package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.event.Listener;

/**
 * The type Human.
 */
public class Human implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Human.
     *
     * @param plugin the plugin
     */
    public Human(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Human.plugin = plugin;
    }
}
