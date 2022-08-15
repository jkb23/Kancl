package online.kancl.server;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

class PebbleTemplateRendererTest
{
	public static final String TEST_TEMPLATE_DIRECTORY = "template";

	private final Object context = new Context();

	@Test
	void singleValueTest()
	{
		Assertions.assertThat(renderTemplate("singleValue.peb", context))
				.contains("Hello John Doe!");
	}

	@Test
	void partialValue()
	{
		Assertions.assertThat(renderTemplate("extends.peb", context))
				.isEqualTo("Hello John Doe! How are you, John Doe?");
	}

	private String renderTemplate(String templateName, Object context)
	{
		PebbleTemplateRenderer renderer = createPebbleTemplateRenderer(templateName);
		return renderer.renderTemplate(templateName, context);
	}

	private PebbleTemplateRenderer createPebbleTemplateRenderer(String templateName)
	{
		FileLoader pebbleTemplateLoader = new FileLoader();
		pebbleTemplateLoader.setPrefix(getTemplateDirectory(templateName).toAbsolutePath().toString());
		PebbleEngine pebbleEngine = new PebbleEngine.Builder().loader(pebbleTemplateLoader).build();
		return new PebbleTemplateRenderer(pebbleEngine);
	}

	private Path getTemplateDirectory(String templateName)
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(TEST_TEMPLATE_DIRECTORY + "/" + templateName);

		Assertions.assertThat(resource)
				.as("Template " + templateName + " not found")
				.isNotNull();

		return Path.of(resource.getPath()).getParent();
	}

	private static class Context
	{
		public String name = "John Doe";
	}
}
