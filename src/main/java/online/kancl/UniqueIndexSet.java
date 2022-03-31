package online.kancl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UniqueIndexSet<T>
{
	private final Map<String, T> elements = new HashMap<>();
	private final Function<T, String> getUniqueIndex;

	public UniqueIndexSet(Function<T, String> getUniqueIndexFunction)
	{
		this.getUniqueIndex = getUniqueIndexFunction;
	}

	public void add(T element)
	{
		elements.put(getUniqueIndex.apply(element), element);
	}

	/**
	 * @return Previous element if it existed, newly added element otherwise.
	 */
	public T addIfAbsent(T element)
	{
		T previousElement = elements.putIfAbsent(getUniqueIndex.apply(element), element);
		return previousElement != null ? previousElement : element;
	}

	public void remove(String uniqueIndex)
	{
		elements.remove(uniqueIndex);
	}

	public T get(String uniqueIndex)
	{
		return elements.get(uniqueIndex);
	}

	public Collection<T> getValues()
	{
		return elements.values();
	}
}
