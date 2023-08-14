package online.kancl.objects;

import java.util.ArrayList;
import java.util.List;

public class GridData {
    private final List<User> users;
    private List<Wall> walls;
    private final List<MeetingObject> meetingObjects;
    private final List<CoffeeMachine> coffeeMachines;

    public GridData() {
        this.walls = new ArrayList<>();
        this.users = new ArrayList<>();
        this.meetingObjects = new ArrayList<>();
        this.coffeeMachines = new ArrayList<>();
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

    public void deleteWall(Wall wall) {
        walls.remove(wall);
    }

    public void deleteMeeting(MeetingObject meetingObject) {
        meetingObjects.remove(meetingObject);
    }

    public void addMeeting(MeetingObject meetingObject) {
        meetingObjects.add(meetingObject);
    }

    public void addCoffeeMachine(CoffeeMachine coffeeMachine) {
        coffeeMachines.add(coffeeMachine);
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

    public List<CoffeeMachine> getCoffeeMachines() {
        return coffeeMachines;
    }

    public boolean wallCanBeAdded(Wall newWall) {
        for (Wall oldWall : walls) {
            if (oldWall.equals(newWall)) {
                return false;
            }
        }
        return true;
    }

    public boolean meetingCanBeAdded(MeetingObject newMeetingObject) {
        for (MeetingObject oldMeeting : meetingObjects) {
            if (oldMeeting.equals(newMeetingObject)) {
                return false;
            }
        }
        return true;
    }
}
