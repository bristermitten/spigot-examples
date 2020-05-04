package me.bristermitten.spigotexamples.runtime_dependencies;

import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public final class Library
{

    private static final String JAR_NAME_FORMAT = "%s-%s.jar";
    private final @NotNull String groupId;
    private final @NotNull String artifactId;
    private final @NotNull String version;
    private final @NotNull String jarName;

    public Library(@NotNull String groupId, @NotNull String artifactId, @NotNull String version)
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.jarName = String.format(JAR_NAME_FORMAT, artifactId, version);
    }

    public @NotNull String getGroupId()
    {
        return groupId;
    }

    public @NotNull String getArtifactId()
    {
        return artifactId;
    }

    public @NotNull String getVersion()
    {
        return version;
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

    @NotNull
    public String getJarName()
    {
        return jarName;
    }

    public String toRepositoryURL()
    {
        StringJoiner joiner = new StringJoiner("/");
        joiner.add(groupId.replace('.', '/'));
        joiner.add(artifactId);
        joiner.add(version);
        joiner.add(getJarName());
        return joiner.toString();
    }
}
