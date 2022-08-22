package online.kancl.controller;

import online.kancl.model.Meetings;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class ZoomHookController extends Controller {

	private final Meetings meetings;

	public ZoomHookController(Meetings meetings) {
		this.meetings = meetings;
	}

	@Override
	public String get(Request request, Response response) {
		return handleZoomMessage(request);
	}

	@Override
	public String post(Request request, Response response) {
		return handleZoomMessage(request);
	}

	public String handleZoomMessage(Request request) {
		System.out.println(request.body());

		try (JsonReader jsonReader = Json.createReader(new StringReader(request.body()))) {
			handleParticipantJoinedMessage(jsonReader.readObject());
		}

		return "OK";
	}

	private void handleParticipantJoinedMessage(JsonObject message) {
		String participantName = message
				.getJsonObject("payload")
				.getJsonObject("object")
				.getJsonObject("participant")
				.getString("user_name");

		meetings.participantJoined(participantName);
	}
}
