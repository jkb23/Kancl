package online.kancl.page.edit;

import online.kancl.objects.CoffeeMachine;
import online.kancl.objects.GridData;
import online.kancl.objects.MeetingObject;
import online.kancl.objects.Wall;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.*;
import java.io.*;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.HttpUtil.dontCache;

public class EditOfficeController extends Controller {
    private static final String OFFICE_STATE_FILE_PATH = "src/main/resources/initialOfficeState.json";
    private static final String OFFICE_STATE_FILE_PATH_EDIT = "src/main/resources/initialOfficeStateEdit.json";

    private final GridData gridData;

    public EditOfficeController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        dontCache(response);
        var jsonObjects = createObjectBuilder()
                .add("objects", createObjectsJsonArray())
                .build()
                .toString();

        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(OFFICE_STATE_FILE_PATH_EDIT);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        printWriter.println(jsonObjects);
        printWriter.close();

        return jsonObjects;
    }

    @Override
    public String post(Request request, Response response) {
        request.body();
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(request.body()))) {
            jsonObject = jsonReader.readObject();
        }
        String type = jsonObject.getString("objectType");
        String action = jsonObject.getString("action");
        String link = jsonObject.getString("link");

        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");

        if (type.equals("wall")) {
            Wall wall = new Wall(x, y);

            if (gridData.wallCanBeAdded(wall)) {
                gridData.addWall(wall);
            } else {
                gridData.deleteWall(wall);
            }
        }

        if (type.equals("meeting")) {
            MeetingObject meetingObject = new MeetingObject(x, y, link);

            if (gridData.meetingCanBeAdded(meetingObject)) {
                gridData.addMeeting(meetingObject);
            } else {
                gridData.deleteMeeting(meetingObject);
            }
        }

        if (action.equals("rewrite")) {
            try {
                String contentToUpdate = readFileContents();
                writeFileContents(contentToUpdate);

                return "Update successful!";
            } catch (IOException e) {
                e.printStackTrace();

                return "Update failed due to an error.";
            }
        }

        return "";
    }


    private JsonArrayBuilder createObjectsJsonArray() {

        JsonArrayBuilder objects = Json.createArrayBuilder();

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

    private String readFileContents() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(EditOfficeController.OFFICE_STATE_FILE_PATH_EDIT))) {
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }

        return contentBuilder.toString();
    }

    private void writeFileContents(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EditOfficeController.OFFICE_STATE_FILE_PATH))) {
            writer.write(content);
        }
    }
}
