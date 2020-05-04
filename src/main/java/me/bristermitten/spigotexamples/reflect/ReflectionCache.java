package me.bristermitten.spigotexamples.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches reflectively obtained data based on a {@link ReflectionDefinition}
 */
public class ReflectionCache
{

    private static final Map<ReflectionDefinition, AccessibleObject> CACHE = new HashMap<>();

    private ReflectionCache()
    {

    }

    /**
     * Get an {@link AccessibleObject} based on the given {@link ReflectionDefinition}
     *
     * @param definition the definition for the {@link AccessibleObject}
     * @return an {@link AccessibleObject} that may have been present in cache
     * @throws ReflectionFailedException if the reflection could not be performed.
     */
    @NotNull
    public static AccessibleObject get(ReflectionDefinition definition) throws ReflectionFailedException
    {
        final AccessibleObject existing = CACHE.get(definition);
        if (existing != null)
        {
            return existing;
        }

        try
        {
            final AccessibleObject retrieved = definition.getAccessibleObject();
            retrieved.setAccessible(true);
            CACHE.put(definition, retrieved);
            return retrieved;
        }
        catch (ReflectiveOperationException e)
        {
            throw new ReflectionFailedException(e);
        }
    }
}
