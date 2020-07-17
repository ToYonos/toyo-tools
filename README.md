# toyo-tools

A set of tools for Java developers

## Documentation

### ConfigProperty

A basic need for many programs is too use settings stored in an arbitrary container. These settings, which can be mandatory and typed, are typically stored in constants.
The `ConfigProperty` annotation allows you to simply bind a setting to a static field in a class.

Let's take this configuration file, `config.properties`
```
myapp.db.username=user
myapp.db.password=passw0rd
myapp.db.connection.timeout=180

myapp.supported.currencies=usd;eur;gbp
```

Now, let's map this file in a class :

```java
@ConfigProperties(prefix = "myapp")
public class Foo
{
	@ConfigProperty
	public static final String DB_USERNAME = null;

	@ConfigProperty
	public static final String DB_PASSWORD = null;
	
	@ConfigProperty(defaultValue="60")
	public static final Integer DB_CONNECTION_TIMEOUT = null;
	
	@ConfigProperty
	public static final String[] SUPPORTED_CURRENCIES = null;
	
	@ConfigProperty(defaultValue="false")
	public static final Boolean DEBUG_MODE = null;
	
	@ConfigProperty(nullable = true)
	public static final String LICENCE_PATH = null;
	
	static
	{
		try
		{
			ConfigPropertyBinder.create(
				new PropertiesAdapter(
					new FileReader(new File("/path/to/config.properties"))
				),
				Foo.class
			).bind();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
```

`@ConfigProperty` annotation is used to map a property to a constant. The property key would be the name of the constant in lower case, with dot instead of underscore and prefixed with the `prefix` attribute value, set in the main annotation `@ConfigProperties`

A `ConfigPropertyAdapter` instance is necessary in order to properly associate your configuration. Some simple adapters are provided but you can off course implement yours.  

For more details, please check the javadoc. 
