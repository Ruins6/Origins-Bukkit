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

    private Main plugin;

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public Main getPlugin() {
        return plugin;
    }

    /**
     * The Config handler.
     */
    public ConfigHandler configHandler = new ConfigHandler(this);

    /**
     * The Item manager.
     */
    public ItemManager itemManager = new ItemManager(this);

    /**
     * The Storage utils.
     */
    public StorageUtils storageUtils = new StorageUtils(this);

    /**
     * The Arachnid.
     */
    public Arachnid arachnid;
    /**
     * The Avian.
     */
    public Avian avian;
    /**
     * The Blazeborn.
     */
    public Blazeborn blazeborn;
    /**
     * The Elytrian.
     */
    public Elytrian elytrian;
    /**
     * The Enderian.
     */
    public Enderian enderian;
    /**
     * The Feline.
     */
    public Feline feline;
    /**
     * The Human.
     */
    public Human human;
    /**
     * The Merling.
     */
    public Merling merling;
    /**
     * The Phantom.
     */
    public Phantom phantom;
    /**
     * The Shulk.
     */
    public Shulk shulk;
    /**
     * The Player origin checker.
     */
    public PlayerOriginChecker playerOriginChecker;
    /**
     * The No origin player restrict.
     */
    public NoOriginPlayerRestrict noOriginPlayerRestrict;

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
            storageUtils.loadOriginsPlayerData();
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
        human = new Human(this);
        arachnid = new Arachnid(this);
        avian = new Avian(this);
        blazeborn = new Blazeborn(this);
        elytrian = new Elytrian(this);
        enderian = new Enderian(this);
        feline = new Feline(this);
        merling = new Merling(this);
        phantom = new Phantom(this);
        shulk = new Shulk(this);
        playerOriginChecker = new PlayerOriginChecker(this);
        noOriginPlayerRestrict = new NoOriginPlayerRestrict(this);
        new AbilitySceptre(this);
    }

    /**
     * Register items.
     */
    public void registerItems() {
        itemManager.init();

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
        Bukkit.getServer().addRecipe(itemManager.abilitySceptreRecipe);

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
        playerOriginChecker.closeAllOriginPickerGui();
    }

    /**
     * Disable plugin.
     */
    public void disablePlugin() {
        this.setEnabled(false);
    }
}
