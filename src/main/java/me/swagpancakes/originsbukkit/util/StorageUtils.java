/*
 * Origins-Bukkit - Origins for Bukkit and forks of Bukkit.
 * Copyright (C) 2021 SwagPannekaker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit.util;

import com.google.gson.Gson;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.OriginChangeEvent;
import me.swagpancakes.originsbukkit.storage.ArachnidAbilityToggleData;
import me.swagpancakes.originsbukkit.storage.MerlingTimerSessionData;
import me.swagpancakes.originsbukkit.storage.OriginsPlayerData;
import me.swagpancakes.originsbukkit.storage.PhantomAbilityToggleData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;

/**
 * The type Storage utils.
 *
 * @author SwagPannekaker
 */
public class StorageUtils {

    private final OriginsBukkit plugin;
    private List<OriginsPlayerData> originsPlayerData = new ArrayList<>();
    private List<MerlingTimerSessionData> merlingTimerSessionData = new ArrayList<>();
    private Map<UUID, ItemStack[]> shulkPlayerStorageData = new HashMap<>();
    private List<ArachnidAbilityToggleData> arachnidAbilityToggleData = new ArrayList<>();
    private List<PhantomAbilityToggleData> phantomAbilityToggleData = new ArrayList<>();
    private boolean isOriginsPlayerDataLoaded = false;

    /**
     * Gets origins player data.
     *
     * @return the origins player data
     */
    public List<OriginsPlayerData> getOriginsPlayerData() {
        return originsPlayerData;
    }

    /**
     * Sets origins player data.
     *
     * @param originsPlayerData the origins player data
     */
    public void setOriginsPlayerData(List<OriginsPlayerData> originsPlayerData) {
        this.originsPlayerData = originsPlayerData;
    }

    /**
     * Gets merling timer session data.
     *
     * @return the merling timer session data
     */
    public List<MerlingTimerSessionData> getMerlingTimerSessionData() {
        return merlingTimerSessionData;
    }

    /**
     * Sets merling timer session data.
     *
     * @param merlingTimerSessionData the merling timer session data
     */
    public void setMerlingTimerSessionData(List<MerlingTimerSessionData> merlingTimerSessionData) {
        this.merlingTimerSessionData = merlingTimerSessionData;
    }

    /**
     * Gets shulk player storage data.
     *
     * @return the shulk player storage data
     */
    public Map<UUID, ItemStack[]> getShulkPlayerStorageData() {
        return shulkPlayerStorageData;
    }

    /**
     * Sets shulk player storage data.
     *
     * @param shulkPlayerStorageData the shulk player storage data
     */
    public void setShulkPlayerStorageData(Map<UUID, ItemStack[]> shulkPlayerStorageData) {
        this.shulkPlayerStorageData = shulkPlayerStorageData;
    }

    /**
     * Gets arachnid ability toggle data.
     *
     * @return the arachnid ability toggle data
     */
    public List<ArachnidAbilityToggleData> getArachnidAbilityToggleData() {
        return arachnidAbilityToggleData;
    }

    /**
     * Sets arachnid ability toggle data.
     *
     * @param arachnidAbilityToggleData the arachnid ability toggle data
     */
    public void setArachnidAbilityToggleData(List<ArachnidAbilityToggleData> arachnidAbilityToggleData) {
        this.arachnidAbilityToggleData = arachnidAbilityToggleData;
    }

    /**
     * Gets phantom ability toggle data.
     *
     * @return the phantom ability toggle data
     */
    public List<PhantomAbilityToggleData> getPhantomAbilityToggleData() {
        return phantomAbilityToggleData;
    }

    /**
     * Sets phantom ability toggle data.
     *
     * @param phantomAbilityToggleData the phantom ability toggle data
     */
    public void setPhantomAbilityToggleData(List<PhantomAbilityToggleData> phantomAbilityToggleData) {
        this.phantomAbilityToggleData = phantomAbilityToggleData;
    }

    /**
     * Is origins player data loaded boolean.
     *
     * @return the boolean
     */
    public boolean isOriginsPlayerDataLoaded() {
        return isOriginsPlayerDataLoaded;
    }

    /**
     * Sets origins player data loaded.
     *
     * @param originsPlayerDataLoaded the origins player data loaded
     */
    public void setOriginsPlayerDataLoaded(boolean originsPlayerDataLoaded) {
        isOriginsPlayerDataLoaded = originsPlayerDataLoaded;
    }

    /**
     * Load data files.
     */
    public void loadDataFiles() {
        try {
            loadOriginsPlayerData();
            loadMerlingTimerSessionData();
            loadArachnidAbilityToggleData();
            loadPhantomAbilityToggleData();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Instantiates a new Storage utils.
     *
     * @param plugin the plugin
     */
    public StorageUtils(OriginsBukkit plugin) {
        this.plugin = plugin;
    }

    /**
     * Create origins player data.
     *
     * @param playerUUID the player uuid
     * @param player     the player
     * @param origin     the origin
     */
    public void createOriginsPlayerData(UUID playerUUID, Player player, String origin) {
        String playerName = player.getName();

        if (findOriginsPlayerData(playerUUID) == null) {
            OriginsPlayerData originsPlayerData = new OriginsPlayerData(playerUUID, playerName, origin);
            String newOrigin = originsPlayerData.getOrigin();

            this.originsPlayerData.add(originsPlayerData);
            OriginChangeEvent originChangeEvent = new OriginChangeEvent(player, null, newOrigin);
            Bukkit.getPluginManager().callEvent(originChangeEvent);
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
        for (OriginsPlayerData originsPlayerData : originsPlayerData) {
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
    public String getPlayerOrigin(UUID playerUUID) {
        for (OriginsPlayerData originsPlayerData : originsPlayerData) {
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
        Player player = Bukkit.getPlayer(playerUUID);

        if (findOriginsPlayerData(playerUUID) != null) {
            for (OriginsPlayerData originsPlayerData : originsPlayerData) {
                if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                    String oldOrigin = originsPlayerData.getOrigin();

                    this.originsPlayerData.remove(originsPlayerData);
                    OriginChangeEvent originChangeEvent = new OriginChangeEvent(player, oldOrigin, null);
                    Bukkit.getPluginManager().callEvent(originChangeEvent);
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
        Player player = Bukkit.getPlayer(playerUUID);

        if (findOriginsPlayerData(playerUUID) != null) {
            for (OriginsPlayerData originsPlayerData : originsPlayerData) {
                if (originsPlayerData.getPlayerUUID().equals(playerUUID)) {
                    String oldOrigin = originsPlayerData.getOrigin();

                    originsPlayerData.setPlayerName(newOriginsPlayerData.getPlayerName());
                    originsPlayerData.setOrigin(newOriginsPlayerData.getOrigin());
                    OriginChangeEvent originChangeEvent = new OriginChangeEvent(player, oldOrigin, newOriginsPlayerData.getOrigin());
                    Bukkit.getPluginManager().callEvent(originChangeEvent);
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
     * Save origins player data.
     *
     * @throws IOException the io exception
     */
    public void saveOriginsPlayerData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "playerorigindata.json");

                if (file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(originsPlayerData, writer);
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
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "playerorigindata.json");

                if (file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    ChatUtils.sendConsoleMessage("&3[Origins-Bukkit] Loading player data...");

                    try {
                        Reader reader = new FileReader(file);
                        OriginsPlayerData[] n = gson.fromJson(reader, OriginsPlayerData[].class);
                        originsPlayerData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                    setOriginsPlayerDataLoaded(true);
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
        merlingTimerSessionData.add(new MerlingTimerSessionData(playerUUID, timeLeft));
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
        for (MerlingTimerSessionData merlingTimerSessionData : merlingTimerSessionData) {
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
        for (MerlingTimerSessionData merlingTimerSessionData : merlingTimerSessionData) {
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
            for (MerlingTimerSessionData merlingTimerSessionData : merlingTimerSessionData) {
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
            for (MerlingTimerSessionData merlingTimerSessionData : merlingTimerSessionData) {
                if (merlingTimerSessionData.getPlayerUUID().equals(playerUUID)) {
                    this.merlingTimerSessionData.remove(merlingTimerSessionData);
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
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "merlingdata" + s + "merlingtimersessiondata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(merlingTimerSessionData, writer);
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
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "merlingdata" + s + "merlingtimersessiondata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    try {
                        Reader reader = new FileReader(file);
                        MerlingTimerSessionData[] n = gson.fromJson(reader, MerlingTimerSessionData[].class);
                        merlingTimerSessionData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Save shulk player storage data.
     *
     * @param playerUUID the player uuid
     *
     * @throws IOException the io exception
     */
    public void saveShulkPlayerStorageData(UUID playerUUID) throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                String s = File.separator;
                File shulkPlayerStorageDataFile = new File(plugin.getDataFolder(), s + "cache" + s + "shulkdata" + s + "inventoriesdata" + s + playerUUID + ".yml");

                if (!shulkPlayerStorageDataFile.getParentFile().exists()) {
                    shulkPlayerStorageDataFile.getParentFile().mkdirs();
                }
                if (!shulkPlayerStorageDataFile.exists()) {
                    try {
                        shulkPlayerStorageDataFile.createNewFile();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                FileConfiguration shulkPlayerStorageData = YamlConfiguration.loadConfiguration(shulkPlayerStorageDataFile);

                for (Map.Entry<UUID, ItemStack[]> entry : plugin.getStorageUtils().shulkPlayerStorageData.entrySet()) {
                    if (entry.getKey().equals(playerUUID)) {
                        shulkPlayerStorageData.set("data." + entry.getKey(), entry.getValue());
                    }
                }
                try {
                    shulkPlayerStorageData.save(shulkPlayerStorageDataFile);
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Load shulk player storage data.
     *
     * @param playerUUID the player uuid
     *
     * @throws IOException the io exception
     */
    public void loadShulkPlayerStorageData(UUID playerUUID) throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                String s = File.separator;
                File shulkPlayerStorageDataFile = new File(plugin.getDataFolder(), s + "cache" + s + "shulkdata" + s + "inventoriesdata" + s + playerUUID + ".yml");

                if (!shulkPlayerStorageDataFile.getParentFile().exists()) {
                    shulkPlayerStorageDataFile.getParentFile().mkdirs();
                }
                if (!shulkPlayerStorageDataFile.exists()) {
                    try {
                        shulkPlayerStorageDataFile.createNewFile();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                FileConfiguration shulkPlayerStorageData = YamlConfiguration.loadConfiguration(shulkPlayerStorageDataFile);
                if (shulkPlayerStorageData.contains("data")) {
                    shulkPlayerStorageData.getConfigurationSection("data").getKeys(false).forEach(key ->{
                        @SuppressWarnings("unchecked")
                        ItemStack[] contents = ((List<ItemStack>) shulkPlayerStorageData.get("data." + key)).toArray(new ItemStack[0]);
                        plugin.getStorageUtils().shulkPlayerStorageData.put(UUID.fromString(key), contents);
                    });
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Create arachnid ability toggle data.
     *
     * @param playerUUID the player uuid
     * @param isToggled  the is toggled
     */
    public void createArachnidAbilityToggleData(UUID playerUUID, boolean isToggled) {
        arachnidAbilityToggleData.add(new ArachnidAbilityToggleData(playerUUID, isToggled));
        try {
            saveArachnidAbilityToggleData();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Find arachnid ability toggle data arachnid ability toggle data.
     *
     * @param playerUUID the player uuid
     *
     * @return the arachnid ability toggle data
     */
    public ArachnidAbilityToggleData findArachnidAbilityToggleData(UUID playerUUID) {
        for (ArachnidAbilityToggleData arachnidAbilityToggleData : arachnidAbilityToggleData) {
            if (arachnidAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                return arachnidAbilityToggleData;
            }
        }
        return null;
    }

    /**
     * Gets arachnid ability toggle data.
     *
     * @param playerUUID the player uuid
     *
     * @return the arachnid ability toggle data
     */
    public boolean getArachnidAbilityToggleData(UUID playerUUID) {
        for (ArachnidAbilityToggleData arachnidAbilityToggleData : arachnidAbilityToggleData) {
            if (arachnidAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                return arachnidAbilityToggleData.isToggled();
            }
        }
        return false;
    }

    /**
     * Update arachnid ability toggle data.
     *
     * @param playerUUID                   the player uuid
     * @param newArachnidAbilityToggleData the new arachnid ability toggle data
     */
    public void updateArachnidAbilityToggleData(UUID playerUUID, ArachnidAbilityToggleData newArachnidAbilityToggleData) {
        if (findArachnidAbilityToggleData(playerUUID) != null) {
            for (ArachnidAbilityToggleData arachnidAbilityToggleData : arachnidAbilityToggleData) {
                if (arachnidAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                    arachnidAbilityToggleData.setToggled(newArachnidAbilityToggleData.isToggled());
                    try {
                        saveArachnidAbilityToggleData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Delete arachnid ability toggle data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteArachnidAbilityToggleData(UUID playerUUID) {
        if (findArachnidAbilityToggleData(playerUUID) != null) {
            for (ArachnidAbilityToggleData arachnidAbilityToggleData : arachnidAbilityToggleData) {
                if (arachnidAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                    this.arachnidAbilityToggleData.remove(arachnidAbilityToggleData);
                    break;
                }
            }
            try {
                saveArachnidAbilityToggleData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Save arachnid ability toggle data.
     *
     * @throws IOException the io exception
     */
    public void saveArachnidAbilityToggleData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "arachniddata" + s + "arachnidabilitytoggledata" + s + "arachnidabilitytoggledata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(arachnidAbilityToggleData, writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Load arachnid ability toggle data.
     *
     * @throws IOException the io exception
     */
    public void loadArachnidAbilityToggleData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "arachniddata" + s + "arachnidabilitytoggledata" + s + "arachnidabilitytoggledata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    try {
                        Reader reader = new FileReader(file);
                        ArachnidAbilityToggleData[] n = gson.fromJson(reader, ArachnidAbilityToggleData[].class);
                        arachnidAbilityToggleData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Create phantom ability toggle data.
     *
     * @param playerUUID the player uuid
     * @param isToggled  the is toggled
     */
    public void createPhantomAbilityToggleData(UUID playerUUID, boolean isToggled) {
        phantomAbilityToggleData.add(new PhantomAbilityToggleData(playerUUID, isToggled));
        try {
            savePhantomAbilityToggleData();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Find phantom ability toggle data phantom ability toggle data.
     *
     * @param playerUUID the player uuid
     *
     * @return the phantom ability toggle data
     */
    public PhantomAbilityToggleData findPhantomAbilityToggleData(UUID playerUUID) {
        for (PhantomAbilityToggleData phantomAbilityToggleData : phantomAbilityToggleData) {
            if (phantomAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                return phantomAbilityToggleData;
            }
        }
        return null;
    }

    /**
     * Gets phantom ability toggle data.
     *
     * @param playerUUID the player uuid
     *
     * @return the phantom ability toggle data
     */
    public boolean getPhantomAbilityToggleData(UUID playerUUID) {
        for (PhantomAbilityToggleData phantomAbilityToggleData : phantomAbilityToggleData) {
            if (phantomAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                return phantomAbilityToggleData.isToggled();
            }
        }
        return false;
    }

    /**
     * Update phantom ability toggle data.
     *
     * @param playerUUID                  the player uuid
     * @param newPhantomAbilityToggleData the new phantom ability toggle data
     */
    public void updatePhantomAbilityToggleData(UUID playerUUID, PhantomAbilityToggleData newPhantomAbilityToggleData) {
        if (findPhantomAbilityToggleData(playerUUID) != null) {
            for (PhantomAbilityToggleData phantomAbilityToggleData : phantomAbilityToggleData) {
                if (phantomAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                    phantomAbilityToggleData.setToggled(newPhantomAbilityToggleData.isToggled());
                    try {
                        savePhantomAbilityToggleData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Delete phantom ability toggle data.
     *
     * @param playerUUID the player uuid
     */
    public void deletePhantomAbilityToggleData(UUID playerUUID) {
        if (findPhantomAbilityToggleData(playerUUID) != null) {
            for (PhantomAbilityToggleData phantomAbilityToggleData : phantomAbilityToggleData) {
                if (phantomAbilityToggleData.getPlayerUUID().equals(playerUUID)) {
                    this.phantomAbilityToggleData.remove(phantomAbilityToggleData);
                    break;
                }
            }
            try {
                savePhantomAbilityToggleData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Save phantom ability toggle data.
     *
     * @throws IOException the io exception
     */
    public void savePhantomAbilityToggleData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "phantomdata" + s + "phantomabilitytoggledata" + s + "phantomabilitytoggledata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(phantomAbilityToggleData, writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Load phantom ability toggle data.
     *
     * @throws IOException the io exception
     */
    public void loadPhantomAbilityToggleData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(plugin.getDataFolder().getAbsolutePath() + s + "cache" + s + "phantomdata" + s + "phantomabilitytoggledata" + s + "phantomabilitytoggledata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    try {
                        Reader reader = new FileReader(file);
                        PhantomAbilityToggleData[] n = gson.fromJson(reader, PhantomAbilityToggleData[].class);
                        phantomAbilityToggleData = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
