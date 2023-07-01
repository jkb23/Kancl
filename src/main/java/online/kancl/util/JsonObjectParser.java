package online.kancl.util;

import online.kancl.db.UserStorage;
import online.kancl.objects.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JsonObjectParser {

    private UserStorage userStorage;

    public JsonObjectParser(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public GridData createGridDataFromJson(GridData gridData) {
        String jsonFilePath = "C:\\Users\\mjakab\\IdeaProjects\\kancl-online\\src\\main\\java\\online\\kancl\\util\\initialOfficeState.json";
        try (FileInputStream fis = new FileInputStream(new File(jsonFilePath))) {
            JsonObject jsonObject = Json.createReader(fis).readObject();
            JsonArray objectsArray = jsonObject.getJsonArray("objects");

            for (JsonValue objectValue : objectsArray) {
                JsonObject object = (JsonObject) objectValue;
                String objectType = object.getString("type");
                int x = object.getInt("x");
                int y = object.getInt("y");

                switch (objectType) {
                    case "user":
                        String username = object.getString("username");
                        gridData.addUser(new User(username, userStorage));
                        break;
                    case "wall":
                        gridData.addWall(new Wall(x, y));
                        break;
                    case "meeting":
                        String link = object.getString("link");
                        gridData.addMeeting(new MeetingObject(x, y, link));
                        break;
                    case "coffeeMachine":
                        gridData.addCoffeeMachine(new CoffeeMachine(x, y));
                        break;
                    default:
                        // Handle unknown object type
                        break;
                }
            }

            return gridData;
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file read error
        }

        return null; // Return null if unable to create GridData
    }
}

