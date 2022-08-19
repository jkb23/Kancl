package online.kancl.server.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

public class PebbleTemplateRenderer
{
	private final PebbleEngine pebbleEngine;

	public PebbleTemplateRenderer(PebbleEngine pebbleEngine)
	{
		this.pebbleEngine = pebbleEngine;
	}

	public String renderTemplate(String templateName, Object context)
	{
		try
		{
			var stringWriter = new StringWriter();

			PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(templateName);
			compiledTemplate.evaluate(stringWriter, Map.of(getContextName(context), context));

			return stringWriter.toString();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String getContextName(Object context)
	{
		String className = context.getClass().getSimpleName();
		if (className.isEmpty())
		{
			throw new IllegalArgumentException("Context can't be an anonymous class.");
		}

		String firstLowercaseLetter = className.substring(0, 1).toLowerCase(Locale.ROOT);
		return firstLowercaseLetter + className.substring(1);
	}
}
