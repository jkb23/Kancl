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
		JsonObject message = Jsoner.deserialize(request.body(), new JsonObject());

		System.out.println(request.body());

		String event = message.getStringOrDefault(mintJsonKey("event", ""));
		switch(event)
		{
			case "meeting.started":
				handleMeetingStartedMessage(message);
				return;

			case "meeting.ended":
				handleMeetingEndedMessage(message);
				return;

			case "meeting.participant_joined":
				handleParticipantJoinedMessage(message);
				return;

			case "meeting.participant_left":
				handleParticipantLeftMessage(message);
				return;
		}
	}

	private void handleMeetingStartedMessage(JsonObject message)
	{
		Meeting meeting = createMeetingFromMessage(message);
		if (meeting != null)
			meetings.meetingStarted(meeting);
	}

	private void handleMeetingEndedMessage(JsonObject message)
	{
		String uniqueId = getMeetingId(message);

		if (!uniqueId.isEmpty())
		{
			meetings.meetingEnded(uniqueId);
		}
	}

	private void handleParticipantJoinedMessage(JsonObject message)
	{
		Meeting meeting = createMeetingFromMessage(message);
		Participant participant = createParticipantFromMessage(message);

		if (meeting != null && participant != null)
		{
			meetings.participantJoined(meeting, participant);
		}
	}

	private void handleParticipantLeftMessage(JsonObject message)
	{
		String meetingId = getMeetingId(message);
		JsonObject participantObject = getParticipantObject(message);
		String userId = participantObject.getStringOrDefault(mintJsonKey("user_id", ""));

		if (!meetingId.isEmpty() && !userId.isEmpty())
		{
			meetings.participantLeft(meetingId, userId);
		}
	}

	private Meeting createMeetingFromMessage(JsonObject message)
	{
		JsonObject meetingObject = getPayloadObject(message);

		String uniqueId = meetingObject.getStringOrDefault(mintJsonKey("uuid", ""));
		String topic = meetingObject.getStringOrDefault(mintJsonKey("topic", ""));

		if (!uniqueId.isEmpty())
		{
			return new Meeting(uniqueId, topic);
		}
		else
		{
			return null;
		}
	}

	private Participant createParticipantFromMessage(JsonObject message)
	{
		JsonObject participantObject = getParticipantObject(message);

		String userId = participantObject.getStringOrDefault(mintJsonKey("user_id", ""));
		String userName = participantObject.getStringOrDefault(mintJsonKey("user_name", ""));

		if (!userId.isEmpty() && !userName.isEmpty())
		{
			return new Participant(userId, userName);
		}
		else
		{
			return null;
		}
	}

	private String getMeetingId(JsonObject message)
	{
		return getPayloadObject(message).getStringOrDefault(mintJsonKey("uuid", ""));
	}

	private JsonObject getPayloadObject(JsonObject message)
	{
		JsonObject payload = message.getMapOrDefault(mintJsonKey("payload", new JsonObject()));
		return payload.getMapOrDefault(mintJsonKey("object", new JsonObject()));
	}

	private JsonObject getParticipantObject(JsonObject message)
	{
		JsonObject meetingObject = getPayloadObject(message);
		return meetingObject.getMapOrDefault(mintJsonKey("participant", new JsonObject()));
	}
}
