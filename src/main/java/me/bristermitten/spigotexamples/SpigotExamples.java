package me.bristermitten.spigotexamples;

import me.bristermitten.spigotexamples.customenchantment.EnchantmentManager;
import me.bristermitten.spigotexamples.runtimelibraries.Library;
import me.bristermitten.spigotexamples.runtimelibraries.LibraryManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotExamples extends JavaPlugin
{

    private static final String kotlinVersion = "1.3.72";
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
        libraryManager.loadLibraries(
                new Library("org.jetbrains.kotlin", "kotlin-stdlib", kotlinVersion),
                new Library("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
        );

        try
        {
            Class.forName("kotlin.Unit");
        }
        catch (ClassNotFoundException e)
        {
            getSLF4JLogger().error("Kotlin stdlib was not loaded! Runtime Libraries didn't work?", e);
        }
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
