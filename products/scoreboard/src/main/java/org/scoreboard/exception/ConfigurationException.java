package org.scoreboard.exception;

public class ConfigurationException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ConfigurationException()
    {
        super();
    }

    public ConfigurationException( String message )
    {
        super( message );
    }

    public ConfigurationException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public ConfigurationException( Throwable cause )
    {
        super( cause );
    }
}
