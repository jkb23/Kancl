package online.kancl.controller;

import online.kancl.Meetings;
import spark.Request;
import spark.Response;

public class MainPageController
{
	private final Meetings meetings;

	public MainPageController(Meetings meetings)
	{
		this.meetings = meetings;
	}

	public String get(Request request, Response response)
	{
		return "<!DOCTYPE html>\n"
				+ "<html lang='en'>\n"
				+ "    <head>\n"
				+ "        <meta charset='utf-8'>\n"
				+ "        <title>Kancl.online</title>\n"
				+ "    </head>\n"
				+ "    <body>\n"
				+ "        <pre>" + meetings.getJoinedParticipantName().orElse("No participant") + "</pre>\n"
				+ "    </body>\n"
				+ "</html>\n";
	}
}
