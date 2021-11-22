package me.swagpancakes.originsbukkit.api.events;

import com.sun.istack.internal.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The type Player origin ability use event.
 *
 * @author SwagPannekaker
 */
public class PlayerOriginAbilityUseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String origin;

    /**
     * Instantiates a new Player origin ability use event.
     *
     * @param player the player
     * @param origin the origin
     */
    public PlayerOriginAbilityUseEvent(@NotNull Player player, @NotNull String origin) {
        this.player = player;
        this.origin = origin;
    }

    /**
     * Gets handlers.
     *
     * @return the handlers
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets handler list.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
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
        return this.origin;
    }
}
