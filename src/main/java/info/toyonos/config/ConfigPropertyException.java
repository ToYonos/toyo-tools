package info.toyonos.config;

public class ConfigPropertyException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ConfigPropertyException(String message)
	{
		super(message);
	}

	public ConfigPropertyException(Throwable cause)
	{
		super(cause);
	}

	public ConfigPropertyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
