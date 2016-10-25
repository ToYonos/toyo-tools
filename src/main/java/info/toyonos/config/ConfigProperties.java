package info.toyonos.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to add global settings for all <code>&#064;ConfigProperty</code> defined in the target class.
 * 
 * @author ToYonos
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperties
{
	/**
	 * @return the prefix to add for all <code>&#064;ConfigProperty</code> (can be overridden)  
	 */
	String prefix() default "";
	
	/**
	 * @return the <code>ConfigPropertyAdapter</code> to use. Only use it if the adapter possesses a no-arg constructor
	 */
	Class<? extends ConfigPropertyAdapter> configPropertyAdapterClass() default ConfigPropertyAdapter.class;
}