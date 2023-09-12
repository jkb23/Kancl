package online.kancl.util;

import online.kancl.objects.CoffeeMachine;
import online.kancl.objects.GridData;
import online.kancl.objects.MeetingObject;
import online.kancl.objects.Wall;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileUtil {
    public static String readFileContents(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }

        return contentBuilder.toString();
    }

    public static void writeFileContents(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    public static JsonArrayBuilder createObjectsJsonArray(GridData gridData) {

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
            meetingObjectBuilder.add("link", meetingObject.getLink());
            meetingObjectBuilder.add("name", meetingObject.getName());
            meetingObjectBuilder.add("id", meetingObject.getId());
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

    private JsonFileUtil() {
        throw new UnsupportedOperationException("JsonFileUtil is a utility class and should not be instantiated");
    }
}
