package me.bristermitten.spigotexamples.runtime_dependencies;

import me.bristermitten.spigotexamples.reflect.ReflectionFailedException;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderReflection
{

    private static Method addUrlMethod;

    /**
     * Get the method {@link URLClassLoader#addURL(URL)} reflectively.
     * The Method is cached and intitialized lazily.
     *
     * @return the method {@link URLClassLoader#addURL(URL)}.
     * @throws ReflectionFailedException if reflection failed for some reason.
     */
    @SuppressWarnings("JavadocReference")
    public static Method getAddUrlMethod() throws ReflectionFailedException
    {
        if (addUrlMethod == null)
        {
            try
            {
                addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
            }
            catch (ReflectiveOperationException e)
            {
                throw new ReflectionFailedException("Could not access URLClassLoader#addUrl", e);
            }
        }

        return addUrlMethod;
    }
}
