package online.kancl;

public class Meeting extends UniqueIndexSet<Participant>
{
	public final String uniqueId;
	public final String topic;

	public Meeting(String uniqueId, String topic)
	{
		super((p) -> p.userId);
		this.uniqueId = uniqueId;
		this.topic = topic;
	}
}
