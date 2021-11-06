package me.swagpancakes.originsbukkit.storage;

import java.util.UUID;

/**
 * The type Merling timer session data.
 */
public class MerlingTimerSessionData {

    private UUID playerUUID;
    private int timeLeft;

    /**
     * Instantiates a new Merling timer session data.
     *
     * @param playerUUID the player uuid
     * @param timeLeft   the time left
     */
    public MerlingTimerSessionData(UUID playerUUID, int timeLeft) {
        this.playerUUID = playerUUID;
        this.timeLeft = timeLeft;
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
     * Gets time left.
     *
     * @return the time left
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Sets time left.
     *
     * @param timeLeft the time left
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
