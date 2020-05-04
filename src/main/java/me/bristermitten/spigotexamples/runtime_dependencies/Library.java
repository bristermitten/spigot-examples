package me.bristermitten.spigotexamples.runtime_dependencies;

import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * Simple POJO defining a Maven repository compatible library.
 */
public final class Library
{

    /**
     * The format for the jar name, on both the Maven repository and local storage.
     * This is formatted as "artifactId-version.jar".
     */
    private static final String JAR_NAME_FORMAT = "%s-%s.jar";
    /**
     * The Maven group id of the library.
     */
    private final @NotNull String groupId;
    /**
     * The Maven artifact id of the library.
     */
    private final @NotNull String artifactId;
    /**
     * The Maven version of the library.
     */
    private final @NotNull String version;
    /**
     * The local jar name of the library.
     */
    private final @NotNull String jarName;

    /**
     * Create a new Library.
     * @param groupId the Maven group id
     * @param artifactId the Maven artifact id
     * @param version the Maven version
     */
    public Library(@NotNull String groupId, @NotNull String artifactId, @NotNull String version)
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.jarName = String.format(JAR_NAME_FORMAT, artifactId, version);
    }

    @NotNull
    public String getJarName()
    {
        return jarName;
    }

    /**
     * Create a Maven Repository compatible URL for this Library.
     * This comprises of <code>"tld/group/id/artifactId/version/artifactId-version.jar"</code>
     * This URL must be appended to a Maven Repository Base URL to work properly.
     * @return a Maven Repository URL for this library.
     */
    public String toRepositoryURL()
    {
        StringJoiner joiner = new StringJoiner("/");
        joiner.add(groupId.replace('.', '/'));
        joiner.add(artifactId);
        joiner.add(version);
        joiner.add(getJarName());
        return joiner.toString();
    }

    @Override
    public String toString()
    {
        return "Library{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Library)) return false;

        Library library = (Library) o;

        if (!groupId.equals(library.groupId)) return false;
        if (!artifactId.equals(library.artifactId)) return false;
        return version.equals(library.version);
    }

    @Override
    public int hashCode()
    {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
