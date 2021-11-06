package me.swagpancakes.originsbukkit.storage;

import me.swagpancakes.originsbukkit.enums.Origins;

import java.util.UUID;

/**
 * The type Origins player data.
 */
public class OriginsPlayerData {

    private UUID playerUUID;
    private String playerName;
    private Origins origin;

    /**
     * Instantiates a new Origins player data.
     *
     * @param playerUUID the player uuid
     * @param playerName the player name
     * @param origin     the origin
     */
    public OriginsPlayerData(UUID playerUUID, String playerName, Origins origin) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.origin = origin;
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
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets player name.
     *
     * @param playerName the player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public Origins getOrigin() {
        return origin;
    }

    /**
     * Sets origin.
     *
     * @param origin the origin
     */
    public void setOrigin(Origins origin) {
        this.origin = origin;
    }
}
