package me.bristermitten.spigotexamples.customenchantment;

import me.bristermitten.spigotexamples.reflect.MemberType;
import me.bristermitten.spigotexamples.reflect.ReflectionCache;
import me.bristermitten.spigotexamples.reflect.ReflectionDefinition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * In charge of holding and managing the custom enchantments.
 * This should be an effective singleton (1 instance per Plugin).
 */
public final class EnchantmentManager
{

    private static final ReflectionDefinition ACCEPTING_NEW_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "acceptingNew");
    private static final ReflectionDefinition BY_KEY_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "byKey");
    private static final ReflectionDefinition BY_NAME_FIELD_DEF = new ReflectionDefinition(Enchantment.class, MemberType.FIELD, "byName");

    private final JavaPlugin plugin;
    private final Set<Enchantment> customEnchantments;
    private final BlastEnchantment blast;

    /**
     * Create a new EnchantmentManager.
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
     * This method also registers any enchantments that are event handlers.
     * This method should only be called once per server startup.
     */
    public void registerAllCustomEnchantments()
    {
        try
        {
            //Reflection to allow registering new enchants. It's locked by default.
            Field acceptingField = (Field) ReflectionCache.get(ACCEPTING_NEW_FIELD_DEF);
            acceptingField.set(null, true);

            //Register all enchants here
            for (Enchantment enchantment : customEnchantments)
            {
                Enchantment.registerEnchantment(enchantment);
                if (enchantment instanceof Listener)
                {
                    Bukkit.getPluginManager().registerEvents((Listener) enchantment, plugin);
                }
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

    /**
     * Reflectively unregister all custom enchantments from Bukkit, and their event handlers.
     */
    public void unregisterAllCustomEnchantments()
    {
        try
        {
            Field byKeyField = (Field) ReflectionCache.get(BY_KEY_FIELD_DEF);
            Field byNameField = (Field) ReflectionCache.get(BY_NAME_FIELD_DEF);

            @SuppressWarnings("unchecked") Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            @SuppressWarnings("unchecked") Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            for (Enchantment enchantment : customEnchantments)
            {
                byKey.remove(enchantment.getKey());
                //noinspection deprecation
                byName.remove(enchantment.getName());

                if (enchantment instanceof Listener)
                {
                    HandlerList.unregisterAll((Listener) enchantment);
                }
            }
        }
        catch (Exception e)
        {
            plugin.getSLF4JLogger().error("Could not unregister custom enchantments:", e);
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
