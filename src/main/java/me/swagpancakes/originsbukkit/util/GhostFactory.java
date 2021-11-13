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

import me.swagpancakes.originsbukkit.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The type Ghost factory.
 *
 * @author SwagPannekaker
 */
public class GhostFactory {

    private final Main plugin;
    private static final String GHOST_TEAM_NAME = "Ghosts";
    private static final long UPDATE_DELAY = 20L;

    // No players in the ghost factory
    private static final OfflinePlayer[] EMPTY_PLAYERS = new OfflinePlayer[0];
    private Team ghostTeam;

    // Task that must be cleaned up
    private BukkitTask task;
    private boolean closed;

    // Players that are actually ghosts
    private Set<String> ghosts = new HashSet<String>();

    /**
     * Instantiates a new Ghost factory.
     *
     * @param plugin the plugin
     */
    public GhostFactory(Main plugin) {
        this.plugin = plugin;
        // Initialize
        createGetTeam();
    }

    /**
     * Create get team.
     */
    private void createGetTeam() {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();

        ghostTeam = board.getTeam(GHOST_TEAM_NAME);

        // Create a new ghost team if needed
        if (ghostTeam == null) {
            ghostTeam = board.registerNewTeam(GHOST_TEAM_NAME);
        }
        // Thanks to Rprrr for noticing a bug here
        ghostTeam.setCanSeeFriendlyInvisibles(true);
    }

    /**
     * Clear members.
     */
    public void clearMembers() {
        if (ghostTeam != null) {
            for (OfflinePlayer player : getMembers()) {
                ghostTeam.removePlayer(player);
            }
        }
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(Player player) {
        validateState();
        if (!ghostTeam.hasPlayer(player)) {
            ghostTeam.addPlayer(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15, false, false));
        }
    }

    /**
     * Is ghost boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    public boolean isGhost(Player player) {
        return player != null && hasPlayer(player) && ghosts.contains(player.getName());
    }

    /**
     * Has player boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    public boolean hasPlayer(Player player) {
        validateState();
        return ghostTeam.hasPlayer(player);
    }

    /**
     * Sets ghost.
     *
     * @param player  the player
     * @param isGhost the is ghost
     */
    public void setGhost(Player player, boolean isGhost) {
        // Make sure the player is tracked by this manager
        if (!hasPlayer(player))
            addPlayer(player);

        if (isGhost) {
            ghosts.add(player.getName());
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15, false, false));
        } else {
            ghosts.remove(player.getName());
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    /**
     * Remove player.
     *
     * @param player the player
     */
    public void removePlayer(Player player) {
        validateState();
        if (ghostTeam.removePlayer(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    /**
     * Get ghosts offline player [ ].
     *
     * @return the offline player [ ]
     */
    public OfflinePlayer[] getGhosts() {
        validateState();
        Set<OfflinePlayer> players = new HashSet<OfflinePlayer>(ghostTeam.getPlayers());

        // Remove all non-ghost players
        for (Iterator<OfflinePlayer> it = players.iterator(); it.hasNext(); ) {
            if (!ghosts.contains(it.next().getName())) {
                it.remove();
            }
        }
        return toArray(players);
    }

    /**
     * Get members offline player [ ].
     *
     * @return the offline player [ ]
     */
    public OfflinePlayer[] getMembers() {
        validateState();
        return toArray(ghostTeam.getPlayers());
    }

    /**
     * To array offline player [ ].
     *
     * @param players the players
     *
     * @return the offline player [ ]
     */
    private OfflinePlayer[] toArray(Set<OfflinePlayer> players) {
        if (players != null) {
            return players.toArray(new OfflinePlayer[0]);
        } else {
            return EMPTY_PLAYERS;
        }
    }

    /**
     * Close.
     */
    public void close() {
        if (!closed) {
            task.cancel();
            ghostTeam.unregister();
            closed = true;
        }
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Validate state.
     */
    private void validateState() {
        if (closed) {
            throw new IllegalStateException("Ghost factory has closed. Cannot reuse instances.");
        }
    }
}