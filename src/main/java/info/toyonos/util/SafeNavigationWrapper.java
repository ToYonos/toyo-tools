package info.toyonos.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * 
 * <p>Simple wrapper that emulates Groovy's Safe Navigation Operator, in order to avoid a <code>NullPointerException</code>.<p>
 * <p>It's based on Functional Interface (<code>java.util.function.Function</code>)</p>
 * 
 * Ex:
 * <pre>
 * 
 * public class Parent
 * {
 *    private Parent child;
 *	
 *    public Parent getChild()
 *    {
 *       return child;
 *    }
 * }
 * 
 * Parent grandFather = ...;
 * 
 * // SafeNavigationWrapper way 
 * Parent grandChild = $(grandFather).$(Parent::getChild).$(Parent::getChild).get();
 *  
 * // Regular way
 * Parent grandChild = grandFather != null && grandFather.getChild() != null ? grandFather.getChild().getChild() : null;
 *  
 * </pre>
 * 
 * @author ToYonos
 *
 * @param <T> The wrapped object type
 */
public class SafeNavigationWrapper<T>
{
	private static final SafeNavigationWrapper<?> NULL_WRAPPER = SafeNavigationWrapper.$((Object) null);

	private Optional<T> target;
	
	/**
	 * Constructs an instance with the target object
	 * 	
	 * @param target the target object, possibly null
	 */
	private SafeNavigationWrapper(T target)
	{
		this.target = Optional.ofNullable(target);
	}

	/**
	 * Returns a {@code SafeNavigationWrapper} for the specified target object
	 * 
	 * @param <T> the class of the value
	 * @param target the target object, possibly null
	 * @return a {@code SafeNavigationWrapper} for the target
	 */
	public static <T> SafeNavigationWrapper<T> $(T target)
	{
		return new SafeNavigationWrapper<T>(target);
	}
	
	/**
	 * Apply a {@code Function} on the current target and wrap the result in a {@code SafeNavigationWrapper}
	 * 
	 * @param <R> the class of the expected result
	 * @param getter the {@code Function} which should be applied on the target 
	 * @return a {@code SafeNavigationWrapper} on the result or a null {@code SafeNavigationWrapper} if the target is null
	 */
	@SuppressWarnings("unchecked")
	public <R> SafeNavigationWrapper<R> $(Function<T, R> getter)
	{
		return target.isPresent() ? $(getter.apply(target.get())) : (SafeNavigationWrapper<R>) NULL_WRAPPER;
	}
	
	/**
	 * Get the target of this {@code SafeNavigationWrapper}
	 * 
	 * @return the target or null if not present
	 */
	public T get()
	{
		return get(null);
	}
	
	/**
	 * Get the target of this {@code SafeNavigationWrapper}
	 * 
	 * @param defaultValue the defaultValue if no target if present
	 * @return  the target or defaultValue if not present
	 */
	public T get(T defaultValue)
	{
		return target.orElse(defaultValue);
	}

	/**
	 * Compares the {@code toString} representation of the target to another {@code String}, ignoring case considerations. 
	 * 
	 * @param other The {@code String} to compare with
	 * @return  {@code true} if the argument represents an equivalent {@code String} ignoring case or if both argument and target are null;
	 * {@code false} otherwise
	 */
	public boolean equalsIgnoreCase(String other)
	{
		return stringOperation(s -> s.equalsIgnoreCase(other), other == null);
	}
	
	/**
	 * Compares the {@code toString} representation of the target to another {@code String} 
	 * 
	 * @param other The {@code String} to compare with
	 * @return  {@code true} if the argument represents an equivalent {@code String} or if both argument and target are null;
	 * {@code false} otherwise
	 */
	public boolean equals(String other)
	{
		return stringOperation(s -> s.equals(other), other == null);
	}

	/**
	 * Return {@code true} if the {@code toString} representation is empty or the target is null
	 * 
	 * @see  String#isEmpty()
	 * @return {@code true} if empty or null,  {@code false} otherwise
	 */
	public boolean empty()
	{
		return stringOperation(String::isEmpty, true);
	}
	
	/**
	 * Return {@code true} if the target is not null and the {@code toString} representation is not empty
	 * 
	 * @see  String#isEmpty()
	 * @return {@code true} if not null and not empty,  {@code false} otherwise
	 */
	public boolean notEmpty()
	{
		return !empty();
	}
	
	private boolean stringOperation(Function<String, Boolean> fnc, boolean defaultIfNull)
	{
		return target.map(o -> fnc.apply(o.toString())).orElse(defaultIfNull);
	}
}
