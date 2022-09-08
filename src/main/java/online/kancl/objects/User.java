package online.kancl.objects;

import online.kancl.auth.Auth;

public class User extends OfficeObject {
    private int x;
    private int y;
    public final String username;
    private Auth auth;

    private String status;

    public User(String username, Auth auth) {
        super();
        this.username = username;
        this.auth = auth;
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
