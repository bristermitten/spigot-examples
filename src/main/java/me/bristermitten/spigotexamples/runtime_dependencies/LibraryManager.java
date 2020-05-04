package me.bristermitten.spigotexamples.runtime_dependencies;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;

public class LibraryManager
{

    private static final String MAVEN_REPO_URL = "https://repo1.maven.org/maven2";
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final File libsDirectory;

    public LibraryManager(@NotNull final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.libsDirectory = new File(plugin.getDataFolder(), "libs");
        if (!libsDirectory.mkdirs())
        {
            plugin.getLogger().warning("Could not create libs directory.");
        }
    }


    public void loadLibraries(@NotNull final Library... downloads)
    {
        for (Library library : downloads)
        {
            final File libraryFile = new File(libsDirectory, library.getJarName());
            if (!libraryFile.exists())
            {
                downloadLibrary(library);
            }
        }
    }

    private void downloadLibrary(@NotNull final Library library)
    {
        final File jarFile = new File(libsDirectory, library.getJarName());
        try
        {
            final URL url = new URL(MAVEN_REPO_URL + library.toRepositoryURL());

            try (final Writer writer = new FileWriter(jarFile))
            {
                try (final Reader reader = new InputStreamReader(url.openStream()))
                {
                    int nextByte;
                    while ((nextByte = reader.read()) != -1)
                    {
                        writer.write(nextByte);
                    }
                }
            }
        }
        catch (IOException e)
        {
            plugin.getLogger().severe("Could not download " + library);
            plugin.getLogger().throwing("LibraryManager", "downloadLibrary", e);
        }
    }
}
