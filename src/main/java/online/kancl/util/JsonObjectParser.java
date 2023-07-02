package online.kancl.util;

import online.kancl.db.UserStorage;
import online.kancl.objects.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.FileInputStream;
import java.io.IOException;

public class JsonObjectParser {

    private final UserStorage userStorage;
    private static final String INITIAL_OFFICE_STATE_FILE_PATH = "C:\\Users\\mjakab\\IdeaProjects\\kancl-online\\src\\main\\java\\online\\kancl\\util\\initialOfficeState.json";

    public JsonObjectParser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean createGridDataFromJson(GridData gridData) {
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
}

