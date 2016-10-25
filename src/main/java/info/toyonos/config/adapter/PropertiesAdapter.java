package info.toyonos.config.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}
}
