package me.swagpancakes.originsbukkit.util;

import com.google.gson.Gson;
import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.storage.MerlingTimerSessionData;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Storage utils.
 */
public class StorageUtils {

    private final Main plugin;
    private List<OriginsPlayerData> originsPlayerData = new ArrayList<>();
    private List<MerlingTimerSessionData> merlingTimerSessionData = new ArrayList<>();
    /**
     * The Is origins player data loaded.
     */
    public boolean isOriginsPlayerDataLoaded = false;

    /**
     * Sets origins player data loaded.
     *
     * @param originsPlayerDataLoaded the origins player data loaded
     */
    public void setOriginsPlayerDataLoaded(boolean originsPlayerDataLoaded) {
        isOriginsPlayerDataLoaded = originsPlayerDataLoaded;
    }

    /**
     * Instantiates a new Storage utils.
     *
     * @param plugin the plugin
     */
    public StorageUtils(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Create origins player data.
     *
     * @param playerUUID the player uuid
     * @param player     the player
     * @param origin     the origin
     */
    public void createOriginsPlayerData(UUID playerUUID, Player player, Origins origin) {
        String playerName = player.getName();

        if (findOriginsPlayerData(playerUUID) == null) {
            OriginsPlayerData originsPlayerData = new OriginsPlayerData(playerUUID, playerName, origin);
            this.originsPlayerData.add(originsPlayerData);
            try {
                saveOriginsPlayerData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Find origins player data origins player data.
     *
     * @param playerUUID the player uuid
     *
     * @return the origins player data
     */
    public OriginsPlayerData findOriginsPlayerData(UUID playerUUID) {
        for (OriginsPlayerData originsPlayerData : this.originsPlayerData) {
            if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                return originsPlayerData;
            }
        }
        return null;
    }

    /**
     * Gets player origin.
     *
     * @param playerUUID the player uuid
     *
     * @return the player origin
     */
    public Origins getPlayerOrigin(UUID playerUUID) {
        for (OriginsPlayerData originsPlayerData : this.originsPlayerData) {
            if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                return originsPlayerData.getOrigin();
            }
        }
        return null;
    }

    /**
     * Delete origins player data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteOriginsPlayerData(UUID playerUUID) {
        if (findOriginsPlayerData(playerUUID) != null) {
            for (OriginsPlayerData originsPlayerData : plugin.storageUtils.originsPlayerData) {
                if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                    plugin.storageUtils.originsPlayerData.remove(originsPlayerData);
                    break;
                }
            }
            try {
                saveOriginsPlayerData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Update origins player data.
     *
     * @param playerUUID           the player uuid
     * @param newOriginsPlayerData the new origins player data
     */
    public void updateOriginsPlayerData(UUID playerUUID, OriginsPlayerData newOriginsPlayerData) {
        if (findOriginsPlayerData(playerUUID) != null) {
            for (OriginsPlayerData originsPlayerData : this.originsPlayerData) {
                if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                    originsPlayerData.setPlayerName(newOriginsPlayerData.getPlayerName());
                    originsPlayerData.setOrigin(newOriginsPlayerData.getOrigin());
                    try {
                        saveOriginsPlayerData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Find all origin player data list.
     *
     * @return the list
     */
    public List<OriginsPlayerData> findAllOriginPlayerData() {
        return this.originsPlayerData;
    }

    /**
     * Save origins player data.
     *
     * @throws IOException the io exception
     */
    public void saveOriginsPlayerData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata.json");

                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(plugin.storageUtils.originsPlayerData, writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Load origins player data.
     *
     * @throws IOException the io exception
     */
    public void loadOriginsPlayerData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata.json");

                if (file.exists()) {
                    ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] Loading player data...");

                    try {
                        Reader reader = new FileReader(file);
                        OriginsPlayerData[] n = gson.fromJson(reader, OriginsPlayerData[].class);
                        plugin.storageUtils.originsPlayerData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                    plugin.storageUtils.setOriginsPlayerDataLoaded(true);
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Player data loaded.");
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Create merling timer session data.
     *
     * @param playerUUID the player uuid
     * @param timeLeft   the time left
     */
    public void createMerlingTimerSessionData(UUID playerUUID, int timeLeft) {
        this.merlingTimerSessionData.add(new MerlingTimerSessionData(playerUUID, timeLeft));
        try {
            saveMerlingTimerSessionData();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Find merling timer session data merling timer session data.
     *
     * @param playerUUID the player uuid
     *
     * @return the merling timer session data
     */
    public MerlingTimerSessionData findMerlingTimerSessionData(UUID playerUUID) {
        for (MerlingTimerSessionData merlingTimerSessionData : this.merlingTimerSessionData) {
            if (merlingTimerSessionData.getPlayerUUID().equals(playerUUID)) {
                return merlingTimerSessionData;
            }
        }
        return null;
    }

    /**
     * Gets merling timer session data time left.
     *
     * @param playerUUID the player uuid
     *
     * @return the merling timer session data time left
     */
    public int getMerlingTimerSessionDataTimeLeft(UUID playerUUID) {
        for (MerlingTimerSessionData merlingTimerSessionData : this.merlingTimerSessionData) {
            if (merlingTimerSessionData.getPlayerUUID().equals(playerUUID)) {
                return merlingTimerSessionData.getTimeLeft();
            }
        }
        return 0;
    }

    /**
     * Update merling timer session data.
     *
     * @param playerUUID                 the player uuid
     * @param newMerlingTimerSessionData the new merling timer session data
     */
    public void updateMerlingTimerSessionData(UUID playerUUID, MerlingTimerSessionData newMerlingTimerSessionData) {
        if (findMerlingTimerSessionData(playerUUID) != null) {
            for (MerlingTimerSessionData merlingTimerSessionData : this.merlingTimerSessionData) {
                if (merlingTimerSessionData.getPlayerUUID().equals(playerUUID)) {
                    merlingTimerSessionData.setTimeLeft(newMerlingTimerSessionData.getTimeLeft());
                    try {
                        saveMerlingTimerSessionData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Delete merling timer session data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteMerlingTimerSessionData(UUID playerUUID) {
        if (findMerlingTimerSessionData(playerUUID) != null) {
            for (MerlingTimerSessionData merlingTimerSessionData : plugin.storageUtils.merlingTimerSessionData) {
                if (merlingTimerSessionData.getPlayerUUID().equals(playerUUID)) {
                    plugin.storageUtils.merlingTimerSessionData.remove(merlingTimerSessionData);
                    break;
                }
            }
            try {
                saveMerlingTimerSessionData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Save merling timer session data.
     *
     * @throws IOException the io exception
     */
    public void saveMerlingTimerSessionData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                Path cache = Paths.get(plugin.getDataFolder().getAbsolutePath() + File.separator + "cache");
                File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "cache" + File.separator + "merlingtimer-session.json");

                if (!Files.exists(cache)) {
                    try {
                        Files.createDirectories(cache);
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(plugin.storageUtils.merlingTimerSessionData, writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Load merling timer session data.
     *
     * @throws IOException the io exception
     */
    public void loadMerlingTimerSessionData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                Path cache = Paths.get(plugin.getDataFolder().getAbsolutePath() + File.separator + "cache");
                File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "cache" + File.separator + "merlingtimer-session.json");

                if (!Files.exists(cache)) {
                    try {
                        Files.createDirectories(cache);
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                if (file.exists()) {
                    ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] Loading sessions...");

                    try {
                        Reader reader = new FileReader(file);
                        MerlingTimerSessionData[] n = gson.fromJson(reader, MerlingTimerSessionData[].class);
                        plugin.storageUtils.merlingTimerSessionData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                    ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Sessions loaded.");
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
