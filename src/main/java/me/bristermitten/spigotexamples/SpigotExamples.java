package me.bristermitten.spigotexamples;

import me.bristermitten.spigotexamples.custom_enchantment.EnchantmentManager;
import me.bristermitten.spigotexamples.runtime_dependencies.Library;
import me.bristermitten.spigotexamples.runtime_dependencies.LibraryManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotExamples extends JavaPlugin
{

    private EnchantmentManager enchantmentManager;

    @Override
    public void onEnable()
    {
        registerEnchantments();
        loadLibraries();
    }

    private void loadLibraries()
    {
        LibraryManager libraryManager = new LibraryManager(this);

        //For this example we will download the Kotlin stdlib
        final String kotlinVersion = "1.3.72";
        libraryManager.loadLibraries(
                new Library("org.jetbrains.kotlin", "kotlin-stdlib", kotlinVersion),
                new Library("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
        );
    }

    private void registerEnchantments()
    {
        this.enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerAllCustomEnchantments();
    }

    public EnchantmentManager getEnchantmentManager()
    {
        return enchantmentManager;
    }

    @Override
    public void onDisable()
    {
        enchantmentManager.unregisterAllCustomEnchantments();
    }
}
