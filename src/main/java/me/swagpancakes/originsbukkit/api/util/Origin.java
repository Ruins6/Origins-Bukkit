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
package me.swagpancakes.originsbukkit.api.util;

import me.swagpancakes.originsbukkit.OriginsBukkit;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Origin.
 *
 * @author SwagPannekaker
 */
public abstract class Origin implements OriginInterface {

    private final double maxHealth;
    private final float walkSpeed;
    private final float flySpeed;

    /**
     * Instantiates a new Origin.
     *
     * @param maxHealth the max health
     * @param walkSpeed the walk speed
     * @param flySpeed  the fly speed
     */
    public Origin(double maxHealth, float walkSpeed, float flySpeed) {
        this.maxHealth = maxHealth;
        this.walkSpeed = walkSpeed;
        this.flySpeed = flySpeed;
    }

    /**
     * Gets max health.
     *
     * @return the max health
     */
    public double getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Gets walk speed.
     *
     * @return the walk speed
     */
    public float getWalkSpeed() {
        return this.walkSpeed;
    }

    /**
     * Gets fly speed.
     *
     * @return the fly speed
     */
    public float getFlySpeed() {
        return this.flySpeed;
    }

    /**
     * Register origin.
     *
     * @param origin the origin
     */
    public void registerOrigin(String origin) {
        if (origin != null && !origin.isEmpty()) {
            if (!containsSpecialChars(origin)) {
                if (Objects.equals(origin, getOriginIdentifier())) {
                    if (getAuthor() != null && !getAuthor().isEmpty()) {
                        if (!containsSpecialChars(getAuthor())) {
                            if (getOriginIcon() != null) {
                                if (getOriginIcon() != Material.AIR) {
                                    if (!OriginsBukkit.getPlugin().getOrigins().contains(origin)) {
                                        OriginsBukkit.getPlugin().getOrigins().add(origin);
                                        Inventory inventory = Bukkit.createInventory(null, 54, ChatUtils.format("&0Choose your Origin."));
                                        inventory.setItem(22, createGuiItem(getOriginIcon(), 1,
                                                getOriginTitle(),
                                                getOriginDescription()));
                                        OriginsBukkit.getPlugin().getOriginsInventoryGUI().add(inventory);
                                        if (OriginsBukkit.getPlugin().getPlayerOriginChecker() != null) {
                                            OriginsBukkit.getPlugin().getPlayerOriginChecker().originPickerGui();
                                        }
                                        ChatUtils.sendConsoleMessage("&a[Origins-Bukkit] Successfully registered &6" + origin + "&a origin by &e" + getAuthor());
                                    } else {
                                        ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The origin &e\"" + origin + "\"&c has already been registered. Ignoring it... Please restart the server if you've updated or made changes to the extension for changes to take full effect.");
                                    }
                                } else {
                                    ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The icon of the origin &e\"" + origin + "\"&c cannot be set to air. Please contact the author (" + getAuthor() + ") of this origin.");
                                }
                            } else {
                                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The icon of the origin &e\"" + origin + "\"&c cannot be null. Please contact the author (" + getAuthor() + ") of this origin.");
                            }
                        } else {
                            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The author of the origin &e\"" + origin + "\"&c contains character(s) that are not allowed.");
                        }
                    } else {
                        ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The author of the origin &e\"" + origin + "\"&c cannot be null.");
                    }
                } else {
                    ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The unique identifier of the origin &e\"" + origin + "\"&c does not match the registered identifier. Please contact the author (" + getAuthor() + ") of this origin.");
                }
            } else {
                ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The unique identifier of the origin &e\"" + origin + "\"&c contains character(s) that are not allowed. Please contact the author (" + getAuthor() + ") of this origin.");
            }
        } else {
            ChatUtils.sendConsoleMessage("&c[Origins-Bukkit] Error registering origin. The unique identifier of the origin &e\"" + origin + "\"&c cannot be null. Please contact the author (" + getAuthor() + ") of this origin.");
        }
    }

    /**
     * Create gui item item stack.
     *
     * @param material the material
     * @param amount   the amount
     * @param itemName the item name
     * @param itemLore the item lore
     *
     * @return the item stack
     */
    ItemStack createGuiItem(Material material, Integer amount, String itemName, String... itemLore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(itemName);
            itemMeta.setLocalizedName(getOriginIdentifier());
            itemMeta.setLore(Arrays.asList(itemLore));
            if (isOriginIconGlowing()) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    /**
     * Contains special chars boolean.
     *
     * @param toExamine the to examine
     *
     * @return the boolean
     */
    private boolean containsSpecialChars(String toExamine) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }
}
