package info.toyonos.config.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import info.toyonos.config.ConfigPropertyAdapter;

/**
 * A <code>ConfigPropertyAdapter</code> for <code>Properties</code> object
 * 
 * @author ToYonos
 */
public class PropertiesAdapter implements ConfigPropertyAdapter
{
	private Properties properties;
	
	public PropertiesAdapter(Properties properties)
	{
		this.properties = properties;
	}
	
	public PropertiesAdapter(InputStream is) throws IOException
	{
		this.properties = new Properties();
		properties.load(is);
	}
	
	public PropertiesAdapter(Reader reader) throws IOException
	{
		this.properties = new Properties();
		properties.load(reader);
	}

	@Override
	public String getProperty(Class<?> source, String prefix, String property)
	{
		return properties.getProperty(String.format("%s.%s", prefix, property));
	}

	@Override
	public List<String> getPropertyAsList(Class<?> source, String prefix, String property)
	{
		return Arrays.asList(properties.getProperty(String.format("%s.%s", prefix, property)).split(";"));
	}
}
