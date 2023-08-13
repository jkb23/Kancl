package online.kancl.objects;

import java.util.Objects;

public class MeetingObject extends OfficeObject {

    private final String meetingLink;

    public MeetingObject(int x, int y, String meetingLink) {
        super(x, y);
        this.meetingLink = meetingLink;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeetingObject that = (MeetingObject) o;
        return Objects.equals(meetingLink, that.meetingLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), meetingLink);
    }
}
