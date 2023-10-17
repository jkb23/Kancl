package online.kancl.objects;

import java.util.Objects;

public class MeetingObject extends OfficeObject {

    private final String name;

    private String link;
    private String id;

    public MeetingObject(int x, int y, String meetingLink, String meetingName, String id) {
        super(x, y);
        this.link = meetingLink;
        this.name = meetingName;
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeetingObject that = (MeetingObject) o;
        return Objects.equals(link, that.link) && Objects.equals(name, that.name) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), link, name, id);
    }
}
