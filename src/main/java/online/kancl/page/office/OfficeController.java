package online.kancl.page.office;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.HttpUtil.dontCache;

public class OfficeController extends Controller {
    private static final String OFFICE_STATE_FILE_PATH = "src/main/resources/officeLayout.json";

    private final GridData gridData;

    public OfficeController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        dontCache(response);
        return createObjectBuilder()
                .add("objects", createObjectsJsonArrayFromFile())
                .add("me", (String) request.session().attribute("user"))
                .build()
                .toString();
    }

    @Override
    public String post(Request request, Response response) {
        request.body();
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(request.body()))) {
            jsonObject = jsonReader.readObject();
        }
        String type = jsonObject.getString("objectType");

        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");

        if (type.equals("user")) {
            String username = jsonObject.getString("username");
            for (User user : gridData.getUsers()) {
                if (user.username.equals(username)) {
                    user.moveObject(x, y);
                }
            }
        }

        return "";
    }

    private JsonArrayBuilder createObjectsJsonArrayFromFile() {
        JsonArrayBuilder objects = Json.createArrayBuilder();

        JsonObject jsonOfficeState = null;
        try (JsonReader reader = Json.createReader(new FileReader(OFFICE_STATE_FILE_PATH))) {
            jsonOfficeState = reader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add objects from jsonOfficeState to the "objects" array
        JsonArray officeObjects = Objects.requireNonNull(jsonOfficeState).getJsonArray("objects");
        for (JsonValue object : officeObjects) {
            objects.add(object);
        }

        // Add users from gridData to the "objects" array
        for (User user : gridData.getUsers()) {
            JsonObjectBuilder userBuilder = Json.createObjectBuilder();
            userBuilder.add("type", "user");
            userBuilder.add("username", user.getUsername());
            userBuilder.add("status", user.getStatus());
            userBuilder.add("avatarBackgroundColor", user.getAvatarBackgroundColor());
            userBuilder.add("x", user.getX());
            userBuilder.add("y", user.getY());
            objects.add(userBuilder);
        }

        return objects;
    }
}
