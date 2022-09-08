package online.kancl.objects;

public class ZoomObject extends OfficeObject {

    private String zoomLink;
    public ZoomObject(int x, int y, String zoomLink) {
        super(x, y);
        this.zoomLink = zoomLink;
    }
}
