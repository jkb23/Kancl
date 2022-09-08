package online.kancl.objects;

public class OfficeObject {
    private int x;
    private int y;

    public OfficeObject() {
        this.x = 0;
        this.y = 0;
    }

    public OfficeObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveObject(int x, int y){
        this.x = x;
        this.y = y;
    }
}
