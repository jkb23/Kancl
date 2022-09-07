package online.kancl.objects;

import java.util.ArrayList;
import java.util.List;

public class GridData {
    List<User> users;
    List<Wall> walls;

    List<OfficeObject> officeObjects;

    public GridData() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public void addWall(Wall wall){
        walls.add(wall);
    }

    public void removeUser(String username){
        users.removeIf(user -> user.username.equals(username));
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Wall> getWalls()
    {
        return walls;
    }

//    public  List<OfficeObject> getOfficeObjects()
//    {
//        return officeObjects;
//    }
}
