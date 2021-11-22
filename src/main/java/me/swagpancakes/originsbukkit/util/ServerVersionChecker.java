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
package me.swagpancakes.originsbukkit.util;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import org.bukkit.Bukkit;

/**
 * The type Server version checker.
 *
 * @author SwagPannekaker
 */
public class ServerVersionChecker {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Server version checker.
     *
     * @param plugin the plugin
     */
    public ServerVersionChecker(OriginsBukkit plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets server software.
     *
     * @return the server software
     */
    public String getServerSoftware() {
        String serverSoftware = Bukkit.getVersion().toUpperCase();

        if (serverSoftware.contains("BUKKIT")) {
            return "Bukkit";
        } else if (serverSoftware.contains("SPIGOT")) {
            return "Spigot";
        } else if (serverSoftware.contains("PAPER")) {
            return "Paper";
        } else if (serverSoftware.contains("TUINITY")) {
            return "Tuinity";
        } else if (serverSoftware.contains("PURPUR")) {
            return "Purpur";
        } else if (serverSoftware.contains("YATOPIA")) {
            return "Yatopia";
        } else if (serverSoftware.contains("AIRPLANE")) {
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

        if (serverSoftware.contains("BUKKIT")) {
            return true;
        } else if (serverSoftware.contains("SPIGOT")) {
            return true;
        } else if (serverSoftware.contains("PAPER")) {
            return true;
        } else if (serverSoftware.contains("TUINITY")) {
            return true;
        } else if (serverSoftware.contains("PURPUR")) {
            return true;
        } else if (serverSoftware.contains("AIRPLANE")) {
            return true;
        } else if (serverSoftware.contains("YATOPIA")) {
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
        } else if (serverVersion.contains("1.17")) {
            return "1.17";
        } else if (serverVersion.contains("1.16.5")) {
            return "1.16.5";
        } else if (serverVersion.contains("1.16.4")) {
            return "1.16.4";
        } else if (serverVersion.contains("1.16.3")) {
            return "1.16.3";
        } else if (serverVersion.contains("1.16.2")) {
            return "1.16.2";
        } else if (serverVersion.contains("1.16.1")) {
            return "1.16.1";
        } else if (serverVersion.contains("1.16")) {
            return "1.16";
        } else if (serverVersion.contains("1.15.2")) {
            return "1.15.2";
        } else if (serverVersion.contains("1.15.1")) {
            return "1.15.1";
        } else if (serverVersion.contains("1.15")) {
            return "1.15";
        } else if (serverVersion.contains("1.14.4")) {
            return "1.14.4";
        } else if (serverVersion.contains("1.14.3")) {
            return "1.14.3";
        } else if (serverVersion.contains("1.14.2")) {
            return "1.14.2";
        } else if (serverVersion.contains("1.14.1")) {
            return "1.14.1";
        } else if (serverVersion.contains("1.14")) {
            return "1.14";
        } else if (serverVersion.contains("1.13.2")) {
            return "1.13.2";
        } else if (serverVersion.contains("1.13.1")) {
            return "1.13.1";
        } else if (serverVersion.contains("1.13")) {
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
        } else if (serverVersion.contains("1.16")) {
            return "1.16.x";
        } else if (serverVersion.contains("1.15")) {
            return "1.15.x";
        } else if (serverVersion.contains("1.14")) {
            return "1.14.x";
        } else if (serverVersion.contains("1.13")) {
            return "1.13.x";
        } else if (serverVersion.contains("1.12")) {
            return "1.12.x";
        } else if (serverVersion.contains("1.11")) {
            return "1.11.x";
        } else if (serverVersion.contains("1.10")) {
            return "1.10.x";
        } else if (serverVersion.contains("1.9")) {
            return "1.9.x";
        } else if (serverVersion.contains("1.8")) {
            return "1.8.x";
        } else if (serverVersion.contains("1.7")) {
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
