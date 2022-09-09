package online.kancl.objects;

import online.kancl.db.UserStorage;

public class User extends OfficeObject {
    private int x;
    private int y;
    public final String username;
    private final UserStorage userStorage;

    private String status;

    public User(String username, UserStorage userStorage) {
        super();
        this.username = username;
        this.userStorage = userStorage;
        this.status = userStorage.getStatusFromDb(username);
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
        userStorage.setStatusToDb(username, status);
    }
}
