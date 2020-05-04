package me.bristermitten.spigotexamples.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Simple POJO defining the data for an {@link AccessibleObject}.
 * This class should be used in {@link ReflectionCache}
 */
public class ReflectionDefinition
{

    /**
     * The class that holds this {@link AccessibleObject}
     */
    @NotNull
    private final Class<?> holder;

    /**
     * The type of the {@link AccessibleObject}
     */
    @NotNull
    private final MemberType type;

    /**
     * The name of the {@link AccessibleObject}, if any
     */
    @Nullable
    private final String name;

    /**
     * The parameter types for an {@link Executable}. In the case of a field, this array will be empty.
     */
    @NotNull
    private final Class<?>[] parameterTypes;

    public ReflectionDefinition(@NotNull Class<?> holder, @NotNull MemberType type, @Nullable String name, @NotNull Class<?>... parameterTypes)
    {
        this.holder = holder;
        this.type = type;
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ReflectionDefinition)) return false;

        ReflectionDefinition that = (ReflectionDefinition) o;

        if (!holder.equals(that.holder)) return false;
        if (type != that.type) return false;
        if (!Objects.equals(name, that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode()
    {
        int result = holder.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public String toString()
    {
        return "ReflectionDefinition{" +
                "holder=" + holder +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }

    /**
     * Get an {@link AccessibleObject} based on this definition.
     *
     * @return an {@link AccessibleObject} based on this definition.
     * @throws ReflectiveOperationException if reflection failed.
     */
    @NotNull
    public AccessibleObject getAccessibleObject() throws ReflectiveOperationException
    {
        return type.get(holder, name, parameterTypes);
    }
}
