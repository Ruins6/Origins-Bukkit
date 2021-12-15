package me.lemonypancakes.originsbukkit.storage.wrappers;

import java.util.UUID;

/**
 * The type Blazeborn nether spawn data wrapper.
 *
 * @author SwagPannekaker
 */
public class BlazebornNetherSpawnDataWrapper {

    private UUID playerUUID;
    private boolean firstTime;

    /**
     * Instantiates a new Blazeborn nether spawn data wrapper.
     *
     * @param playerUUID the player uuid
     * @param firstTime  the first time
     */
    public BlazebornNetherSpawnDataWrapper(UUID playerUUID, boolean firstTime) {
        this.playerUUID = playerUUID;
        this.firstTime = firstTime;
    }

    /**
     * Gets player uuid.
     *
     * @return the player uuid
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Sets player uuid.
     *
     * @param playerUUID the player uuid
     */
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    /**
     * Is first time boolean.
     *
     * @return the boolean
     */
    public boolean isFirstTime() {
        return firstTime;
    }

    /**
     * Sets first time.
     *
     * @param firstTime the first time
     */
    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
}
