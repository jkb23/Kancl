package online.kancl.server.template;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.extension.core.EmptyTest;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PebbleExtension extends AbstractExtension
{
	@Override
	public Map<String, Filter> getFilters()
	{
		return Map.of("orElse", new OrElseFilter());
	}

	@Override
	public Map<String, Test> getTests()
	{
		return Map.of("emptyOptional", new EmptyOptionalTest());
	}

	private static class OrElseFilter implements Filter
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

	private static class EmptyOptionalTest implements Test
	{
		@Override
		public boolean apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException
		{
			Test emptyTest = new EmptyTest();
			if (emptyTest.apply(input, args, self, context, lineNumber))
			{
				return true;
			}
			else if (input instanceof Optional)
			{
				return ((Optional<?>) input).isEmpty();
			}
			else
			{
				return false;
			}
		}

		@Override
		public List<String> getArgumentNames()
		{
			return null;
		}
	}
}
