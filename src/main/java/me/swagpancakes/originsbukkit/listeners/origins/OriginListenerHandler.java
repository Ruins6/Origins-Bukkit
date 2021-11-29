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
package me.swagpancakes.originsbukkit.listeners.origins;

import me.swagpancakes.originsbukkit.listeners.ListenerHandler;

/**
 * The type Origin listener handler.
 *
 * @author SwagPannekaker
 */
public class OriginListenerHandler {
    
    private final ListenerHandler listenerHandler;
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

    /**
     * Gets listener handler.
     *
     * @return the listener handler
     */
    public ListenerHandler getListenerHandler() {
        return listenerHandler;
    }

    /**
     * Gets arachnid.
     *
     * @return the arachnid
     */
    public Arachnid getArachnid() {
        return arachnid;
    }

    /**
     * Gets avian.
     *
     * @return the avian
     */
    public Avian getAvian() {
        return avian;
    }

    /**
     * Gets blazeborn.
     *
     * @return the blazeborn
     */
    public Blazeborn getBlazeborn() {
        return blazeborn;
    }

    /**
     * Gets elytrian.
     *
     * @return the elytrian
     */
    public Elytrian getElytrian() {
        return elytrian;
    }

    /**
     * Gets enderian.
     *
     * @return the enderian
     */
    public Enderian getEnderian() {
        return enderian;
    }

    /**
     * Gets feline.
     *
     * @return the feline
     */
    public Feline getFeline() {
        return feline;
    }

    /**
     * Gets human.
     *
     * @return the human
     */
    public Human getHuman() {
        return human;
    }

    /**
     * Gets merling.
     *
     * @return the merling
     */
    public Merling getMerling() {
        return merling;
    }

    /**
     * Gets phantom.
     *
     * @return the phantom
     */
    public Phantom getPhantom() {
        return phantom;
    }

    /**
     * Gets shulk.
     *
     * @return the shulk
     */
    public Shulk getShulk() {
        return shulk;
    }

    /**
     * Instantiates a new Origin listener handler.
     *
     * @param listenerHandler the listener handler
     */
    public OriginListenerHandler(ListenerHandler listenerHandler) {
        this.listenerHandler = listenerHandler;
        init();
    }

    /**
     * Init.
     */
    private void init() {
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
    }
}
