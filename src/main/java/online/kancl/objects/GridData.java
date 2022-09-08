package online.kancl.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void addWallsList(List<Wall> startWalls){
        this.walls = Stream.concat(this.walls.stream(), walls.stream()).collect(Collectors.toList());
    }

    public List<ZoomObject> getZoomObjects() {
        return zoomObjects;
    }


}
