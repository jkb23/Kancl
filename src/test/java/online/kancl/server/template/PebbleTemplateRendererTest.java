package online.kancl.server.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static online.kancl.testutil.ResourcePathResolver.getResourcePath;
import static org.assertj.core.api.Assertions.assertThat;

class PebbleTemplateRendererTest {
	public static final String TEST_TEMPLATE_DIRECTORY = "template";

	private final Context context = new Context();

	@Test
	void singleValueTest() {
		assertThat(renderTemplate("singleValue.peb", context))
				.contains("Hello John Doe!");
	}

	@Test
	void partialValue() {
		assertThat(renderTemplate("extends.peb", context))
				.isEqualTo("Hello John Doe! How are you, John Doe?");
	}

	@Test
	void emptyOptionalTest() {
		assertThat(renderTemplate("optional.peb", context))
				.isEqualTo("default value");
	}

	@Test
	void filledOptionalTest() {
		context.optional = Optional.of("Foo");

		assertThat(renderTemplate("optional.peb", context))
				.isEqualTo("Foo");
	}

	private String renderTemplate(String templateName, Object context) {
		PebbleTemplateRenderer renderer = createPebbleTemplateRenderer();
		return renderer.renderTemplate(templateName, context);
	}

	private PebbleTemplateRenderer createPebbleTemplateRenderer() {
		var pebbleTemplateLoader = new FileLoader();
		pebbleTemplateLoader.setPrefix(getResourcePath(this, TEST_TEMPLATE_DIRECTORY).toAbsolutePath().toString());
		var pebbleEngine = new PebbleEngine.Builder()
				.loader(pebbleTemplateLoader)
				.extension(new PebbleExtension())
				.build();
		return new PebbleTemplateRenderer(pebbleEngine);
	}

	private static class Context {
		public String name = "John Doe";
		public Optional<String> optional = Optional.empty();
	}
}
