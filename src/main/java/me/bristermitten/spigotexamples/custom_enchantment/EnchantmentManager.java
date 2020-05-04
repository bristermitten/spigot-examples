package me.bristermitten.spigotexamples.custom_enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * In charge of holding and managing the custom enchantments.
 * This should be an effective singleton (1 instance per Plugin).
 */
public final class EnchantmentManager
{

    private final JavaPlugin plugin;

    private final Set<Enchantment> customEnchantments;
    private final BlastEnchantment blast;

    /**
     * Create a new EnchantmentManager.
     * This has the side effect of registering all custom enchantment classes as {@link Listener}s.
     *
     * @param plugin the plugin managing the enchantments.
     */
    public EnchantmentManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.customEnchantments = new HashSet<>();

        this.blast = new BlastEnchantment(plugin);

        customEnchantments.add(blast);

    }

    /**
     * Register all custom enchantments with Bukkit.
     * This uses reflection to hook into Bukkit's "acceptingNew" field to allow new Enchantments to be registered.
     * This method should only be called once per server startup.
     */
    public void registerAllCustomEnchantments()
    {
        try
        {
            //Reflection to allow registering new enchants. It's locked by default.
            Field acceptingField = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingField.setAccessible(true);
            acceptingField.set(null, true);

            //Register all enchants here
            for (Enchantment enchantment : customEnchantments)
            {
                Enchantment.registerEnchantment(enchantment);
            }
        }
        catch (Exception ex)
        {
            plugin.getSLF4JLogger().error("Could not register custom enchantments:", ex);
        }
        finally
        {
            Enchantment.stopAcceptingRegistrations();
        }
    }

    public BlastEnchantment getBlast()
    {
        return blast;
    }

    public Set<Enchantment> getCustomEnchantments()
    {
        return Collections.unmodifiableSet(customEnchantments);
    }
}
