package online.kancl.objects;

public class User {
    private int x;
    private int y;
    public final String username;

    public User(String username) {
        this.y = 0;
        this.x = 0;
        this.username = username;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }

    public void moveRight(){
        this.x += 1;
    }

    public void moveLeft(){
        this.x -= 1;
    }

    public void moveUp(){
        this.y -= 1;
    }

    public void moveDown(){
        this.y += 1;
    }
}
