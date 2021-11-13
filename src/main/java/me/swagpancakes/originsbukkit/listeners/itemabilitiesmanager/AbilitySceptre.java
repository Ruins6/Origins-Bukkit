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
package me.swagpancakes.originsbukkit.listeners.itemabilitiesmanager;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Ability sceptre.
 *
 * @author SwagPannekaker
 */
public class AbilitySceptre implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Ability sceptre.
     *
     * @param plugin the plugin
     */
    public AbilitySceptre(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * On ability sceptre right click.
     *
     * @param event the event
     */
    @EventHandler
    public void onAbilitySceptreRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                if (Objects.equals(event.getItem().getItemMeta(), plugin.itemManager.abilitySceptre.getItemMeta())) {
                    if (plugin.storageUtils.getPlayerOrigin(playerUUID) != null) {
                        switch (plugin.storageUtils.getPlayerOrigin(playerUUID)) {
                            case HUMAN:
                                ChatUtils.sendPlayerMessage(player, "&bHuman :D");
                                break;
                            case ENDERIAN:
                                plugin.enderian.enderianEnderPearlThrow(player);
                                break;
                            case MERLING:
                                break;
                            case PHANTOM:
                                break;
                            case ELYTRIAN:
                                plugin.elytrian.elytrianLaunchIntoAir(player);
                                break;
                            case BLAZEBORN:
                                break;
                            case AVIAN:
                                break;
                            case ARACHNID:
                                break;
                            case SHULK:
                                break;
                            case FELINE:
                                break;
                        }
                    }
                }
            }
        }
    }
}
