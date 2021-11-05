package me.swagpancakes.originsbukkit.config;

import com.tchristofferson.configupdater.ConfigUpdater;
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
    private YamlConfiguration CONFIG;
    private File CONFIG_FILE;
    private YamlConfiguration LANG;
    private File LANG_FILE;

    /**
     * Instantiates a new Config handler.
     *
     * @param plugin the plugin
     */
    public ConfigHandler(Main plugin) {
        this.plugin = plugin;
    }

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
        try {
            loadConfig();
            loadLang();
        } catch (Exception event) {
            event.printStackTrace();
        }
        reloadFiles();
    }

    /**
     * Load config yaml configuration.
     *
     * @return the yaml configuration
     */
    public YamlConfiguration loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] The config.yml file was not found. Creating one...");
            try {
                configFile.createNewFile();
                InputStream defaultConfigStream = plugin.getResource("config.yml");
                if (defaultConfigStream != null) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));
                    defaultConfig.save(configFile);
                    try {
                        ConfigUpdater.update(plugin, "config.yml", configFile, null);
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                    Config.setFile(defaultConfig);
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Successfully created the config.yml file");
                    return defaultConfig;
                }
            } catch (IOException event) {
                event.printStackTrace();
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Couldn't create config file.");
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] This is a fatal error. Now disabling.");
                plugin.disablePlugin();
            }
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        for (Config item : Config.values()) {
            if (yamlConfiguration.getString(item.getPath()) == null) {
                yamlConfiguration.set(item.getPath(), item.getDefaultValue());
            }
        }
        Config.setFile(yamlConfiguration);
        CONFIG = yamlConfiguration;
        CONFIG_FILE = configFile;
        try {
            yamlConfiguration.save(getConfigFile());
        } catch (IOException event) {
            event.printStackTrace();
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Failed to save lang.yml.");
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] this stack trace to <your name>.");
        }
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, null);
        } catch (IOException event) {
            event.printStackTrace();
        }
        return yamlConfiguration;
    }

    /**
     * Load lang yaml configuration.
     *
     * @return the yaml configuration
     */
    public YamlConfiguration loadLang() {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] The lang.yml file was not found. Creating one...");
            try {
                langFile.createNewFile();
                InputStream defaultConfigStream = plugin.getResource("lang.yml");
                if (defaultConfigStream != null) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));
                    defaultConfig.save(langFile);
                    try {
                        ConfigUpdater.update(plugin, "lang.yml", langFile, null);
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                    Lang.setFile(defaultConfig);
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Successfully created the lang.yml file");
                    return defaultConfig;
                }
            } catch (IOException event) {
                event.printStackTrace();
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Couldn't create language file.");
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] This is a fatal error. Now disabling.");
                plugin.disablePlugin();
            }
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(langFile);
        for (Lang item : Lang.values()) {
            if (yamlConfiguration.getString(item.getPath()) == null) {
                yamlConfiguration.set(item.getPath(), item.getDefaultValue());
            }
        }
        Lang.setFile(yamlConfiguration);
        LANG = yamlConfiguration;
        LANG_FILE = langFile;
        try {
            yamlConfiguration.save(getLangFile());
        } catch (IOException event) {
            event.printStackTrace();
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Failed to save lang.yml.");
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] this stack trace to <your name>.");
        }
        try {
            ConfigUpdater.update(plugin, "lang.yml", langFile, null);
        } catch (IOException event) {
            event.printStackTrace();
        }
        return yamlConfiguration;
    }

    /**
     * Reload files.
     */
    public void reloadFiles() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        File langFile = new File(plugin.getDataFolder(), "lang.yml");

        if (configFile.exists()) {
            loadConfig();
        }
        if (langFile.exists()) {
            loadLang();
        }
    }
}
