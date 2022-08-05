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
	private final Path templateDirectory;
	private final MustacheFactory mustacheFactory;

	public MustacheTemplateRenderer(Path templateDirectory, MustacheFactory mustacheFactory)
	{
		this.templateDirectory = templateDirectory;
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
			return mustacheFactory.compile(fileReader, templateName);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private File getTemplateFile(String templateName)
	{
		return templateDirectory.resolve(templateName).toFile();
	}
}
