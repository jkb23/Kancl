package online.kancl.objects;

import online.kancl.db.UserStorage;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OfficeObjectsCreator {
    private static final String INITIAL_OFFICE_STATE_FILE_PATH = "src/main/resources/initialOfficeState.json";

    private final UserStorage userStorage;

    public OfficeObjectsCreator(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void create(GridData gridData) {
        if (!loadOfficeObjectsFromFile(gridData)) {
            createInitialGridTemplate(gridData);
        }
    }

    private boolean loadOfficeObjectsFromFile(GridData gridData) {
        try (FileInputStream fis = new FileInputStream(INITIAL_OFFICE_STATE_FILE_PATH)) {
            JsonObject jsonObject = Json.createReader(fis).readObject();
            JsonArray objectsArray = jsonObject.getJsonArray("objects");

            for (JsonValue objectValue : objectsArray) {
                JsonObject object = (JsonObject) objectValue;
                String objectType = object.getString("type");
                int x = object.getInt("x");
                int y = object.getInt("y");

                switch (objectType) {
                    case "user" -> {
                        String username = object.getString("username");
                        gridData.addUser(new User(username, userStorage));
                    }
                    case "wall" -> gridData.addWall(new Wall(x, y));
                    case "meeting" -> {
                        String link = object.getString("link");
                        gridData.addMeeting(new MeetingObject(x, y, link));
                    }
                    case "coffeeMachine" -> gridData.addCoffeeMachine(new CoffeeMachine(x, y));
                    default -> {
                        return false;
                    }
                }
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void createInitialGridTemplate(GridData gridData) {
        List<String> meetingsLinks = addMeetingLinksIfExistent();
        gridData.addWallsList(getWallList());
        gridData.addMeeting(new MeetingObject(25, 0, meetingsLinks.get(0)));
        gridData.addMeeting(new MeetingObject(0, 0, meetingsLinks.get(1)));
        gridData.addMeeting( new MeetingObject(0, 17, meetingsLinks.get(2)));
        gridData.addMeeting(new MeetingObject(25, 17, meetingsLinks.get(3)));
        gridData.addCoffeeMachine(new CoffeeMachine(12, 0));
        gridData.addCoffeeMachine(new CoffeeMachine(13, 0));
    }

    private static List<Wall> getWallList() {
        return Arrays.asList(
                new Wall(0, 4),
                new Wall(1, 4),
                new Wall(2, 4),
                new Wall(3, 4),
                new Wall(6, 3),
                new Wall(6, 4),
                new Wall(6, 0),
                new Wall(6, 1),
                new Wall(6, 2),
                new Wall(7, 4),
                new Wall(8, 4),
                new Wall(9, 4),
                new Wall(10, 4),
                new Wall(19, 0),
                new Wall(19, 1),
                new Wall(19, 2),
                new Wall(19, 4),
                new Wall(19, 3),
                new Wall(22, 4),
                new Wall(23, 4),
                new Wall(24, 4),
                new Wall(25, 4),
                new Wall(18, 4),
                new Wall(17, 4),
                new Wall(16, 4),
                new Wall(15, 4),
                new Wall(0, 13),
                new Wall(1, 13),
                new Wall(2, 13),
                new Wall(3, 13),
                new Wall(6, 14),
                new Wall(6, 13),
                new Wall(6, 15),
                new Wall(6, 16),
                new Wall(6, 17),
                new Wall(19, 14),
                new Wall(19, 15),
                new Wall(19, 16),
                new Wall(19, 17),
                new Wall(19, 13),
                new Wall(22, 13),
                new Wall(23, 13),
                new Wall(24, 13),
                new Wall(25, 13)
        );
    }

    private static List<String> addMeetingLinksIfExistent() {
        List<String> meetingsLinks = new ArrayList<>();
        try {
            File linksFile = new File("meetinglinks.txt");
            Scanner scanner = new Scanner(linksFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                meetingsLinks.add(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
        }

        return meetingsLinks;
    }
}
