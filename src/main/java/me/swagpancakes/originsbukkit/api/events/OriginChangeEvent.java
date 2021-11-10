package me.swagpancakes.originsbukkit.api.events;

import me.swagpancakes.originsbukkit.enums.Origins;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The type Origin change event.
 */
public class OriginChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Origins oldOrigin;
    private final Origins newOrigin;

    /**
     * Instantiates a new Origin change event.
     *
     * @param player    the player
     * @param oldOrigin the old origin
     * @param newOrigin the new origin
     */
    public OriginChangeEvent(Player player, Origins oldOrigin, Origins newOrigin) {
        this.player = player;
        this.oldOrigin = oldOrigin;
        this.newOrigin = newOrigin;
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
     * Gets new origin.
     *
     * @return the new origin
     */
    public Origins getNewOrigin() {
        return this.newOrigin;
    }

    /**
     * Gets old origin.
     *
     * @return the old origin
     */
    public Origins getOldOrigin() {
        return this.oldOrigin;
    }
}
