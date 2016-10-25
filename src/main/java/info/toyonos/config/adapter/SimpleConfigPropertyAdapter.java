package info.toyonos.config.adapter;

import java.util.Map;

import info.toyonos.config.ConfigPropertyAdapter;

/**
 * A simple <code>ConfigPropertyAdapter</code> using a <code>Map</code>
 * 
 * @author ToYonos
 */
public class SimpleConfigPropertyAdapter implements ConfigPropertyAdapter
{
	private Map<String, String> properties;
	
	public SimpleConfigPropertyAdapter(Map<String, String> properties)
	{
		this.properties = properties;
	}

	@Override
	public String getProperty(String key)
	{
		return properties.get(key);
	}
}
