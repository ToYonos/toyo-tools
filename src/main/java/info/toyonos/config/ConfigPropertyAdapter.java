package info.toyonos.config;

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
	 * @param key the key of the property
	 * @return the associated value in configuration
	 */
	String getProperty(String key);
}