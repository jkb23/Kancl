package online.kancl.objects;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;

public class User {
    private int x;
    private int y;
    public final int id;
    public final String username;

    public User(DatabaseRunner dbRunner, String username) {
        this.y = 800;
        this.x = 800;
        this.username = username;
        this.id = UserStorage.getUserIdFromUsername(dbRunner, username);
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
