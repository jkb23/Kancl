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
        for (User user1 : users){
            if (user1.username.equals(user.username)){
                return;
            }
        }
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

    public void addWallsList(List<Wall> startWalls){
        List<Wall> temp = new ArrayList<>();
        temp.addAll(startWalls);
        temp.addAll(this.walls);
        this.walls = temp;
    }

    public List<ZoomObject> getZoomObjects() {
        return zoomObjects;
    }


}
