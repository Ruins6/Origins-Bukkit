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
package me.swagpancakes.originsbukkit.items;

import me.swagpancakes.originsbukkit.Main;
import me.swagpancakes.originsbukkit.util.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Item manager.
 *
 * @author SwagPannekaker
 */
public class ItemManager {

    private final Main plugin;
    public ItemStack abilitySceptre;
    public ShapedRecipe abilitySceptreRecipe;

    /**
     * Instantiates a new Item manager.
     *
     * @param plugin the plugin
     */
    public ItemManager(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Init.
     */
    public void init() {
        createAbilitySceptre();
    }

    /**
     * Create ability sceptre.
     */
    public void createAbilitySceptre() {
        ItemStack itemStack = new ItemStack(Material.STICK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.format("&6Ability Sceptre"));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatUtils.format(""));
        itemMeta.setLore(itemLore);
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.LUCK, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        abilitySceptre = itemStack;

        ShapedRecipe shapedRecipe = new ShapedRecipe(NamespacedKey.minecraft("ability_sceptre"), itemStack);
        shapedRecipe.shape(
                "Y",
                "X",
                "X");
        shapedRecipe.setIngredient('Y', Material.DIRT);
        shapedRecipe.setIngredient('X', Material.STICK);

        abilitySceptreRecipe = shapedRecipe;
    }
}
