package me.swagpancakes.originsbukkit.api.wrappers;

import com.sun.istack.internal.NotNull;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import org.bukkit.entity.Player;

/**
 * The type Origin player.
 *
 * @author SwagPannekaker
 */
public class OriginPlayer {

    private final Player player;

    /**
     * Instantiates a new Origin player.
     *
     * @param player the player
     */
    public OriginPlayer(@NotNull Player player) {
        this.player = player;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return OriginsBukkit.getPlugin().getStorageUtils().getPlayerOrigin(this.player.getUniqueId());
    }

    /**
     * Find origins player data origins player data.
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData() {
        return OriginsBukkit.getPlugin().getStorageUtils().findOriginsPlayerData(this.player.getUniqueId());
    }
}
