package online.kancl.objects;

public class MeetingObject extends OfficeObject {

    private final String meetingLink;
    public MeetingObject(int x, int y, String meetingLink) {
        super(x, y);
        this.meetingLink = meetingLink;
    }

    public String getMeetingLink() {
        return meetingLink;
    }
}
