package info.toyonos.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field annotated with <code>&#064;ConfigProperty</code> ...
 * 
 * @author ToYonos
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty
{
	/**
	 * @return the default value of the property if not properly set
	 */
    String defaultValue() default "";
    
    /**
     * @return true if the property can be null, false otherwise
     */
    boolean nullable() default false;
    
    /**
     * @return the prefix to add for this <code>&#064;ConfigProperty</code>, override the one in <code>&#064;ConfigProperties</code> if defined
     */
    String prefix() default "";
    
    /**
     * @return the separator used to split the property value if the field is an <code>Array</code> 
     */
    String separator() default ",";
    
    /**
     * @return true if the property value has to be trimmed, false otherwise 
     */
    boolean trim() default false;
}