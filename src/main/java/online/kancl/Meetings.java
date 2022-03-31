package online.kancl;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Optional;
import java.util.stream.Collectors;

public class Meetings
{
	private final WebSocketHandler webSocketHandler;
	private final UniqueIndexSet<Meeting> meetings = new UniqueIndexSet<>(meeting -> meeting.uniqueId);

	public Meetings(WebSocketHandler webSocketHandler)
	{
		this.webSocketHandler = webSocketHandler;
		webSocketHandler.setOnMessageHandler(this::onWebSocketMessage);
	}

	public void meetingStarted(Meeting meeting)
	{
		meetings.add(meeting);
		sendUpdate();
	}

	public void meetingEnded(String meetingId)
	{
		meetings.remove(meetingId);
		sendUpdate();
	}

	public void participantJoined(Meeting meeting, Participant participant)
	{
		meetings.addIfAbsent(meeting)
				.add(participant);
		sendUpdate();
	}

	public void participantLeft(String meetingId, String participantId)
	{
		Meeting meeting = meetings.get(meetingId);
		if (meeting != null)
		{
			meeting.remove(participantId);
			sendUpdate();
		}
	}

	private void sendUpdate()
	{
		webSocketHandler.sendToAll(getMeetingsJson().toJson());
	}

	private Optional<String> onWebSocketMessage(String message) {
		if (message.equals("get"))
		{
			return Optional.of(getMeetingsJson().toJson());
		}

		return Optional.empty();
	}

	private JsonArray getMeetingsJson()
	{
		return meetings.getValues().stream()
				.map(this::getMeetingParticipantsJson)
				.collect(Collectors.toCollection(JsonArray::new));
	}

	private JsonObject getMeetingParticipantsJson(Meeting meeting)
	{
		JsonArray participants = meeting.getValues().stream()
				.map(this::getParticipantJson)
				.collect(Collectors.toCollection(JsonArray::new));

		return new JsonObject()
				.putChain("topic", meeting.topic)
				.putChain("participants", participants);
	}

	private JsonObject getParticipantJson(Participant participant)
	{
		return new JsonObject()
				.putChain("userName", participant.userName);
	}
}
