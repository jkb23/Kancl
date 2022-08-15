package online.kancl.controller;

import online.kancl.model.Meetings;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.sql.Connection;

public class MainPageController extends Controller
{
	private final PebbleTemplateRenderer pebbleTemplateRenderer;
	private final Meetings meetings;

	public MainPageController(PebbleTemplateRenderer pebbleTemplateRenderer, Meetings meetings)
	{
		this.pebbleTemplateRenderer = pebbleTemplateRenderer;
		this.meetings = meetings;
	}

	@Override
	public String get(Request request, Response response, Connection connection)
	{
		return pebbleTemplateRenderer.renderTemplate("MainPage.peb", meetings);
	}
}
