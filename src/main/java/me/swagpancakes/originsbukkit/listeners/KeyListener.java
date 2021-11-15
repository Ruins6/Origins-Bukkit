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
package me.swagpancakes.originsbukkit.listeners;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.UUID;

/**
 * The type Key listener.
 *
 * @author SwagPannekaker
 */
public class KeyListener implements Listener {

    private final Main plugin;

    /**
     * Instantiates a new Key listener.
     *
     * @param plugin the plugin
     */
    public KeyListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /**
     * On ability use.
     *
     * @param event the event
     */
    @EventHandler
    public void onAbilityUse(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (player.isSneaking()) {
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
                        plugin.arachnid.arachnidClimbToggleAbility(player);
                        break;
                    case SHULK:
                        plugin.shulk.shulkInventoryAbility(player);
                        break;
                    case FELINE:
                        break;
                }
                event.setCancelled(true);
            }
        }
    }
}
