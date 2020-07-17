package info.toyonos.config;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>An object to bind annotated static fields from a class, to a configuration adapted in a <code>ConfigPropertyBinder</code></p> 
 * 
 * Example :
 * 
 * Let's take this configuration file, <code>config.properties</code>
 *
 * <pre>
 * myapp.db.username=user
 * myapp.db.password=passw0rd
 * myapp.db.connection.timeout=180
 * 
 * myapp.supported.currencies=usd;eur;gbp
 * </pre>
 * 
 * Now, let's map this file in a class :
 * 
 * <pre>
 * &#064;ConfigProperties(prefix = "myapp")
 * public class Foo
 * {
 * 	&#064;ConfigProperty
 * 	public static final String DB_USERNAME = null;
 * 	
 * 	&#064;ConfigProperty
 * 	public static final String DB_PASSWORD = null;
 * 	
 * 	&#064;ConfigProperty(defaultValue="60")
 * 	public static final Integer DB_CONNECTION_TIMEOUT = null;
 * 	
 * 	&#064;ConfigProperty
 * 	public static final String[] SUPPORTED_CURRENCIES = null;
 * 	
 * 	&#064;ConfigProperty(defaultValue="false")
 * 	public static final Boolean DEBUG_MODE = null;
 * 	
 * 	&#064;ConfigProperty(nullable = true)
 * 	public static final String LICENCE_PATH = null;
 * 	
 * 	static
 * 	{
 *		try
 *		{
 *			ConfigPropertyBinder.create(new PropertiesAdapter(new FileReader(new File("/path/to/config.properties"))), Foo.class).bind();
 *		}
 *		catch (IOException e)
 *		{
 *			e.printStackTrace();
 *		}
 * 	}
 * }
 * </pre>
 * 
 * @author ToYonos
 */
public final class ConfigPropertyBinder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropertyBinder.class);

	private static final PeriodFormatter PERIOD_FORMATTER =
			new PeriodFormatterBuilder()
				.appendDays().appendSuffix("d")
				.appendHours().appendSuffix("h")
				.appendMinutes().appendSuffix("m")
				.appendSeconds().appendSuffix("s")
				.appendMillis().appendSuffix("ms")
				.toFormatter();

		private static final ConvertUtilsBean CONVERT_UTILS_BEAN = BeanUtilsBean.getInstance().getConvertUtils();

		static
		{
			CONVERT_UTILS_BEAN.register(false, true, 0);
			CONVERT_UTILS_BEAN.register(new Converter()
			{
				@Override
				public <T> T convert(Class<T> type, Object value)
				{
					return type.cast(Duration.parse(PERIOD_FORMATTER.parsePeriod(value.toString()).toStandardDuration().toString()));
				}
			}, Duration.class);
		}

		private ConfigPropertyAdapter configPropertyAdapter;
		private Class<?> target;
		private ConfigProperties configProperties;

		private ConfigPropertyBinder(ConfigPropertyAdapter configPropertyAdapter, Class<?> target, ConfigProperties configProperties)
		{
			this.configPropertyAdapter = configPropertyAdapter;
			this.target = target;
			this.configProperties = configProperties;
		}
		
		/**
		 * Constructs a <code>ConfigPropertyBinder</code> for a target class with the given <code>ConfigPropertyAdapter</code>
		 * 
		 * @param configPropertyAdapter the <code>configPropertyAdapter</code> to use
		 * @param target the target class to bind
		 * @return the new <code>ConfigPropertyBinder</code>
		 */
		public static ConfigPropertyBinder create(ConfigPropertyAdapter configPropertyAdapter, Class<?> target)
		{
			return new ConfigPropertyBinder(configPropertyAdapter, target, target.getAnnotation(ConfigProperties.class));
		}
		
		/**
		 * <p>Constructs a <code>ConfigPropertyBinder</code> for a target class</p>
		 * <p>Use the <code>ConfigPropertyAdapter</code> defined in the annotation <code>ConfigProperties</code></p>
		 * 
		 * @param target the target class to bind
		 * @return the new <code>ConfigPropertyBinder</code>
		 * @throws ConfigPropertyException if the <code>ConfigProperties</code> annotation is missing or if the <code>ConfigPropertyAdapter</code> instantiation failed
		 */
		public static ConfigPropertyBinder create(Class<?> target)
		{
			ConfigProperties configProperties = target.getAnnotation(ConfigProperties.class);
			if (configProperties == null)
			{
				throw new ConfigPropertyException(String.format("The ConfigProperties annotation is not defined for the target class '%s'", target));
			}

			try
			{
				return new ConfigPropertyBinder(configProperties.configPropertyAdapterClass().newInstance(), target, configProperties);
			}
			catch (ReflectiveOperationException e)
			{
				throw new ConfigPropertyException("Unable to instantiate the ConfigPropertyAdapter", e);
			}
		}

		/**
		 * Bind the configuration into the target class
		 */
		public void bind()
		{
			for (Field propertyField : target.getDeclaredFields())
			{
				ConfigProperty configProperty = propertyField.getAnnotation(ConfigProperty.class);
				if (Modifier.isStatic(propertyField.getModifiers()) && configProperty != null)
				{
					try
					{
						if (!propertyField.isAccessible()) propertyField.setAccessible(true);
						removeFinalModifier(propertyField);
						setPropertyField(propertyField, configProperty);
						if (LOGGER.isDebugEnabled())
						{
							Object value = propertyField.get(null);
							LOGGER.debug(
								String.format(
									"Property %s has been set with %s",
									propertyField.getName(),
									value == null ?
										null :
										(propertyField.getType().isArray() ?
											Arrays.asList(getArray(propertyField.get(null))) :
											propertyField.get(null)
										)
									)
							);
						}
					}
					catch (ConversionException | IllegalArgumentException | ReflectiveOperationException | SecurityException e)
					{
						LOGGER.warn(String.format("Unable to set the ConfigProperty %s", propertyField.getName()), e);
					}
				}
			}
		}

		private Object[] getArray(Object val)
		{

			if (val instanceof Object[]) return (Object[]) val;
			int arrlength = Array.getLength(val);
			Object[] outputArray = new Object[arrlength];
			for (int i = 0; i < arrlength; ++i)
			{
				outputArray[i] = Array.get(val, i);
			}
			return outputArray;
		}
		
		private void removeFinalModifier(Field propertyField) throws NoSuchFieldException, IllegalAccessException
		{
			if (Modifier.isFinal(propertyField.getModifiers()))
			{
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				if (!modifiersField.isAccessible()) modifiersField.setAccessible(true);
				modifiersField.setInt(propertyField, propertyField.getModifiers() & ~Modifier.FINAL);
				if (propertyField.get(null) != null)
				{
					/*
					 * Special case for already set static final fields : they can't be changed
					 * 
					 * @see http://stackoverflow.com/a/3301818/2003986
					 * @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-13.html#jls-13.4.9
					 * 
					 * "If a field is a constant variable (§4.12.4), then deleting the keyword final or changing its value
					 * will not break compatibility with pre-existing binaries by causing them not to run,
					 * but they will not see any new value for the usage of the field unless they are recompiled.
					 * This is true even if the usage itself is not a compile-time constant expression (§15.28)"
					 * 
					 */
					throw new IllegalStateException(String.format("The final field %s has already a value", propertyField.getName()));
				}
			}
		}

		private void setPropertyField(Field propertyField, ConfigProperty configProperty) throws IllegalAccessException
		{
			Pair<String, String> propertyId = getPropertyId(propertyField, configProperty);
			Object convertedValue = getConvertedValue(propertyField, configProperty, propertyId);
			
			if (!configProperty.nullable() && convertedValue == null)
			{
				throw new MissingConfigPropertyException(
					String.format(
						"The property %s could not be set, missing value in configuration (prefix : %s, key : %s)",
						propertyField.getName(),
						propertyId.getLeft(),
						propertyId.getRight()
					)
				);
			}
			else
			{
				propertyField.set(null, convertedValue);
			}
		}

		private Object getConvertedValue(Field propertyField, ConfigProperty configProperty, Pair<String, String> propertyId)
		{		
			if (propertyField.getType().isArray())
			{
				List<String> defaultValue = configProperty.defaultListValue().length > 0 ? Arrays.asList(configProperty.defaultListValue()) : null;
				List<String> value = defaultIfNull(
					configPropertyAdapter.getPropertyAsList(propertyField.getDeclaringClass(), propertyId.getLeft(), propertyId.getRight()),
					defaultValue
				);
				if (value != null)
				{
			        Class<?> finalComponentType = ClassUtils.primitiveToWrapper(propertyField.getType().getComponentType());
			        Object newArray = Array.newInstance(propertyField.getType().getComponentType(), value.size());
			        for (int i = 0; i < value.size(); i++)
			        {
			        	Array.set(
			        		newArray,
			        		i,
			        		finalComponentType.cast(CONVERT_UTILS_BEAN.convert(
			        			configProperty.trim() && value.get(i) != null ? value.get(i).trim() : value.get(i),
			        			finalComponentType
			        		))
			        	);
			        }
			        return propertyField.getType().cast(newArray);
				}
			}
			else
			{
				String defaultValue = !configProperty.defaultValue().isEmpty() ? configProperty.defaultValue() : null;
				String value = defaultIfNull(
					configPropertyAdapter.getProperty(propertyField.getDeclaringClass(), propertyId.getLeft(), propertyId.getRight()),
					defaultValue
				);
				if (value != null)
				{
					String processedValue = configProperty.trim() ? value.trim() : value;
					Class<?> finalClass = ClassUtils.primitiveToWrapper(propertyField.getType());
					return finalClass.cast(CONVERT_UTILS_BEAN.convert(processedValue, finalClass));
				}
			}

			return null;
		}

		private Pair<String, String> getPropertyId(Field propertyField, ConfigProperty configProperty)
		{
			return Pair.of(
				getDefaultValue(configProperty.prefix(), configProperties != null ? getDefaultValue(configProperties.prefix()) : null),
				propertyField.getName().replace('_', '.').toLowerCase()
			);
		}

		private String getDefaultValue(String value)
		{
			return getDefaultValue(value, null);
		}
		
		private String getDefaultValue(String value, String other)
		{
			return !value.isEmpty() ? value : other;
		}
	
//	static
//	{
//		BeanUtilsBean.getInstance().getConvertUtils().register(true, false, 0);
//	}
//
//	private ConfigPropertyAdapter configPropertyAdapter;
//	private Class<?> target;
//	private ConfigProperties configProperties;
// 
//	private ConfigPropertyBinder(ConfigPropertyAdapter configPropertyAdapter, Class<?> target, ConfigProperties configProperties)
//	{
//		this.configPropertyAdapter = configPropertyAdapter;
//		this.target = target;
//		this.configProperties = configProperties;
//	}
//	
//	/**
//	 * Constructs a <code>ConfigPropertyBinder</code> for a target class with the given <code>ConfigPropertyAdapter</code>
//	 * 
//	 * @param configPropertyAdapter the <code>ConfigPropertyBinder</code> to use
//	 * @param target the target class to bind
//	 * @return the new <code>ConfigPropertyBinder</code>
//	 */
//	public static ConfigPropertyBinder create(ConfigPropertyAdapter configPropertyAdapter, Class<?> target)
//	{
//		return new ConfigPropertyBinder(configPropertyAdapter, target, target.getAnnotation(ConfigProperties.class));
//	}
//	
//	/**
//	 * <p>Constructs a <code>ConfigPropertyBinder</code> for a target class</p>
//	 * <p>Use the <code>ConfigPropertyAdapter</code> defined in the annotation <code>ConfigProperties</code></p>
//	 * 
//	 * @param target the target class to bind
//	 * @return the new <code>ConfigPropertyBinder</code>
//	 * @throws ConfigPropertyException if the <code>ConfigProperties</code> annotation is missing or if the <code>ConfigPropertyAdapter</code> instantiation failed
//	 */
//	public static ConfigPropertyBinder create(Class<?> target)
//	{
//		ConfigProperties configProperties = target.getAnnotation(ConfigProperties.class);
//		if (configProperties == null)
//		{
//			throw new ConfigPropertyException(String.format("The ConfigProperties annotation is not defined for the target class '%s'", target));
//		}
//
//		try
//		{
//			return new ConfigPropertyBinder(configProperties.configPropertyAdapterClass().newInstance(), target, configProperties);
//		}
//		catch (ReflectiveOperationException e)
//		{
//			throw new ConfigPropertyException("Unable to instantiate the ConfigPropertyAdapter", e);
//		}
//	}
//
//	/**
//	 * Bind the configuration into the target class
//	 */
//	public void bind()
//	{
//		for (Field propertyField : target.getDeclaredFields())
//		{
//			ConfigProperty configProperty = propertyField.getAnnotation(ConfigProperty.class);
//			if (Modifier.isStatic(propertyField.getModifiers()) && configProperty != null)
//			{
//				try
//				{
//					if (!propertyField.isAccessible()) propertyField.setAccessible(true);
//					removeFinalModifier(propertyField);
//					setPropertyField(propertyField, configProperty);
//				}
//				catch (ConversionException | IllegalArgumentException | ReflectiveOperationException | SecurityException e)
//				{
//					LOGGER.warn(String.format("Unable to set the ConfigProperty %s", propertyField.getName()), e);
//				}
//			}
//		}
//	}
//
//	private void removeFinalModifier(Field propertyField) throws NoSuchFieldException, IllegalAccessException
//	{
//		if (Modifier.isFinal(propertyField.getModifiers()))
//		{
//			Field modifiersField = Field.class.getDeclaredField("modifiers");
//			if (!modifiersField.isAccessible()) modifiersField.setAccessible(true);
//			modifiersField.setInt(propertyField, propertyField.getModifiers() & ~Modifier.FINAL);
//			if (propertyField.get(null) != null)
//			{
//				/*
//				 * Special case for already set static final fields : they can't be changed
//				 * 
//				 * see http://stackoverflow.com/a/3301818/2003986
//				 * see http://docs.oracle.com/javase/specs/jls/se7/html/jls-13.html#jls-13.4.9
//				 * 
//				 * "If a field is a constant variable (§4.12.4), then deleting the keyword final or changing its value
//				 * will not break compatibility with pre-existing binaries by causing them not to run,
//				 * but they will not see any new value for the usage of the field unless they are recompiled.
//				 * This is true even if the usage itself is not a compile-time constant expression (§15.28)"
//				 * 
//				 */
//				throw new IllegalStateException(String.format("The final field %s has already a value", propertyField.getName()));
//			}
//		}
//	}
//
//	private void setPropertyField(Field propertyField, ConfigProperty configProperty) throws IllegalAccessException
//	{
//		String prefix = configProperty.prefix().isEmpty() ?
//			(configProperties != null ? configProperties.prefix() : null) :
//			configProperty.prefix();
//		prefix = prefix != null && !prefix.isEmpty() ? prefix + "." : "";
//
//		String property = prefix + propertyField.getName().replace('_', '.').toLowerCase();
//		String value = configPropertyAdapter.getProperty(property);
//		if (value != null)
//		{
//			propertyField.set(null, convertStringValue(configProperty, propertyField.getType(), value));
//		}
//		else
//		{
//			if (configProperty.nullable())
//			{
//				propertyField.set(null, convertStringValue(configProperty, propertyField.getType(), configProperty.defaultValue().isEmpty() ? null : configProperty.defaultValue()));
//			}
//			else
//			{
//				if (!configProperty.defaultValue().isEmpty())
//				{
//					propertyField.set(null, convertStringValue(configProperty, propertyField.getType(), configProperty.defaultValue()));
//				}
//				else
//				{
//					throw new MissingConfigPropertyException(String.format("The property %s could not be set, missing value in configuration (key : %s)", propertyField.getName(), property));
//				}
//			}
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private <A> A convertStringValue(ConfigProperty configProperty, Class<A> clazz, String value)
//	{
//		String processedValue = configProperty.trim() ? value.trim() : value;
//		Class<A> finalClass = (Class<A>) ClassUtils.primitiveToWrapper(clazz);
//		
//		if (clazz.isArray())
//		{
//			String[] tokens = processedValue.split(configProperty.separator());
//	        Class<?> finalComponentType = ClassUtils.primitiveToWrapper(clazz.getComponentType());
//	        Object newArray = Array.newInstance(clazz.getComponentType(), tokens.length);
//	        for (int i = 0; i < tokens.length; i++)
//	        {
//	        	Array.set(newArray, i, finalComponentType.cast(ConvertUtils.convert(tokens[i], finalComponentType)));
//	        }
//	        return clazz.cast(newArray);
//		}
//		else
//		{
//			return finalClass.cast(ConvertUtils.convert(processedValue, finalClass));
//		}
//	}
}
