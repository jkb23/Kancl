package online.kancl.server.template;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrElseFilter implements Filter
{
	@Override
	public Object apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException
	{
		Object defaultValue = args.get("default");

		Test emptyTest = new EmptyOptionalTest();
		boolean isEmpty = emptyTest.apply(input, args, self, context, lineNumber);

		if (isEmpty)
		{
			return defaultValue;
		}
		else
		{
			if (input instanceof Optional)
			{
				return ((Optional<?>) input).orElse(null);
			}
			else
			{
				return input;
			}
		}
	}

	@Override
	public List<String> getArgumentNames()
	{
		return List.of("default");
	}
}