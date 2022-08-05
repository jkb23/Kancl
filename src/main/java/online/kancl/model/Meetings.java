package online.kancl.model;

import java.util.Optional;

public class Meetings
{
	private Optional<String> participantName = Optional.empty();

	public void participantJoined(String name)
	{
		participantName = Optional.of(name);
	}

	public Optional<String> getJoinedParticipantName()
	{
		return participantName;
	}
}
