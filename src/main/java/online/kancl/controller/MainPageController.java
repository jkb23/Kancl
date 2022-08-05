package online.kancl.controller;

import online.kancl.Meetings;
import online.kancl.server.MustacheTemplateRenderer;
import spark.Request;
import spark.Response;

public class MainPageController
{
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
