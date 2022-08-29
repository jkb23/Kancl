package online.kancl;

import online.kancl.page.main.Meetings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeetingsTest
{
	private final Meetings meetings = new Meetings();

	@Test
	void initially_meetingIsEmpty()
	{
		assertThat(meetings.getJoinedParticipantName())
				.isEmpty();
	}

	@Test
	void joinedParticipant_isInMeeting()
	{
		meetings.participantJoined("John Doe");

		assertThat(meetings.getJoinedParticipantName())
				.as("Participant name")
				.get()
				.isEqualTo("John Doe");
	}
}
