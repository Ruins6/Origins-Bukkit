package me.swagpancakes.originsbukkit.config;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The type Config handler.
 */
public class ConfigHandler {

    private final Main plugin;

    /**
     * Instantiates a new Config handler.
     *
     * @param plugin the plugin
     */
    public ConfigHandler(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * The constant CONFIG.
     */
    public static YamlConfiguration CONFIG;
    /**
     * The constant CONFIG_FILE.
     */
    public static File CONFIG_FILE;
    /**
     * The constant LANG.
     */
    public static YamlConfiguration LANG;
    /**
     * The constant LANG_FILE.
     */
    public static File LANG_FILE;

    /**
     * Gets config.
     *
     * @return the config
     */
    public FileConfiguration getConfig() {
        return CONFIG;
    }

    /**
     * Gets config file.
     *
     * @return the config file
     */
    public File getConfigFile() {
        return CONFIG_FILE;
    }

    /**
     * Gets lang.
     *
     * @return the lang
     */
    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Gets lang file.
     *
     * @return the lang file
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    /**
     * Sets .
     */
    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        loadConfig();
        loadLang();
    }

    /**
     * Load config yaml configuration.
     *
     * @return the yaml configuration
     */
    public YamlConfiguration loadConfig() {
        File config = new File(plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            try {
                plugin.getDataFolder().mkdir();
                config.createNewFile();
                InputStream defConfigStream = plugin.getResource("config.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
                    defConfig.save(config);
                    Config.setFile(defConfig);
                    return defConfig;
                }
            } catch(IOException event) {
                event.printStackTrace();
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Couldn't create config file.");
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] This is a fatal error. Now disabling.");
                plugin.disablePlugin();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(config);
        for (Config item : Config.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefaultValue());
            }
        }
        Config.setFile(conf);
        CONFIG = conf;
        CONFIG_FILE = config;
        try {
            conf.save(getConfigFile());
        } catch(IOException event) {
            event.printStackTrace();
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Failed to save lang.yml.");
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] this stack trace to <your name>.");
        }
        return conf;
    }

    /**
     * Load lang yaml configuration.
     *
     * @return the yaml configuration
     */
    public YamlConfiguration loadLang() {
        File lang = new File(plugin.getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                plugin.getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = plugin.getResource("lang.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return defConfig;
                }
            } catch(IOException event) {
                event.printStackTrace();
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Couldn't create language file.");
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] This is a fatal error. Now disabling.");
                plugin.disablePlugin();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefaultValue());
            }
        }
        Lang.setFile(conf);
        LANG = conf;
        LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException event) {
            event.printStackTrace();
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Failed to save lang.yml.");
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] this stack trace to <your name>.");
        }
        return conf;
    }
}
