package online.kancl.objects;

public class User {
    private int x;
    private int y;
    public final String username;

    public User(String username) {
        this.y = 800;
        this.x = 800;
        this.username = username;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveRight(){
        this.x += 5;
    }

    public void moveLeft(){
        this.x -= 5;
    }

    public void moveUp(){
        this.y -= 5;
    }

    public void moveDown(){
        this.y += 5;
    }
}
