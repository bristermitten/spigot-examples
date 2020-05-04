package me.bristermitten.spigotexamples;

import me.bristermitten.spigotexamples.custom_enchantment.EnchantmentManager;
import me.bristermitten.spigotexamples.runtime_dependencies.LibraryManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotExamples extends JavaPlugin
{

    private EnchantmentManager enchantmentManager;
    private LibraryManager libraryManager;

    @Override
    public void onEnable()
    {
        registerEnchantments();
    }

    private void loadLibraries()
    {

    }

    private void registerEnchantments()
    {
        enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerAllCustomEnchantments();
    }

    public EnchantmentManager getEnchantmentManager()
    {
        return enchantmentManager;
    }
}
