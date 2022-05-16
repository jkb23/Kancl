package online.kancl.controller;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import online.kancl.Meetings;
import spark.Request;
import spark.Response;

import java.io.StringWriter;

public class MainPageController
{
	private final Mustache template;
	private final Meetings meetings;

	public MainPageController(MustacheFactory mustacheFactory, Meetings meetings)
	{
		this.template = mustacheFactory.compile("views/MainPage.mustache");
		this.meetings = meetings;
	}

	public String get(Request request, Response response)
	{
		return template.execute(new StringWriter(), meetings).toString();
	}
}
