package me.swagpancakes.originsbukkit.listeners;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import me.swagpancakes.originsbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Air controller.
 */
public class AirController extends BukkitRunnable implements PacketListener {

    // our variables
    private final Main plugin;
    private final WrappedDataWatcherObject ADDRESS;
    private final Player target;
    private int airticks;

    /**
     * Instantiates a new Air controller.
     *
     * @param plugin the plugin
     * @param target the target
     */
    public AirController(Main plugin, Player target) {
        this.plugin = plugin;
        // set the target
        this.target = target;

        // this is our "address" where we will be writing
        ADDRESS = new WrappedDataWatcherObject(1, WrappedDataWatcher.Registry.get(Integer.class));

        // adds the listener to ProtocolLib
        ProtocolLibrary.getProtocolManager().addPacketListener(this);

        // schedules the task, async, each tick
        runTaskTimerAsynchronously(getPlugin(), 0, 1);
    }

    /**
     * Finalize.
     */
    @Override
    protected void finalize() {
        destroy();
    }

    /**
     * Destroy.
     */
    public void destroy() {
        // remove listener
        ProtocolLibrary.getProtocolManager().removePacketListener(this);

        // cancel event, if not cancelled already
        if(!isCancelled())
            cancel();
    }

    /**
     * Gets airticks.
     *
     * @return the airticks
     */
    public int getAirticks() {
        return airticks;
    }

    /**
     * Sets airticks.
     *
     * @param airticks the airticks
     */
    public void setAirticks(int airticks) {
        this.airticks = airticks;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Player getTarget() {
        return target;
    }

    /**
     * On packet sending.
     *
     * @param e the e
     */
    @Override
    public void onPacketSending(PacketEvent e) {
        // check player
        if (e.getPlayer() != target)
            return;

        // get wrapper that helps us get the data
        WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata(e.getPacket());

        // create the watcher that safely overrides the data
        WrappedDataWatcher watcher = new WrappedDataWatcher(wrapper.getMetadata());

        // override the desired address
        watcher.setObject(ADDRESS, airticks);

        // apply changes pack to the wrapper
        wrapper.setMetadata(watcher.getWatchableObjects());
    }

    /**
     * On packet receiving.
     *
     * @param e the e
     */
    @Override
    public void onPacketReceiving(PacketEvent e) {
        // unused
    }

    /**
     * Gets sending whitelist.
     *
     * @return the sending whitelist
     */
    @Override
    public ListeningWhitelist getSendingWhitelist() {
        // listens to entity metadata
        return ListeningWhitelist.newBuilder().types(Server.ENTITY_METADATA).build();
    }

    /**
     * Gets receiving whitelist.
     *
     * @return the receiving whitelist
     */
    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        // we won't be listening to packets from a client
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        // send a dummy packed
        // we will listen to this packet
        // and we will override it at "onPacketSending(PacketEvent)"
        WrapperPlayServerEntityMetadata w = new WrapperPlayServerEntityMetadata();
        w.setEntityID(target.getEntityId());
        w.sendPacket(target);

        // check the player's status
        if(target.isOnline()) return;

        // player is offline
        cancel();
        destroy();
    }
}
 