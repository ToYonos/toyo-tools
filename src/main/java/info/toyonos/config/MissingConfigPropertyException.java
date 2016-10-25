package info.toyonos.config;

public class MissingConfigPropertyException extends ConfigPropertyException
{
	private static final long serialVersionUID = 1L;

	public MissingConfigPropertyException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MissingConfigPropertyException(Throwable cause)
	{
		super(cause);
	}

	public MissingConfigPropertyException(String message)
	{
		super(message);
	}
}