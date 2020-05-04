package me.bristermitten.spigotexamples.runtime_dependencies;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * In charge of downloading libraries that are required.
 * Currently this class only uses Maven Central but could be adjusted to support more repositories.
 * Downloaded libraries are stored in the "libs" directory of the plugin's directory and are loaded in using the Plugin's ClassLoader.
 * This class should be an effective singleton (1 instance per Plugin).
 */
public class LibraryManager
{

    /**
     * The base URL of the Maven Central URL.
     */
    private static final String MAVEN_REPO_URL = "https://repo1.maven.org/maven2";

    /**
     * The plugin that these libraries are for.
     */
    @NotNull
    private final JavaPlugin plugin;
    /**
     * The libs directory of the plugin (plugins/PluginName/libs)
     * where downloaded libaries are stored.
     */
    @NotNull
    private final File libsDirectory;

    /**
     * Create a new LibraryManager. This creates the libs directory if necessary.
     *
     * @param plugin the plugin that the libraries are downloaded for.
     */
    public LibraryManager(@NotNull final JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.libsDirectory = new File(plugin.getDataFolder(), "libs");
        if (!libsDirectory.mkdirs())
        {
            plugin.getSLF4JLogger().warn("Could not create libs directory.");
        }
    }


    /**
     * Load libraries into the classpath, downloading them if necessary
     *
     * @param downloads the libraries to download.
     */
    public void loadLibraries(@NotNull final Library... downloads)
    {
        for (Library library : downloads)
        {
            final File libraryFile = new File(libsDirectory, library.getJarName());
            if (!libraryFile.exists())
            {
                downloadLibrary(library);
            }

            loadIntoClasspath(libraryFile);
        }
    }

    /**
     * Load a file into the classpath using the {@link ClassLoader} of {@link LibraryManager#plugin}
     *
     * @param libraryFile the file to load into the classpath.
     */
    private void loadIntoClasspath(@NotNull final File libraryFile)
    {
        Method addUrl = ClassLoaderReflection.getAddUrlMethod();
        URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

        try
        {
            addUrl.invoke(classLoader, libraryFile.toURI().toURL());
        }
        catch (IllegalAccessException | InvocationTargetException | MalformedURLException e)
        {
            plugin.getSLF4JLogger().error("Could not invoke URLClassLoader#addUrl", e);
        }
    }

    /**
     * Download a given library from Maven Central, storing the file in {@link LibraryManager#libsDirectory}
     *
     * @param library the library to download.
     */
    private void downloadLibrary(@NotNull final Library library)
    {
        final File jarFile = new File(libsDirectory, library.getJarName());
        try
        {
            final URL url = new URL(MAVEN_REPO_URL + library.toRepositoryURL());

            //Copy the data from the URL to the File
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
            plugin.getSLF4JLogger().error("Could not download {}", library, e);

        }
    }
}
