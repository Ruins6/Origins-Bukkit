/*
 *     Origins-Bukkit
 *     Copyright (C) 2021 SwagPannekaker
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.swagpancakes.originsbukkit.listeners.origins;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.api.events.PlayerOriginInitiateEvent;
import me.swagpancakes.originsbukkit.enums.Config;
import me.swagpancakes.originsbukkit.enums.Lang;
import me.swagpancakes.originsbukkit.enums.Origins;
import me.swagpancakes.originsbukkit.util.Origin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Shulk.
 *
 * @author SwagPannekaker
 */
public class Shulk extends Origin implements Listener {

    private final OriginsBukkit plugin;

    /**
     * Instantiates a new Shulk.
     *
     * @param plugin the plugin
     */
    public Shulk(OriginsBukkit plugin) {
        super(Config.ORIGINS_SHULK_MAX_HEALTH.toDouble(), 0.2f, 0.1f);
        this.plugin = plugin;
        init();
    }

    /**
     * Gets origin identifier.
     *
     * @return the origin identifier
     */
    @Override
    public String getOriginIdentifier() {
        return "Shulk";
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    @Override
    public String getAuthor() {
        return "SwagPannekaker";
    }

    /**
     * Gets origin icon.
     *
     * @return the origin icon
     */
    @Override
    public Material getOriginIcon() {
        return Material.SHULKER_SHELL;
    }

    /**
     * Is origin icon glowing boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean isOriginIconGlowing() {
        return false;
    }

    /**
     * Gets origin title.
     *
     * @return the origin title
     */
    @Override
    public String getOriginTitle() {
        return Lang.SHULK_TITLE.toString();
    }

    /**
     * Get origin description string [ ].
     *
     * @return the string [ ]
     */
    @Override
    public String[] getOriginDescription() {
        return Lang.SHULK_DESCRIPTION.toStringList();
    }

    /**
     * Init.
     */
    private void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerOrigin(getOriginIdentifier());
        registerShulkBlockDiggingPacketListener();
    }

    /**
     * Shulk join.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkJoin(PlayerOriginInitiateEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = event.getOrigin();

        if (Objects.equals(origin, Origins.SHULK.toString())) {
            AttributeInstance genericArmorAttribute = player.getAttribute(Attribute.GENERIC_ARMOR);

            if (genericArmorAttribute != null) {
                double defaultBaseValue = genericArmorAttribute.getBaseValue();

                if (!(defaultBaseValue >= 8))
                genericArmorAttribute.setBaseValue(defaultBaseValue + 8);
            }
            player.setHealthScale(Config.ORIGINS_SHULK_MAX_HEALTH.toDouble());
            if (!plugin.storageUtils.getShulkPlayerStorageData().containsKey(playerUUID)) {
                try {
                    plugin.storageUtils.loadShulkPlayerStorageData(playerUUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Shulk shield disability.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkShieldDisability(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();
        World world = player.getWorld();
        Location location = player.getLocation();
        EquipmentSlot equipmentSlot = event.getHand();
        PlayerInventory playerInventory = player.getInventory();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK.toString())) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if (itemStack != null) {
                    Material material = itemStack.getType();

                    if (material == Material.SHIELD) {
                        if (equipmentSlot == EquipmentSlot.HAND) {
                            playerInventory.remove(itemStack);
                            world.dropItemNaturally(location, itemStack);
                        } else if (equipmentSlot == EquipmentSlot.OFF_HAND) {
                            playerInventory.setItemInOffHand(null);
                            world.dropItemNaturally(location, itemStack);
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Shulk fast exhaustion.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkFastExhaustion(FoodLevelChangeEvent event) {
        HumanEntity humanEntity = event.getEntity();
        Player player = (Player) humanEntity;
        UUID playerUUID = player.getUniqueId();

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK.toString())) {
            event.setFoodLevel(event.getFoodLevel() - 1);
        }
    }

    /**
     * Shulk inventory ability.
     *
     * @param player the player
     */
    public void shulkInventoryAbility(Player player) {
        UUID playerUUID = player.getUniqueId();
        Inventory inv = Bukkit.createInventory(player, InventoryType.DROPPER, player.getName() + "'s Vault");

        if (plugin.storageUtils.getShulkPlayerStorageData().containsKey(playerUUID)) {
            inv.setContents(plugin.storageUtils.getShulkPlayerStorageData().get(playerUUID));
        }
        player.openInventory(inv);
    }

    /**
     * On shulk custom inventory close.
     *
     * @param event the event
     */
    @EventHandler
    public void onShulkCustomInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getView().getTitle().contains(player.getName() + "'s Vault")) {
            plugin.storageUtils.getShulkPlayerStorageData().remove(playerUUID);
            plugin.storageUtils.getShulkPlayerStorageData().put(playerUUID, event.getInventory().getContents());
            try {
                plugin.storageUtils.saveShulkPlayerStorageData(playerUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shulk block break.
     *
     * @param event the event
     */
    @EventHandler
    public void shulkBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Block block = event.getBlock();
        Material material = block.getType();
        Material mainHandItemType = player.getInventory().getItemInMainHand().getType();
        List<Material> tools = Arrays.asList(
                Material.WOODEN_PICKAXE,
                Material.STONE_PICKAXE,
                Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_PICKAXE,
                Material.NETHERITE_PICKAXE);

        if (Objects.equals(plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK.toString())) {
            if (!tools.contains(mainHandItemType)) {
                if (material == Material.STONE || material == Material.NETHERRACK) {
                    block.breakNaturally();
                }
            }
        }
    }

    /**
     * Register shulk block digging packet listener.
     */
    public void registerShulkBlockDiggingPacketListener() {
        plugin.protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                BlockPosition blockPosition = packet.getBlockPositionModifier().read(0);
                int x = blockPosition.getX();
                int y = blockPosition.getY();
                int z = blockPosition.getZ();
                Player player = event.getPlayer();
                World world = player.getWorld();
                UUID playerUUID = player.getUniqueId();
                Location location = new Location(world, x, y, z);
                Block block = location.getBlock();
                Material material = block.getType();
                PlayerInventory playerInventory = player.getInventory();
                ItemStack itemStackMainHand = playerInventory.getItemInMainHand();
                Material mainHandItemType = itemStackMainHand.getType();
                List<Material> blocks = Arrays.asList(
                        Material.STONE,
                        Material.NETHERRACK);
                List<Material> tools = Arrays.asList(
                        Material.WOODEN_PICKAXE,
                        Material.STONE_PICKAXE,
                        Material.IRON_PICKAXE,
                        Material.GOLDEN_PICKAXE,
                        Material.DIAMOND_PICKAXE,
                        Material.NETHERITE_PICKAXE);

                if (Objects.equals(Shulk.this.plugin.storageUtils.getPlayerOrigin(playerUUID), Origins.SHULK.toString())) {
                    if (blocks.contains(material)) {
                        if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                            if (!tools.contains(mainHandItemType)) {
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, Config.ORIGINS_SHULK_ABILITY_DIGGING_SPEED.toInt(), false, false));
                                    }
                                }.runTask(plugin);
                            }
                        } else if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(plugin);
                        } else if (digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }.runTask(plugin);
                        }
                    }
                }
            }
        });
    }
}
