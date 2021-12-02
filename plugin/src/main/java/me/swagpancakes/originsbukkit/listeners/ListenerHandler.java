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
package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.listeners.items.ItemListenerHandler;
import me.swagpancakes.originsbukkit.listeners.keys.KeyListener;
import me.swagpancakes.originsbukkit.listeners.origins.OriginListenerHandler;
import me.swagpancakes.originsbukkit.listeners.playerchecks.NoOriginPlayerRestrict;
import me.swagpancakes.originsbukkit.listeners.playerchecks.PlayerOriginChecker;

/**
 * The type Listener handler.
 *
 * @author SwagPannekaker
 */
public class ListenerHandler {

    private final OriginsBukkit plugin;
    private OriginListenerHandler originListenerHandler;
    private ItemListenerHandler itemListenerHandler;
    private NoOriginPlayerRestrict noOriginPlayerRestrict;
    private PlayerOriginChecker playerOriginChecker;
    private KeyListener keyListener;

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public OriginsBukkit getPlugin() {
        return plugin;
    }

    /**
     * Gets origin listener handler.
     *
     * @return the origin listener handler
     */
    public OriginListenerHandler getOriginListenerHandler() {
        return originListenerHandler;
    }

    /**
     * Gets item listener handler.
     *
     * @return the item listener handler
     */
    public ItemListenerHandler getItemListenerHandler() {
        return itemListenerHandler;
    }

    /**
     * Gets no origin player restrict.
     *
     * @return the no origin player restrict
     */
    public NoOriginPlayerRestrict getNoOriginPlayerRestrict() {
        return noOriginPlayerRestrict;
    }

    /**
     * Gets player origin checker.
     *
     * @return the player origin checker
     */
    public PlayerOriginChecker getPlayerOriginChecker() {
        return playerOriginChecker;
    }

    /**
     * Gets key listener.
     *
     * @return the key listener
     */
    public KeyListener getKeyListener() {
        return keyListener;
    }

    /**
     * Instantiates a new Listener handler.
     *
     * @param plugin the plugin
     */
    public ListenerHandler(OriginsBukkit plugin) {
        this.plugin = plugin;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        originListenerHandler = new OriginListenerHandler(this);
        itemListenerHandler = new ItemListenerHandler(this);
        noOriginPlayerRestrict = new NoOriginPlayerRestrict(this);
        playerOriginChecker = new PlayerOriginChecker(this);
        keyListener = new KeyListener(this);
    }
}
