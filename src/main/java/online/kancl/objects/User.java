package online.kancl.objects;

import online.kancl.auth.Auth;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;

public class User extends OfficeObject {
    private int x;
    private int y;
    public final String username;

    private String status;

    public User(String username) {
        super();
        this.username = username;
        this.status = UserStorage.getStatusFromDb(username);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        UserStorage.setStatusToDb(username, status);
    }
}
