package info.toyonos.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * 
 * TODO javadoc
 * 
 * @author ToYonos
 *
 * @param <T>
 */
public class SafeNavigationWrapper<T>
{
	private static final SafeNavigationWrapper<?> NULL_WRAPPER = SafeNavigationWrapper.$((Object) null);

	private Optional<T> target;
	
	private SafeNavigationWrapper(T target)
	{
		this.target = Optional.ofNullable(target);
	}

	public static <T> SafeNavigationWrapper<T> $(T target)
	{
		return new SafeNavigationWrapper<T>(target);
	}
	
	@SuppressWarnings("unchecked")
	public <R> SafeNavigationWrapper<R> $(Function<T, R> getter)
	{
		return target.isPresent() ? $(getter.apply(target.get())) : (SafeNavigationWrapper<R>) NULL_WRAPPER;
	}
	
	public T get()
	{
		return get(null);
	}
	
	public T get(T defaultValue)
	{
		return target.orElse(defaultValue);
	}

	public boolean equalsIgnoreCase(String other)
	{
		return target.map(o -> o.toString().equalsIgnoreCase(other)).orElse(other == null);
	}

	public boolean empty()
	{
		return target.map(o -> o.toString().isEmpty()).orElse(true);
	}
	
	public boolean notEmpty()
	{
		return !empty();
	}
}
