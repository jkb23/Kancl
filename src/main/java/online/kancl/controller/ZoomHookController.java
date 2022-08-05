package online.kancl.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import online.kancl.Meetings;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import static com.github.cliftonlabs.json_simple.Jsoner.mintJsonKey;

public class ZoomHookController extends Controller
{
	private final Meetings meetings;

	public ZoomHookController(Meetings meetings)
	{
		this.meetings = meetings;
	}

	@Override
	public String get(Request request, Response response)
	{
		return handleZoomMessage(request);
	}

	@Override
	public String post(Request request, Response response)
	{
		return handleZoomMessage(request);
	}

	public String handleZoomMessage(Request request)
	{
		System.out.println(request.body());
		
		JsonObject message = Jsoner.deserialize(request.body(), new JsonObject());
		handleParticipantJoinedMessage(message);

		return "OK";
	}

	private void handleParticipantJoinedMessage(JsonObject message)
	{
		JsonObject payload = message.getMapOrDefault(mintJsonKey("payload", new JsonObject()));
		JsonObject meetingObject = payload.getMapOrDefault(mintJsonKey("object", new JsonObject()));
		JsonObject participantObject = meetingObject.getMapOrDefault(mintJsonKey("participant", new JsonObject()));
		String participantName = participantObject.getStringOrDefault(mintJsonKey("user_name", ""));

		meetings.participantJoined(participantName);
	}
}
