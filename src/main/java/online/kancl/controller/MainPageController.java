package online.kancl.controller;

import online.kancl.Meetings;
import online.kancl.server.MustacheTemplateRenderer;
import spark.Request;
import spark.Response;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainPageController
{
	public static final Path TEMPLATE_DIRECTORY = Paths.get("web", "templates");

	private final MustacheTemplateRenderer mustacheTemplateRenderer;
	private final Meetings meetings;

	public MainPageController(MustacheTemplateRenderer mustacheTemplateRenderer, Meetings meetings)
	{
		this.mustacheTemplateRenderer = mustacheTemplateRenderer;
		this.meetings = meetings;
	}

	public String get(Request request, Response response)
	{
		return mustacheTemplateRenderer.renderTemplate("MainPage.mustache", meetings);
	}
}
