package online.kancl.server;

import com.github.mustachejava.DefaultMustacheFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

class MustacheTemplateRendererTest
{

	public static final String TEST_TEMPLATE_DIRECTORY = "template";

	@Test
	void singleValueTest()
	{
		Object context = new Object()
		{
			String name = "John Doe";
		};

		Assertions.assertThat(renderTemplate("singleValue.mustache", context))
				.isEqualTo("Hello John Doe!");
	}

	private String renderTemplate(String templateName, Object context)
	{
		MustacheTemplateRenderer renderer = new MustacheTemplateRenderer(
				getTemplateDirectory(templateName),
				new DefaultMustacheFactory());

		return renderer.renderTemplate(templateName, context);
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
}
