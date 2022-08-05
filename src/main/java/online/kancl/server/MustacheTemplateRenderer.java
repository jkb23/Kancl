package online.kancl.server;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MustacheTemplateRenderer
{
	public static final Path TEMPLATE_DIRECTORY = Paths.get("web", "templates");

	private final MustacheFactory mustacheFactory;

	public MustacheTemplateRenderer(MustacheFactory mustacheFactory)
	{
		this.mustacheFactory = mustacheFactory;
	}

	public String renderTemplate(String templateName, Object context)
	{
		Mustache template = compileTemplate(templateName);
		return template.execute(new StringWriter(), context).toString();
	}

	private Mustache compileTemplate(String templateName)
	{
		try (FileReader fileReader = new FileReader(getTemplateFile(templateName)))
		{
			return mustacheFactory.compile(fileReader, "MainPage");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private File getTemplateFile(String templateName)
	{
		return TEMPLATE_DIRECTORY.resolve(templateName).toFile();
	}
}
