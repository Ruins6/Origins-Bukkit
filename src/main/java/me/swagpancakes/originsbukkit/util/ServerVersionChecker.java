package me.swagpancakes.originsbukkit.util;

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.Bukkit;

/**
 * The type Server version checker.
 */
public class ServerVersionChecker {

    private final Main plugin;

    /**
     * Instantiates a new Server version checker.
     *
     * @param plugin the plugin
     */
    public ServerVersionChecker(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets server software.
     *
     * @return the server software
     */
    public String getServerSoftware() {
        String serverSoftware = Bukkit.getVersion().toUpperCase();

        if (serverSoftware.contains("CRAFTBUKKIT")) {
            return "CraftBukkit";
        }
        if (serverSoftware.contains("SPIGOT")) {
            return "Spigot";
        }
        if (serverSoftware.contains("PAPER")) {
            return "Paper";
        }
        if (serverSoftware.contains("TUINITY")) {
            return "Tuinity";
        }
        if (serverSoftware.contains("PURPUR")) {
            return "Purpur";
        }
        if (serverSoftware.contains("YATOPIA")) {
            return "Yatopia";
        }
        if (serverSoftware.contains("AIRPLANE")) {
            return "Airplane";
        } else {
            return "Custom";
        }
    }

    /**
     * Is server software safe boolean.
     *
     * @return the boolean
     */
    public boolean isServerSoftwareSafe() {
        String serverSoftware = getServerSoftware().toUpperCase();

        if (serverSoftware.contains("CRAFTBUKKIT")) {
            return true;
        }
        if (serverSoftware.contains("SPIGOT")) {
            return true;
        }
        if (serverSoftware.contains("PAPER")) {
            return true;
        }
        if (serverSoftware.contains("TUINITY")) {
            return true;
        }
        if (serverSoftware.contains("PURPUR")) {
            return true;
        }
        if (serverSoftware.contains("AIRPLANE")) {
            return true;
        }
        if (serverSoftware.contains("YATOPIA")) {
            return false;
        } else if (serverSoftware.contains("CUSTOM")) {
            return false;
        }
        return false;
    }

    /**
     * Check server software compatibility.
     */
    public void checkServerSoftwareCompatibility() {
        if (isServerSoftwareSafe()) {
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3Server running &6" + getServerSoftware() + " &3version &6" + Bukkit.getVersion());
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3(Implementing API version &6" + Bukkit.getVersion() + "&3)");
        } else {
            if (getServerSoftware().equalsIgnoreCase("YATOPIA")) {
                ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3Server running &6" + getServerSoftware() + " &3version &6" + Bukkit.getVersion());
                ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3(Implementing API version &6" + Bukkit.getVersion() + "&3)");
                ChatUtils.sendConsoleMessage("&6[Origins-Bukkit] Warning! You are using an unstable minecraft server software");
                ChatUtils.sendConsoleMessage("&6[Origins-Bukkit] that might break some plugin features! Use at your own risk.");
            } else {
                ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3Server running &6" + getServerSoftware() + " &3version &6" + Bukkit.getVersion());
                ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &3(Implementing API version &6" + Bukkit.getVersion() + "&3)");
                ChatUtils.sendConsoleMessage("&6[Origins-Bukkit] Custom Software Detected! You are using a custom server software.");
                ChatUtils.sendConsoleMessage("&6[Origins-Bukkit] There is no assurance of this plugin fully supporting the custom software!");
                ChatUtils.sendConsoleMessage("&6[Origins-Bukkit] Use at your own risk.");
            }
        }
    }

    /**
     * Gets server version.
     *
     * @return the server version
     */
    public String getServerVersion() {
        String serverVersion = Bukkit.getBukkitVersion().toUpperCase();

        if (serverVersion.contains("1.17.1")) {
            return "1.17.1";
        }
        if (serverVersion.contains("1.17")) {
            return "1.17";
        }
        if (serverVersion.contains("1.16.5")) {
            return "1.16.5";
        }
        if (serverVersion.contains("1.16.4")) {
            return "1.16.4";
        }
        if (serverVersion.contains("1.16.3")) {
            return "1.16.3";
        }
        if (serverVersion.contains("1.16.2")) {
            return "1.16.2";
        }
        if (serverVersion.contains("1.16.1")) {
            return "1.16.1";
        }
        if (serverVersion.contains("1.16")) {
            return "1.16";
        }
        if (serverVersion.contains("1.15.2")) {
            return "1.15.2";
        }
        if (serverVersion.contains("1.15.1")) {
            return "1.15.1";
        }
        if (serverVersion.contains("1.15")) {
            return "1.15";
        }
        if (serverVersion.contains("1.14.4")) {
            return "1.14.4";
        }
        if (serverVersion.contains("1.14.3")) {
            return "1.14.3";
        }
        if (serverVersion.contains("1.14.2")) {
            return "1.14.2";
        }
        if (serverVersion.contains("1.14.1")) {
            return "1.14.1";
        }
        if (serverVersion.contains("1.14")) {
            return "1.14";
        }
        if (serverVersion.contains("1.13.2")) {
            return "1.13.2";
        }
        if (serverVersion.contains("1.13.1")) {
            return "1.13.1";
        }
        if (serverVersion.contains("1.13")) {
            return "1.13";
        } else {
            return "Unknown/Unsupported Version";
        }
    }

    /**
     * Gets simplified server version.
     *
     * @return the simplified server version
     */
    public String getSimplifiedServerVersion() {
        String serverVersion = Bukkit.getBukkitVersion().toUpperCase();

        if (serverVersion.contains("1.17")) {
            return "1.17.x";
        }
        if (serverVersion.contains("1.16")) {
            return "1.16.x";
        }
        if (serverVersion.contains("1.15")) {
            return "1.15.x";
        }
        if (serverVersion.contains("1.14")) {
            return "1.14.x";
        }
        if (serverVersion.contains("1.13")) {
            return "1.13.x";
        }
        if (serverVersion.contains("1.12")) {
            return "1.12.x";
        }
        if (serverVersion.contains("1.11")) {
            return "1.11.x";
        }
        if (serverVersion.contains("1.10")) {
            return "1.10.x";
        }
        if (serverVersion.contains("1.9")) {
            return "1.9.x";
        }
        if (serverVersion.contains("1.8")) {
            return "1.8.x";
        }
        if (serverVersion.contains("1.7")) {
            return "1.7.x";
        } else {
            return "Unknown/Unsupported Version";
        }
    }

    /**
     * Check server version compatibility.
     */
    public void checkServerVersionCompatibility() {
        if (getSimplifiedServerVersion().equals("1.17.x") ||
                getSimplifiedServerVersion().equals("1.16.x") ||
                getSimplifiedServerVersion().equals("1.15.x") ||
                getSimplifiedServerVersion().equals("1.14.x") ||
                getSimplifiedServerVersion().equals("1.13.x")) {
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit]");
            ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] &dSupported Server Version Detected. Initializing!");
        } else {
            ChatUtils.sendConsoleMessage("&4[Origins-Bukkit] Unsupported Server Version Detected! Safely disabling plugin...");
            plugin.disablePlugin();
        }
    }
}
