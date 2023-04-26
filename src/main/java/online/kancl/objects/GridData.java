package online.kancl.objects;

import java.util.ArrayList;
import java.util.List;

public class GridData {
    private final List<User> users;
    private List<Wall> walls;
    private final List<MeetingObject> meetingObjects;

    public GridData() {
        this.walls = new ArrayList<>();
        this.users = new ArrayList<>();
        this.meetingObjects = new ArrayList<>();
    }

    public void addUser(User user) {
        for (User user1 : users) {
            if (user1.username.equals(user.username)) {
                return;
            }
        }
        users.add(user);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public void addZoom(MeetingObject meetingObject) {
        meetingObjects.add(meetingObject);
    }

    public void removeUser(String username) {
        users.removeIf(user -> user.username.equals(username));
    }

    public void updateStatus(String username, String newStatus) {
        users.stream()
                .filter(user -> user.getUsername()
                .equals(username))
                .findFirst()
                .ifPresent(user -> user
                .setStatus(newStatus));
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void addWallsList(List<Wall> startWalls) {
        List<Wall> temp = new ArrayList<>();
        temp.addAll(startWalls);
        temp.addAll(this.walls);
        this.walls = temp;
    }

    public List<MeetingObject> getMeetingObjects() {
        return meetingObjects;
    }
}
