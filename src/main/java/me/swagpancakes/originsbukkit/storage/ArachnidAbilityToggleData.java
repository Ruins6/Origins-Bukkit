package me.swagpancakes.originsbukkit.storage;

import com.sun.istack.internal.NotNull;

import java.util.UUID;

/**
 * The type Arachnid ability toggle data.
 *
 * @author SwagPannekaker
 */
public class ArachnidAbilityToggleData {

    private UUID playerUUID;
    private boolean isToggled;

    /**
     * Instantiates a new Arachnid ability toggle data.
     *
     * @param playerUUID the player uuid
     * @param isToggled  the is toggled
     */
    public ArachnidAbilityToggleData(@NotNull UUID playerUUID, @NotNull boolean isToggled) {
        this.playerUUID = playerUUID;
        this.isToggled = isToggled;
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
     * Is toggled boolean.
     *
     * @return the boolean
     */
    public boolean isToggled() {
        return isToggled;
    }

    /**
     * Sets toggled.
     *
     * @param toggled the toggled
     */
    public void setToggled(boolean toggled) {
        isToggled = toggled;
    }
}
