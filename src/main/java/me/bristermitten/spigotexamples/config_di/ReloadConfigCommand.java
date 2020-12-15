package me.bristermitten.spigotexamples.config_di;

import me.bristermitten.spigotexamples.SpigotExamples;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor
{

    private final SpigotExamples spigotExamples;
    private final ConfigWrapper config;

    public ReloadConfigCommand(SpigotExamples spigotExamples, ConfigWrapper config)
    {
        this.spigotExamples = spigotExamples;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        //do something
        spigotExamples.reloadConfig(); //This will edit the value that spigotExamples.getConfig() returns

        config.setConfig(spigotExamples.getConfig()); //So we need to update our config wrapperA

        config.getConfig().getString("blah"); //This will now hold the new value!

        sender.sendMessage("Config reloaded!");
        return false;
    }
}
