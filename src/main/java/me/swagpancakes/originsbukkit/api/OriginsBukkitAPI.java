package me.swagpancakes.originsbukkit.api;

import com.sun.istack.internal.NotNull;
import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The type Origins bukkit api.
 */
public class OriginsBukkitAPI {

    private final Main plugin;

    /**
     * Instantiates a new Origins bukkit api.
     *
     * @param plugin the plugin
     */
    public OriginsBukkitAPI(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets player origin.
     *
     * @param playerUUID the player uuid
     *
     * @return the player origin
     */
    public Origins getPlayerOrigin(@NotNull UUID playerUUID) {
        return plugin.storageUtils.getPlayerOrigin(playerUUID);
    }

    /**
     * Create origins player data.
     *
     * @param playerUUID the player uuid
     * @param player     the player
     * @param origin     the origin
     */
    public void createOriginsPlayerData(@NotNull UUID playerUUID, @NotNull Player player, @NotNull Origins origin) {
        plugin.storageUtils.createOriginsPlayerData(playerUUID, player, origin);
    }

    /**
     * Find origins player data origins player data.
     *
     * @param playerUUID the player uuid
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData(@NotNull UUID playerUUID) {
        return plugin.storageUtils.findOriginsPlayerData(playerUUID);
    }

    /**
     * Delete origins player data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteOriginsPlayerData(@NotNull UUID playerUUID) {
        plugin.storageUtils.deleteOriginsPlayerData(playerUUID);
    }

    /**
     * Update origins player data.
     *
     * @param playerUUID           the player uuid
     * @param newOriginsPlayerData the new origins player data
     */
    public void updateOriginsPlayerData(@NotNull UUID playerUUID, @NotNull OriginsPlayerData newOriginsPlayerData) {
        plugin.storageUtils.updateOriginsPlayerData(playerUUID, newOriginsPlayerData);
    }
}
