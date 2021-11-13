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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Lists;
import me.swagpancakes.originsbukkit.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The type Ghost maker.
 *
 * @author SwagPannekaker
 */
public class GhostMaker {

    private final Main plugin;
    private final Set<UUID> ghosts = new HashSet<>(); //Keeps track of players who should be semi-transparent

    /**
     * Instantiates a new Ghost maker.
     *
     * @param plugin the plugin
     */
    public GhostMaker(Main plugin) {
        this.plugin = plugin;
        plugin.protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SPAWN_ENTITY) { //Listen for anytime a player may see another entity
            @Override
            public void onPacketSending(PacketEvent event) {
                Entity entity = event.getPacket().getEntityModifier(event).read(0);
                if (entity instanceof Player
                        && ghosts.contains(entity.getUniqueId())) { //Player can potentially see a ghost
                    showAsGhost(event.getPlayer(), (Player) entity); //Render the ghost as semi-transparent
                }
            }
        });
    }

    /**
     * Add ghost.
     *
     * @param player the player
     */
    public void addGhost(Player player) {
        if (ghosts.add(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, false, false), true); //Apply invisibility to the ghost
            showAsGhost(player, player); //Let ghost see themselves as a ghost
            for (Player viewer : plugin.protocolManager.getEntityTrackers(player)) { //Send a packet to anyone who can "see" the ghost
                showAsGhost(viewer, player);
            }
        }
    }

    /**
     * Remove ghost.
     *
     * @param player the player
     */
    public void removeGhost(Player player) {
        if (ghosts.remove(player.getUniqueId())) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY); //Remove invisibility
            for (Player viewer : Bukkit.getServer().getOnlinePlayers()) { //Send removal packets to every player (some that recv'd the addGhost packet may no longer be in range)
                PacketContainer packet = plugin.protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM, true);
                packet.getStrings().write(0, viewer.getEntityId() + "." + player.getEntityId()); //Make the team name unique to both the viewer and the ghost
                packet.getIntegers().write(1, 1); //We are removing this team
                try {
                    plugin.protocolManager.sendServerPacket(viewer, packet); //Only the viewer needs to be sent the packet
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Show as ghost.
     *
     * @param viewer the viewer
     * @param player the player
     */
    private void showAsGhost(Player viewer, Player player) {
        PacketContainer packet = plugin.protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM, true);
        packet.getStrings().write(0, viewer.getEntityId() + "." + player.getEntityId()); //Make the team name unique to both the viewer and the ghost
        packet.getIntegers().write(1, 0); //We are creating a new team
        packet.getModifier().write(6, Lists.newArrayList(viewer.getName(), player.getName())); //Team only consists of the ghost and the viewer
        packet.getIntegers().write(2, 3); //Ghost can be seen and attacked by the viewer
        try {
            plugin.protocolManager.sendServerPacket(viewer, packet); //Only the viewer needs to be sent the packet
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
