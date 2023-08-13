package online.kancl.objects;

import java.util.Objects;

public class OfficeObject {
    private int x;
    private int y;

    public OfficeObject() {
        this.x = 13;
        this.y = 8;
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

    public void moveObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeObject that = (OfficeObject) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
