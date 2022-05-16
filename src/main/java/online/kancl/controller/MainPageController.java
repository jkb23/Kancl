package online.kancl.controller;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import online.kancl.Meetings;
import spark.Request;
import spark.Response;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;

public class MainPageController
{
	private final MustacheFactory mustacheFactory;
	private final Meetings meetings;

	public MainPageController(MustacheFactory mustacheFactory, Meetings meetings)
	{
		this.mustacheFactory = mustacheFactory;
		this.meetings = meetings;
	}

	public String get(Request request, Response response)
	{
		try (FileReader fileReader = new FileReader(Paths.get("web", "templates", "MainPage.mustache").toFile()))
		{
			Mustache template = mustacheFactory.compile(fileReader, "MainPage");
			return template.execute(new StringWriter(), meetings).toString();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
