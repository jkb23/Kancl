package online.kancl.server.template;

import online.kancl.Main;
import online.kancl.server.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PebbleTemplateRendererTest {
	public static final Path TEST_TEMPLATE_DIRECTORY = Paths.get("src", "test", "resources", "template");

	private final Context context = new Context();
	private PebbleTemplateRenderer renderer;

	@BeforeEach
	void setUp() {
		renderer = Main.createPebbleTemplateRenderer(TEST_TEMPLATE_DIRECTORY);
	}

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

	@Test
	void testDefaultControllerName() {
		String templateName = renderer.getDefaultControllerTemplateName(new TestController());
		assertThat(templateName)
				.isEqualTo("online/kancl/server/template/PebbleTemplateRendererTest/Test.peb");
	}

	private String renderTemplate(String templateName, Object context) {
		return renderer.renderTemplate(templateName, context);
	}

	private static class Context {
		public String name = "John Doe";
		public Optional<String> optional = Optional.empty();
	}

	private static class TestController extends Controller {
	}
}
