package me.bristermitten.spigotexamples.reflect;

public class ReflectionFailedException extends RuntimeException
{

    public ReflectionFailedException()
    {
    }

    public ReflectionFailedException(String message)
    {
        super(message);
    }

    public ReflectionFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ReflectionFailedException(Throwable cause)
    {
        super(cause);
    }

    public ReflectionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
