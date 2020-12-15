package me.bristermitten.spigotexamples.config_di;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Because {@link JavaPlugin#reloadConfig()} will break our nicely injected FileConfigurations, we're introducing
 * a wrapper class to update with any state
 */
public class ConfigWrapper
{

    private @NotNull FileConfiguration config;

    public ConfigWrapper(@NotNull final FileConfiguration config)
    {
        this.config = config;
    }

    @NotNull
    public FileConfiguration getConfig()
    {
        return config;
    }

    public void setConfig(@NotNull FileConfiguration config)
    {
        this.config = config;
    }
}
