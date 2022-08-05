package online.kancl.server;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import online.kancl.controller.MainPageController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

public class MustacheTemplateRenderer
{
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
		return MainPageController.TEMPLATE_DIRECTORY.resolve(templateName).toFile();
	}
}
