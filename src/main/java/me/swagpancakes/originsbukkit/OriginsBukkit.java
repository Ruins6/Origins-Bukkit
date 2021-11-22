/*
 *     Origins-Bukkit
 *     Copyright (C) 2021 SwagPannekaker
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.swagpancakes.originsbukkit.commands.MainCommand;
import me.swagpancakes.originsbukkit.config.ConfigHandler;
import me.swagpancakes.originsbukkit.listeners.KeyListener;
import me.swagpancakes.originsbukkit.listeners.NoOriginPlayerRestrict;
import me.swagpancakes.originsbukkit.listeners.PlayerOriginChecker;
import me.swagpancakes.originsbukkit.listeners.origins.*;
import me.swagpancakes.originsbukkit.metrics.Metrics;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.GhostFactory;
import me.swagpancakes.originsbukkit.util.ServerVersionChecker;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Origins bukkit.
 *
 * @author SwagPannekaker
 */
public final class OriginsBukkit extends JavaPlugin {

    public List<String> origins = new ArrayList<>();
    public List<Inventory> originsInventoryGUI = new ArrayList<>();

    public ProtocolManager protocolManager;
    public StorageUtils storageUtils;
    public ConfigHandler configHandler;
    public ServerVersionChecker serverVersionChecker;
    public GhostFactory ghostFactory;
    public Arachnid arachnid;
    public Avian avian;
    public Blazeborn blazeborn;
    public Elytrian elytrian;
    public Enderian enderian;
    public Feline feline;
    public Human human;
    public Merling merling;
    public Phantom phantom;
    public Shulk shulk;
    public NoOriginPlayerRestrict noOriginPlayerRestrict;
    public PlayerOriginChecker playerOriginChecker;

    private static OriginsBukkit plugin;

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public static OriginsBukkit getPlugin() {
        return plugin;
    }

    /**
     * On enable.
     */
    @Override
    public void onEnable() {
        plugin = this;

        serverVersionChecker = new ServerVersionChecker(this);

        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &4   ___       _       _                 ____        _    _    _ _");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &c  / _ \\ _ __(_) __ _(_)_ __  ___      | __ ) _   _| | _| | _(_) |_");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &6 | | | | '__| |/ _` | | '_ \\/ __|_____|  _ \\| | | | |/ / |/ / | __|");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &e | |_| | |  | | (_| | | | | \\__ \\_____| |_) | |_| |   <|   <| | |_");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &a  \\___/|_|  |_|\\__, |_|_| |_|___/     |____/ \\__,_|_|\\_\\_|\\_\\_|\\__|");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &b               |___/");
        ChatUtils.sendConsoleMessage("&3[Origins-Bukkit]");
        checkServerCompatibility();
        checkServerDependencies();

        if (plugin.isEnabled()) {
            protocolManager = ProtocolLibrary.getProtocolManager();

            init();

            ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Plugin has been enabled!");
        }
    }

    /**
     * On disable.
     */
    @Override
    public void onDisable() {
        unregisterRecipes();
        closeAllPlayerInventory();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();

            if (storageUtils.findOriginsPlayerData(playerUUID) == null) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Plugin has been disabled!");
    }

    /**
     * Check server compatibility.
     */
    public void checkServerCompatibility() {
        serverVersionChecker.checkServerSoftwareCompatibility();
        serverVersionChecker.checkServerVersionCompatibility();
    }

    /**
     * Check server dependencies.
     */
    public void checkServerDependencies() {
        if (plugin.isEnabled()) {
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] Checking dependencies...");
        }
        if (plugin.isEnabled()) {
            Plugin protocolLib = Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib");

            if (protocolLib != null) {
                if (protocolLib.isEnabled()) {
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] ProtocolLib found! Hooking...");
                } else {
                    ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] ProtocolLib seems to be disabled. Safely disabling plugin...");
                    disablePlugin();
                }
            } else {
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Dependency not found (ProtocolLib). Safely disabling plugin...");
                disablePlugin();
            }
        }
        if (plugin.isEnabled()) {
            Plugin pancakeLibCore = Bukkit.getServer().getPluginManager().getPlugin("PancakeLibCore");

            if (pancakeLibCore != null) {
                if (pancakeLibCore.isEnabled()) {
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] PancakeLibCore found! Hooking...");
                } else {
                    ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] PancakeLibCore seems to be disabled. Safely disabling plugin...");
                    disablePlugin();
                }
            } else {
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Dependency not found (PancakeLibCore). Safely disabling plugin...");
                disablePlugin();
            }
        }
    }

    /**
     * Init.
     */
    private void init() {
        loadFiles();
        registerCommands();
        registerListeners();
        registerItems();
        registerRecipes();
        startMetrics();
        checkUpdates();
    }

    /**
     * Load files.
     */
    public void loadFiles() {
        storageUtils = new StorageUtils(this);
        configHandler = new ConfigHandler(this);
        ghostFactory = new GhostFactory(this);
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
        noOriginPlayerRestrict = new NoOriginPlayerRestrict(this);
        playerOriginChecker = new PlayerOriginChecker(this);
        new KeyListener(this);
    }

    /**
     * Register items.
     */
    public void registerItems() {
        //ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all items.");
    }

    /**
     * Start metrics.
     */
    public void startMetrics() {
        int serviceId = 13236;

        Metrics metrics = new Metrics(this, serviceId);
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
        //ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all item recipes.");
    }

    /**
     * Unregister recipes.
     */
    public void unregisterRecipes() {
        //ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Unregistered all item recipes.");
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
