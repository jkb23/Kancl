package online.kancl;

import com.github.cliftonlabs.json_simple.JsonArray;

import java.util.Optional;

public class Meetings
{
	private final WebSocketHandler webSocketHandler;
	private Optional<String> participantName = Optional.empty();

	public Meetings(WebSocketHandler webSocketHandler)
	{
		this.webSocketHandler = webSocketHandler;
		webSocketHandler.setOnMessageHandler(this::onWebSocketMessage);
	}

	public void participantJoined(String name)
	{
		participantName = Optional.of(name);
		sendUpdate();
	}

	private void sendUpdate()
	{
		webSocketHandler.sendToAll(getParticipantsJson().toJson());
	}

	private Optional<String> onWebSocketMessage(String message) {
		return Optional.of(getParticipantsJson().toJson());
	}

	private JsonArray getParticipantsJson()
	{
		var jsonArray = new JsonArray();
		participantName.ifPresent(jsonArray::add);
		return jsonArray;
	}
}
