package online.kancl.server.template;

import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.extension.core.EmptyTest;
import com.mitchellbosecke.pebble.operator.BinaryOperator;
import com.mitchellbosecke.pebble.operator.UnaryOperator;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PebbleExtension implements Extension
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

	@Override
	public Map<String, Function> getFunctions()
	{
		return null;
	}

	@Override
	public List<TokenParser> getTokenParsers()
	{
		return null;
	}

	@Override
	public List<BinaryOperator> getBinaryOperators()
	{
		return null;
	}

	@Override
	public List<UnaryOperator> getUnaryOperators()
	{
		return null;
	}

	@Override
	public Map<String, Object> getGlobalVariables()
	{
		return null;
	}

	@Override
	public List<NodeVisitorFactory> getNodeVisitors()
	{
		return null;
	}

	@Override
	public List<AttributeResolver> getAttributeResolver()
	{
		return null;
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
}
