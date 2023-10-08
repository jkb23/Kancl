package online.kancl.objects;

import online.kancl.db.UserStorage;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.JsonFileUtil.createObjectsJsonArray;
import static online.kancl.util.JsonFileUtil.writeFileContents;

public class OfficeObjectsCreator {
    private static final String INITIAL_OFFICE_STATE_FILE_PATH = "src/main/resources/officeLayout.json";

    private final UserStorage userStorage;

    public OfficeObjectsCreator(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private static void createInitialGridTemplate(GridData gridData) {
        gridData.addWallsList(getWallList());
        gridData.addCoffeeMachine(new CoffeeMachine(12, 0));
        gridData.addCoffeeMachine(new CoffeeMachine(13, 0));

        try {
            String jsonObjects = createObjectBuilder()
                    .add("objects", createObjectsJsonArray(gridData))
                    .build()
                    .toString();
            writeFileContents(jsonObjects, INITIAL_OFFICE_STATE_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void create(GridData gridData) {
        if (!loadOfficeObjectsFromFile(gridData)) {
            createInitialGridTemplate(gridData);
        }
    }

    private boolean loadOfficeObjectsFromFile(GridData gridData) {
        try (FileInputStream fis = new FileInputStream(INITIAL_OFFICE_STATE_FILE_PATH)) {

            JsonObject jsonObject;
            try (JsonReader jsonReader = Json.createReader(fis)) {
                jsonObject = jsonReader.readObject();
            }

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
                        String name = object.getString("name");
                        String id = object.getString("id");
                        gridData.addMeeting(new MeetingObject(x, y, link, name, id));
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
}
