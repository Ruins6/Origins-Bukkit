package me.lemonypancakes.originsbukkit.storage.data;

import com.google.gson.Gson;
import me.lemonypancakes.originsbukkit.storage.StorageHandler;
import me.lemonypancakes.originsbukkit.storage.wrappers.ElytrianClaustrophobiaTimerDataWrapper;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Elytrian claustrophobia timer data.
 *
 * @author SwagPannekaker
 */
public class ElytrianClaustrophobiaTimerData {

    private final StorageHandler storageHandler;
    private List<ElytrianClaustrophobiaTimerDataWrapper> elytrianClaustrophobiaTimerDataWrappers = new ArrayList<>();

    /**
     * Gets storage handler.
     *
     * @return the storage handler
     */
    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    /**
     * Gets elytrian claustrophobia timer data.
     *
     * @return the elytrian claustrophobia timer data
     */
    public List<ElytrianClaustrophobiaTimerDataWrapper> getElytrianClaustrophobiaTimerData() {
        return elytrianClaustrophobiaTimerDataWrappers;
    }

    /**
     * Sets merling timer session data.
     *
     * @param elytrianClaustrophobiaTimerDataWrappers the elytrian claustrophobia timer data wrappers
     */
    public void setMerlingTimerSessionData(List<ElytrianClaustrophobiaTimerDataWrapper> elytrianClaustrophobiaTimerDataWrappers) {
        this.elytrianClaustrophobiaTimerDataWrappers = elytrianClaustrophobiaTimerDataWrappers;
    }

    /**
     * Instantiates a new Elytrian claustrophobia timer data.
     *
     * @param storageHandler the storage handler
     */
    public ElytrianClaustrophobiaTimerData(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
        init();
    }

    /**
     * Init.
     */
    private void init() {
        try {
            loadElytrianClaustrophobiaTimerData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create elytrian claustrophobia timer data.
     *
     * @param playerUUID             the player uuid
     * @param timerTimeLeft          the timer time left
     * @param claustrophobiaTimeLeft the claustrophobia time left
     */
    public void createElytrianClaustrophobiaTimerData(UUID playerUUID, int timerTimeLeft, int claustrophobiaTimeLeft) {
        if (findElytrianClaustrophobiaTimerData(playerUUID) == null) {
            ElytrianClaustrophobiaTimerDataWrapper elytrianClaustrophobiaTimerDataWrapper = new ElytrianClaustrophobiaTimerDataWrapper(playerUUID, timerTimeLeft, claustrophobiaTimeLeft);
            getElytrianClaustrophobiaTimerData().add(elytrianClaustrophobiaTimerDataWrapper);
            try {
                saveElytrianClaustrophobiaTimerData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Find elytrian claustrophobia timer data elytrian claustrophobia timer data wrapper.
     *
     * @param playerUUID the player uuid
     *
     * @return the elytrian claustrophobia timer data wrapper
     */
    public ElytrianClaustrophobiaTimerDataWrapper findElytrianClaustrophobiaTimerData(UUID playerUUID) {
        for (ElytrianClaustrophobiaTimerDataWrapper elytrianClaustrophobiaTimerDataWrapper : getElytrianClaustrophobiaTimerData()) {
            if (elytrianClaustrophobiaTimerDataWrapper.getPlayerUUID().equals(playerUUID)) {
                return elytrianClaustrophobiaTimerDataWrapper;
            }
        }
        return null;
    }

    /**
     * Gets elytrian claustrophobia timer data.
     *
     * @param playerUUID the player uuid
     *
     * @return the elytrian claustrophobia timer data
     */
    public int getElytrianClaustrophobiaTimerData(UUID playerUUID) {
        for (ElytrianClaustrophobiaTimerDataWrapper elytrianClaustrophobiaTimerDataWrapper : getElytrianClaustrophobiaTimerData()) {
            if (elytrianClaustrophobiaTimerDataWrapper.getPlayerUUID().equals(playerUUID)) {
                return elytrianClaustrophobiaTimerDataWrapper.getTimerTimeLeft();
            }
        }
        return 0;
    }

    /**
     * Update elytrian claustrophobia timer data.
     *
     * @param playerUUID                                the player uuid
     * @param newElytrianClaustrophobiaTimerDataWrapper the new elytrian claustrophobia timer data wrapper
     */
    public void updateElytrianClaustrophobiaTimerData(UUID playerUUID, ElytrianClaustrophobiaTimerDataWrapper newElytrianClaustrophobiaTimerDataWrapper) {
        if (findElytrianClaustrophobiaTimerData(playerUUID) != null) {
            for (ElytrianClaustrophobiaTimerDataWrapper elytrianClaustrophobiaTimerDataWrapper : getElytrianClaustrophobiaTimerData()) {
                if (elytrianClaustrophobiaTimerDataWrapper.getPlayerUUID().equals(playerUUID)) {
                    elytrianClaustrophobiaTimerDataWrapper.setTimerTimeLeft(newElytrianClaustrophobiaTimerDataWrapper.getTimerTimeLeft());
                    elytrianClaustrophobiaTimerDataWrapper.setClaustrophobiaTimeLeft(newElytrianClaustrophobiaTimerDataWrapper.getClaustrophobiaTimeLeft());
                    try {
                        saveElytrianClaustrophobiaTimerData();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Delete elytrian claustrophobia timer data.
     *
     * @param playerUUID the player uuid
     */
    public void deleteElytrianClaustrophobiaTimerData(UUID playerUUID) {
        if (findElytrianClaustrophobiaTimerData(playerUUID) != null) {
            for (ElytrianClaustrophobiaTimerDataWrapper elytrianClaustrophobiaTimerDataWrapper : getElytrianClaustrophobiaTimerData()) {
                if (elytrianClaustrophobiaTimerDataWrapper.getPlayerUUID().equals(playerUUID)) {
                    getElytrianClaustrophobiaTimerData().remove(elytrianClaustrophobiaTimerDataWrapper);
                    break;
                }
            }
            try {
                saveElytrianClaustrophobiaTimerData();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Save elytrian claustrophobia timer data.
     *
     * @throws IOException the io exception
     */
    public void saveElytrianClaustrophobiaTimerData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(getStorageHandler().getPlugin().getDataFolder().getAbsolutePath() + s + "cache" + s + "elytriandata" + s + "elytrianclaustrophobiatimerdata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Writer writer = new FileWriter(file, false);
                    gson.toJson(getElytrianClaustrophobiaTimerData(), writer);
                    writer.flush();
                    writer.close();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }.runTaskAsynchronously(getStorageHandler().getPlugin());
    }

    /**
     * Load elytrian claustrophobia timer data.
     *
     * @throws IOException the io exception
     */
    public void loadElytrianClaustrophobiaTimerData() throws IOException {

        new BukkitRunnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                String s = File.separator;
                File file = new File(getStorageHandler().getPlugin().getDataFolder().getAbsolutePath() + s + "cache" + s + "elytriandata" + s + "elytrianclaustrophobiatimerdata.json");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    try {
                        Reader reader = new FileReader(file);
                        ElytrianClaustrophobiaTimerDataWrapper[] n = gson.fromJson(reader, ElytrianClaustrophobiaTimerDataWrapper[].class);
                        elytrianClaustrophobiaTimerDataWrappers = new ArrayList<>(Arrays.asList(n));
                    } catch (FileNotFoundException event) {
                        event.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(getStorageHandler().getPlugin());
    }
}
