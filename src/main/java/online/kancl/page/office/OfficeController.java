package online.kancl.page.office;

import online.kancl.objects.*;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.*;
import java.io.StringReader;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.HttpUtil.dontCache;

public class OfficeController extends Controller {
    private final GridData gridData;

    public OfficeController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        dontCache(response);
        return createObjectBuilder()
                .add("objects", createObjectsJsonArray())
                .add("me", (String) request.session().attribute("user"))
                .build()
                .toString();
    }

    @Override
    public String post(Request request, Response response) {
        request.body();
        JsonReader jsonReader = Json.createReader(new StringReader(request.body()));
        JsonObject jsonObject = jsonReader.readObject();
        String type = jsonObject.getString("objectType");

        if (type.equals("user")) {
            String username = jsonObject.getString("username");
            int x = jsonObject.getInt("x");
            int y = jsonObject.getInt("y");
            for (User user : gridData.getUsers()) {
                if (user.username.equals(username)) {
                    user.moveObject(x, y);
                }
            }
        }

        return "";
    }

    private JsonArrayBuilder createObjectsJsonArray() {

        JsonArrayBuilder objects = Json.createArrayBuilder();

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

        for (Wall wall : gridData.getWalls()) {
            JsonObjectBuilder wallBuilder = Json.createObjectBuilder();
            wallBuilder.add("type", "wall");
            wallBuilder.add("x", wall.getX());
            wallBuilder.add("y", wall.getY());
            objects.add(wallBuilder);
        }

        for (MeetingObject meetingObject : gridData.getMeetingObjects()) {
            JsonObjectBuilder meetingObjectBuilder = Json.createObjectBuilder();
            meetingObjectBuilder.add("type", "meeting");
            meetingObjectBuilder.add("link", meetingObject.getMeetingLink());
            meetingObjectBuilder.add("x", meetingObject.getX());
            meetingObjectBuilder.add("y", meetingObject.getY());
            objects.add(meetingObjectBuilder);
        }

        for (CoffeeMachine coffeeMachine : gridData.getCoffeeMachines()) {
            JsonObjectBuilder coffeeMachineBuilder = Json.createObjectBuilder();
            coffeeMachineBuilder.add("type", "coffeeMachine");
            coffeeMachineBuilder.add("x", coffeeMachine.getX());
            coffeeMachineBuilder.add("y", coffeeMachine.getY());
            objects.add(coffeeMachineBuilder);
        }

        return objects;
    }
}
