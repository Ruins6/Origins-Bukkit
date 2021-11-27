/*
 * Origins-Bukkit - Origins for Bukkit and forks of Bukkit.
 * Copyright (C) 2021 SwagPannekaker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import me.swagpancakes.originsbukkit.nms.NMSHandler;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.GhostFactory;
import me.swagpancakes.originsbukkit.util.ServerVersionChecker;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The type Origins bukkit.
 *
 * @author SwagPannekaker
 */
public final class OriginsBukkit extends JavaPlugin {

    private final List<String> origins = new ArrayList<>();
    private final List<Inventory> originsInventoryGUI = new ArrayList<>();

    private NMSHandler nmsHandler;
    private ProtocolManager protocolManager;
    private StorageUtils storageUtils;
    private ConfigHandler configHandler;
    private ServerVersionChecker serverVersionChecker;
    private GhostFactory ghostFactory;
    private Arachnid arachnid;
    private Avian avian;
    private Blazeborn blazeborn;
    private Elytrian elytrian;
    private Enderian enderian;
    private Feline feline;
    private Human human;
    private Merling merling;
    private Phantom phantom;
    private Shulk shulk;
    private NoOriginPlayerRestrict noOriginPlayerRestrict;
    private PlayerOriginChecker playerOriginChecker;

    /**
     * Gets origins.
     *
     * @return the origins
     */
    public List<String> getOrigins() {
        return this.origins;
    }

    /**
     * Gets origins inventory gui.
     *
     * @return the origins inventory gui
     */
    public List<Inventory> getOriginsInventoryGUI() {
        return this.originsInventoryGUI;
    }

    /**
     * Gets nms handler.
     *
     * @return the nms handler
     */
    public NMSHandler getNMSHandler() {
        return nmsHandler;
    }

    /**
     * Gets protocol manager.
     *
     * @return the protocol manager
     */
    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    /**
     * Gets storage utils.
     *
     * @return the storage utils
     */
    public StorageUtils getStorageUtils() {
        return this.storageUtils;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    /**
     * Gets server version checker.
     *
     * @return the server version checker
     */
    public ServerVersionChecker getServerVersionChecker() {
        return this.serverVersionChecker;
    }

    /**
     * Gets ghost factory.
     *
     * @return the ghost factory
     */
    public GhostFactory getGhostFactory() {
        return this.ghostFactory;
    }

    /**
     * Gets arachnid.
     *
     * @return the arachnid
     */
    public Arachnid getArachnid() {
        return this.arachnid;
    }

    /**
     * Gets avian.
     *
     * @return the avian
     */
    public Avian getAvian() {
        return this.avian;
    }

    /**
     * Gets blazeborn.
     *
     * @return the blazeborn
     */
    public Blazeborn getBlazeborn() {
        return this.blazeborn;
    }

    /**
     * Gets elytrian.
     *
     * @return the elytrian
     */
    public Elytrian getElytrian() {
        return this.elytrian;
    }

    /**
     * Gets enderian.
     *
     * @return the enderian
     */
    public Enderian getEnderian() {
        return this.enderian;
    }

    /**
     * Gets feline.
     *
     * @return the feline
     */
    public Feline getFeline() {
        return this.feline;
    }

    /**
     * Gets human.
     *
     * @return the human
     */
    public Human getHuman() {
        return this.human;
    }

    /**
     * Gets merling.
     *
     * @return the merling
     */
    public Merling getMerling() {
        return this.merling;
    }

    /**
     * Gets phantom.
     *
     * @return the phantom
     */
    public Phantom getPhantom() {
        return this.phantom;
    }

    /**
     * Gets shulk.
     *
     * @return the shulk
     */
    public Shulk getShulk() {
        return this.shulk;
    }

    /**
     * Gets no origin player restrict.
     *
     * @return the no origin player restrict
     */
    public NoOriginPlayerRestrict getNoOriginPlayerRestrict() {
        return this.noOriginPlayerRestrict;
    }

    /**
     * Gets player origin checker.
     *
     * @return the player origin checker
     */
    public PlayerOriginChecker getPlayerOriginChecker() {
        return this.playerOriginChecker;
    }

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
    private void checkServerCompatibility() {
        serverVersionChecker.checkServerSoftwareCompatibility();
        serverVersionChecker.checkServerVersionCompatibility();
    }

    /**
     * Check server dependencies.
     */
    private void checkServerDependencies() {
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
    private void loadFiles() {
        nmsHandler = new NMSHandler(this);
        storageUtils = new StorageUtils(this);
        configHandler = new ConfigHandler(this);
        ghostFactory = new GhostFactory(this);
    }

    /**
     * Register commands.
     */
    private void registerCommands() {
        new MainCommand(this);
    }

    /**
     * Register listeners.
     */
    private void registerListeners() {
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
    private void registerItems() {
        //ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all items.");
    }

    /**
     * Start metrics.
     */
    private void startMetrics() {
        int serviceId = 13236;

        Metrics metrics = new Metrics(this, serviceId);
    }

    /**
     * Check updates.
     */
    private void checkUpdates() {

    }

    /**
     * Register recipes.
     */
    private void registerRecipes() {
        //ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Registered all item recipes.");
    }

    /**
     * Unregister recipes.
     */
    private void unregisterRecipes() {
        //ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Unregistered all item recipes.");
    }

    /**
     * Close all player inventory.
     */
    private void closeAllPlayerInventory() {
        playerOriginChecker.closeAllOriginPickerGui();

        for (Player player : getShulk().getShulkInventoryViewers()) {
            UUID playerUUID = player.getUniqueId();

            if (player.getOpenInventory().getTitle().equals(player.getName() + "'s Vault")) {
                Map<UUID, ItemStack[]> shulkPlayerStorageData = new HashMap<>();

                shulkPlayerStorageData.put(playerUUID, player.getOpenInventory().getTopInventory().getContents());
                String s = File.separator;
                File shulkPlayerStorageDataFile = new File(getDataFolder(), s + "cache" + s + "shulkdata" + s + "inventoriesdata" + s + playerUUID + ".yml");

                if (!shulkPlayerStorageDataFile.getParentFile().exists()) {
                    shulkPlayerStorageDataFile.getParentFile().mkdirs();
                }
                if (!shulkPlayerStorageDataFile.exists()) {
                    try {
                        shulkPlayerStorageDataFile.createNewFile();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                FileConfiguration shulkPlayerStorageDataConf = YamlConfiguration.loadConfiguration(shulkPlayerStorageDataFile);

                for (Map.Entry<UUID, ItemStack[]> entry : shulkPlayerStorageData.entrySet()) {
                    if (entry.getKey().equals(playerUUID)) {
                        shulkPlayerStorageDataConf.set("data." + entry.getKey(), entry.getValue());
                    }
                }
                try {
                    shulkPlayerStorageDataConf.save(shulkPlayerStorageDataFile);
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
            player.closeInventory();
        }
    }

    /**
     * Disable plugin.
     */
    public void disablePlugin() {
        this.setEnabled(false);
    }
}
