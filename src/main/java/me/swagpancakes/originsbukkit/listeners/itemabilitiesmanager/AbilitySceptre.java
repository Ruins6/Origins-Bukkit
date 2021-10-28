package me.swagpancakes.originsbukkit.listeners.itemabilitiesmanager;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.items.ItemManager;
import me.swagpancakes.originsbukkit.listeners.origins.Enderian;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import me.swagpancakes.originsbukkit.util.StorageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * The type Ability sceptre.
 */
public class AbilitySceptre implements Listener {

    private static Main plugin;

    /**
     * Instantiates a new Ability sceptre.
     *
     * @param plugin the plugin
     */
    public AbilitySceptre(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        AbilitySceptre.plugin = plugin;
    }

    /**
     * On ability sceptre right click.
     *
     * @param event the event
     */
    @EventHandler
    public void onAbilitySceptreRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                if (Objects.equals(event.getItem().getItemMeta(), ItemManager.abilitySceptre.getItemMeta())) {
                    switch (Objects.requireNonNull(StorageUtils.getPlayerOrigin(playerUUID))) {
                        case HUMAN:
                            ChatUtils.sendPlayerMessage(player, "&bHuman :D");
                            break;
                        case ENDERIAN:
                            Enderian.enderianEnderPearlThrow(player);
                            break;
                        case MERLING:
                            break;
                        case PHANTOM:
                            break;
                        case ELYTRIAN:
                            break;
                        case BLAZEBORN:
                            break;
                        case AVIAN:
                            break;
                        case ARACHNID:
                            break;
                        case SHULK:
                            break;
                        case FELINE:
                            break;
                    }
                }
            }
        }
    }
}
