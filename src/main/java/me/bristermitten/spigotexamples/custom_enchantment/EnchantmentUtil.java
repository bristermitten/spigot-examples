package me.bristermitten.spigotexamples.custom_enchantment;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class EnchantmentUtil
{

    private static final Set<Material> PICKAXE_TYPES = EnumSet.of(
            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE);

    private EnchantmentUtil()
    {

    }

    public static boolean isPickaxe(@NotNull final ItemStack item)
    {
        return PICKAXE_TYPES.contains(item.getType());
    }

    /**
     * Because these are custom enchantments, the Minecraft client does not add them to the lore automatically.
     * As such, we have to add it ourselves.
     * <p>
     * This method uses {@link ItemMeta} and a few String operations to add the lore manually, removing old versioned lore.
     *
     * @param enchantment the enchantment to add.
     * @param itemStack the item to add the enchantment to.
     * @param level the level of the enchantment to add.
     * @throws IllegalArgumentException if the enchantment is unsafe (more than the maximum level).
     */
    public static void applyEnchantment(@NotNull final Enchantment enchantment, @NotNull final ItemStack itemStack, final int level) throws IllegalArgumentException
    {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return; //the item type is probably AIR so can't be enchanted.

        List<String> lore = itemMeta.getLore();

        if (lore == null)
        {
            lore = new ArrayList<>(1); //a nice little micro-optimization since we're only adding 1 element to the list
        } else
        {
            int existingLevel = itemMeta.getEnchantLevel(enchantment);
            if (existingLevel != 0) //Lore removal needed as some is present.
            {
                //remove any old lore. Note that this way of doing it could break lore ordering.
                lore.removeIf(line -> ChatColor.stripColor(line).matches(enchantment.getKey().getKey() + " " + existingLevel));
            }
        }

        lore.add(enchantment.getKey().getKey() + " " + level);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        //Simple as this! Glowing is handled automatically.
        itemStack.addEnchantment(enchantment, level);
    }
}
