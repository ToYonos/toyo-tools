package info.toyonos.config;

import java.util.List;

/**
 * An adapter which allows to bind a configuration thanks to its keys
 * 
 * @author ToYonos
 */
public interface ConfigPropertyAdapter
{
	/**
	 * Retrieve a configuration value from its key
	 * 
	 * @param source the class containing the target property
	 * @param prefix the prefix of the property
	 * @param property the key of the property
	 * @return the associated value in configuration, as a String
	 */
	String getProperty(Class<?> source, String prefix, String property);

	/**
	 * Retrieve a configuration value from its key, if the property is a list of values
	 * 
	 * @param source the class containing the target property
	 * @param prefix the prefix of the property
	 * @param property the key of the property
	 * @return the associated value in configuration, as a List of String
	 */
	List<String> getPropertyAsList(Class<?> source, String prefix, String property);
}