package online.kancl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MeetingsTest
{
	private final Meetings meetings = new Meetings();

	@Test
	void joinedParticipant_isInMeeting()
	{
		meetings.participantJoined("John Doe");

		Assertions.assertThat(meetings.getJoinedParticipantName())
				.as("Participant name")
				.get()
				.isEqualTo("John Doe");
	}
}