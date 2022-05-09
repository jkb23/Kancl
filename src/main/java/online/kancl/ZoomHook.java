package online.kancl;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import spark.Request;

import static com.github.cliftonlabs.json_simple.Jsoner.mintJsonKey;

public class ZoomHook
{
	private final Meetings meetings;

	public ZoomHook(Meetings meetings)
	{
		this.meetings = meetings;
	}

	public void handleZoomMessage(Request request)
	{
		System.out.println(request.body());
		
		JsonObject message = Jsoner.deserialize(request.body(), new JsonObject());
		handleParticipantJoinedMessage(message);
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
