package info.toyonos.config.adapter;

import java.util.Arrays;
import java.util.List;
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
	public String getProperty(Class<?> source, String prefix, String property)
	{
		return properties.get(String.format("%s.%s", prefix, property));
	}

	@Override
	public List<String> getPropertyAsList(Class<?> source, String prefix, String property)
	{
		return Arrays.asList(properties.get(String.format("%s.%s", prefix, property)).split(";"));
	}
}
