package me.lemonypancakes.originsbukkit.storage.data;

import com.google.gson.Gson;
import me.lemonypancakes.originsbukkit.storage.StorageHandler;
import me.lemonypancakes.originsbukkit.storage.wrappers.BlazebornNetherSpawnDataWrapper;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Blazeborn nether spawn data.
 *
 * @author SwagPannekaker
 */
public class BlazebornNetherSpawnData {

    private final StorageHandler storageHandler;
    private List<BlazebornNetherSpawnDataWrapper> blazebornNetherSpawnDataWrappers = new ArrayList<>();

    /**
     * Gets storage handler.
     *
     * @return the storage handler
     */
    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    /**
     * Gets blazeborn nether spawn data wrappers.
     *
     * @return the blazeborn nether spawn data wrappers
     */
    public List<BlazebornNetherSpawnDataWrapper> getBlazebornNetherSpawnDataWrappers() {
        return blazebornNetherSpawnDataWrappers;
    }

    /**
     * Sets blazeborn nether spawn data wrappers.
     *
     * @param blazebornNetherSpawnDataWrappers the blazeborn nether spawn data wrappers
     */
    public void setBlazebornNetherSpawnDataWrappers(List<BlazebornNetherSpawnDataWrapper> blazebornNetherSpawnDataWrappers) {
        this.blazebornNetherSpawnDataWrappers = blazebornNetherSpawnDataWrappers;
    }

    /**
     * Instantiates a new Blazeborn nether spawn data.
     *
     * @param storageHandler the storage handler
     */
    public BlazebornNetherSpawnData(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        try {
            loadBlazebornNetherSpawnData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create blazeborn nether spawn data.
     *
     * @param playerUUID the player uuid
     * @param firstTime  the first time
     */
    public void createBlazebornNetherSpawnData(UUID playerUUID, boolean firstTime) {
        if (findBlazebornNetherSpawnData(playerUUID) == null) {
            BlazebornNetherSpawnDataWrapper blazebornNetherSpawnDataWrapper = new BlazebornNetherSpawnDataWrapper(playerUUID, firstTime);
            getBlazebornNetherSpawnDataWrappers().add(blazebornNetherSpawnDataWrapper);
            try {
                saveBlazebornNetherSpawnData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Find blazeborn nether spawn data blazeborn nether spawn data wrapper.
     *
     * @param playerUUID the player uuid
     *
     * @return the blazeborn nether spawn data wrapper
     */
    public BlazebornNetherSpawnDataWrapper findBlazebornNetherSpawnData(UUID playerUUID) {
        for (BlazebornNetherSpawnDataWrapper blazebornNetherSpawnDataWrapper : getBlazebornNetherSpawnDataWrappers()) {
            if (blazebornNetherSpawnDataWrapper.getPlayerUUID().equals(playerUUID)) {
                return blazebornNetherSpawnDataWrapper;
            }
        }
        return null;
    }

    /**
     * Gets blazeborn nether spawn data.
     *
     * @param playerUUID the player uuid
     *
     * @return the blazeborn nether spawn data
     */
    public boolean getBlazebornNetherSpawnData(UUID playerUUID) {
        for (BlazebornNetherSpawnDataWrapper blazebornNetherSpawnDataWrapper : getBlazebornNetherSpawnDataWrappers()) {
            if (blazebornNetherSpawnDataWrapper.getPlayerUUID().equals(playerUUID)) {
                return blazebornNetherSpawnDataWrapper.isFirstTime();
            }
        }
        return false;
    }

    /**
     * Update blazeborn nether spawn data.
     *
     * @param playerUUID                         the player uuid
     * @param newBlazebornNetherSpawnDataWrapper the new blazeborn nether spawn data wrapper
     */
    public void updateBlazebornNetherSpawnData(UUID playerUUID, BlazebornNetherSpawnDataWrapper newBlazebornNetherSpawnDataWrapper) {
        if (findBlazebornNetherSpawnData(playerUUID) != null) {
            for (BlazebornNetherSpawnDataWrapper blazebornNetherSpawnDataWrapper : getBlazebornNetherSpawnDataWrappers()) {
                if (blazebornNetherSpawnDataWrapper.getPlayerUUID().equals(playerUUID)) {
                    blazebornNetherSpawnDataWrapper.setFirstTime(newBlazebornNetherSpawnDataWrapper.isFirstTime());
                    try {
                        saveBlazebornNetherSpawnData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Delete blazeborn nether spawn data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteBlazebornNetherSpawnData(UUID playerUUID) {
        if (findBlazebornNetherSpawnData(playerUUID) != null) {
            for (BlazebornNetherSpawnDataWrapper blazebornNetherSpawnDataWrapper : getBlazebornNetherSpawnDataWrappers()) {
                if (blazebornNetherSpawnDataWrapper.getPlayerUUID().equals(playerUUID)) {
                    getBlazebornNetherSpawnDataWrappers().remove(blazebornNetherSpawnDataWrapper);
                    break;
                }
            }
            try {
                saveBlazebornNetherSpawnData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Save blazeborn nether spawn data.
     *
     * @throws IOException the io exception
     */
    public void saveBlazebornNetherSpawnData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(getStorageHandler().getPlugin().getDataFolder().getAbsolutePath() + s + "cache" + s + "blazeborndata" + s + "blazebornnetherspawndata" + s + "blazebornnetherspawndata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(getBlazebornNetherSpawnDataWrappers(), writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(getStorageHandler().getPlugin());
    }

    /**
     * Load blazeborn nether spawn data.
     *
     * @throws IOException the io exception
     */
    public void loadBlazebornNetherSpawnData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(getStorageHandler().getPlugin().getDataFolder().getAbsolutePath() + s + "cache" + s + "blazeborndata" + s + "blazebornnetherspawndata" + s + "blazebornnetherspawndata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    try {
                        Reader reader = new FileReader(file);
                        BlazebornNetherSpawnDataWrapper[] n = gson.fromJson(reader, BlazebornNetherSpawnDataWrapper[].class);
                        blazebornNetherSpawnDataWrappers = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(getStorageHandler().getPlugin());
    }
}
