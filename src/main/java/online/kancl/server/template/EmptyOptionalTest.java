package online.kancl.server.template;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.extension.core.EmptyTest;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmptyOptionalTest implements Test
{
	@Override
	public boolean apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException
	{
		EmptyTest emptyTest = new EmptyTest();
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
