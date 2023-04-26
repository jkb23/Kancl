package online.kancl.objects;

public class MeetingObject extends OfficeObject {

    private final String zoomLink;
    public MeetingObject(int x, int y, String zoomLink) {
        super(x, y);
        this.zoomLink = zoomLink;
    }

    public String getMeetingLink() {
        return zoomLink;
    }
}
