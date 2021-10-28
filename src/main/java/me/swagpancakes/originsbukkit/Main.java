package me.swagpancakes.originsbukkit;

import me.swagpancakes.originsbukkit.commands.MainCommand;
import me.swagpancakes.originsbukkit.config.ConfigHandler;
import me.swagpancakes.originsbukkit.items.ItemManager;
import me.swagpancakes.originsbukkit.listeners.NoOriginPlayerRestrict;
import me.swagpancakes.originsbukkit.listeners.PlayerOriginChecker;
import me.swagpancakes.originsbukkit.listeners.itemabilitiesmanager.AbilitySceptre;
import me.swagpancakes.originsbukkit.listeners.origins.*;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


/**
 * The type Main.
 */
public final class Main extends JavaPlugin {

    private static Main plugin;

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public static Main getPlugin() {
        return plugin;
    }

    /**
     * The Config handler.
     */
    public ConfigHandler configHandler = new ConfigHandler(this);

    /**
     * On enable.
     */
    @Override
    public void onEnable() {
        plugin = this;

        loadFiles();
        registerCommands();
        registerListeners();
        registerItems();
        registerRecipes();
        startMetrics();
        checkUpdates();

        ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Plugin has been enabled!");
    }

    /**
     * On disable.
     */
    @Override
    public void onDisable() {
        unregisterRecipes();
        closeAllPlayerInventory();

        ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Plugin has been disabled!");
    }

    /**
     * Load files.
     */
    public void loadFiles() {
        configHandler.setup();
        try {
            StorageUtils.loadOriginsPlayerData();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Register commands.
     */
    public void registerCommands() {
        new MainCommand(this);
    }

    /**
     * Register listeners.
     */
    public void registerListeners() {
        new Human(this);
        new Arachnid(this);
        new Avian(this);
        new Blazeborn(this);
        new Elytrian(this);
        new Enderian(this);
        new Feline(this);
        new Merling(this);
        new Phantom(this);
        new Shulk(this);
        new PlayerOriginChecker(this);
        new NoOriginPlayerRestrict(this);
        new AbilitySceptre(this);
    }

    /**
     * Register items.
     */
    public void registerItems() {
        ItemManager.init();

        ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all items.");
    }

    /**
     * Start metrics.
     */
    public void startMetrics() {

    }

    /**
     * Check updates.
     */
    public void checkUpdates() {

    }

    /**
     * Register recipes.
     */
    public void registerRecipes() {
        Bukkit.getServer().addRecipe(ItemManager.abilitySceptreRecipe);

        ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all item recipes.");
    }

    /**
     * Unregister recipes.
     */
    public void unregisterRecipes() {
        Bukkit.getServer().removeRecipe(NamespacedKey.minecraft("ability_sceptre"));

        ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Unregistered all item recipes.");
    }

    /**
     * Close all player inventory.
     */
    public void closeAllPlayerInventory() {
        PlayerOriginChecker.closeAllOriginPickerGui();
    }

    /**
     * Disable plugin.
     */
    public void disablePlugin() {
        this.setEnabled(false);
    }
}
