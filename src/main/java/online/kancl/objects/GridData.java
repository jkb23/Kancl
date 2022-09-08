package online.kancl.objects;

import java.util.ArrayList;
import java.util.List;

public class GridData {
    List<User> users;
    List<Wall> walls;
    List<ZoomObject> zoomObjects;

    public GridData() {
        this.walls = new ArrayList<>();
        this.users = new ArrayList<>();
        this.zoomObjects = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public void addWall(Wall wall){
        walls.add(wall);
    }

    public void addZoom(ZoomObject zoomObject){
        zoomObjects.add(zoomObject);
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

    public List<ZoomObject> getZoomObjects() {
        return zoomObjects;
    }


}
