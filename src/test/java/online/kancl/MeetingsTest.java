package online.kancl;

import online.kancl.model.Meetings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MeetingsTest
{
	private final Meetings meetings = new Meetings();

	@Test
	void initially_meetingIsEmpty()
	{
		Assertions.assertThat(meetings.getJoinedParticipantName())
				.isEmpty();
	}

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
