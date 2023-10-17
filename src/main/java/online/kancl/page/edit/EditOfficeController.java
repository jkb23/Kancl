package online.kancl.page.edit;

import online.kancl.objects.GridData;
import online.kancl.objects.Wall;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.HttpUtil.dontCache;
import static online.kancl.util.JsonFileUtil.createObjectsJsonArray;
import static online.kancl.util.JsonFileUtil.readFileContents;
import static online.kancl.util.JsonFileUtil.writeFileContents;

public class EditOfficeController extends Controller {
    private static final String OFFICE_STATE_FILE_PATH = "src/main/resources/officeLayout.json";
    private static final String OFFICE_STATE_FILE_PATH_EDIT = "src/main/resources/editedOfficeLayout.json";

    private final GridData gridData;

    public EditOfficeController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        dontCache(response);
        String jsonObjects = createObjectBuilder()
                .add("objects", createObjectsJsonArray(gridData))
                .build()
                .toString();

        try (PrintWriter printWriter = new PrintWriter(OFFICE_STATE_FILE_PATH_EDIT)) {
            printWriter.println(jsonObjects);
        } catch (FileNotFoundException e) {
            throw new EditFileException("Failed to write to the office layout edit file.", e);
        }

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

        if (action.equals("rewrite")) {
            try {
                String contentToUpdate = readFileContents(OFFICE_STATE_FILE_PATH_EDIT);
                writeFileContents(contentToUpdate, OFFICE_STATE_FILE_PATH);

                return "Update successful!";
            } catch (IOException e) {
                throw new EditFileException("Failed to rewrite the office layout file.", e);
            }
        }

        return "";
    }

    public static class EditFileException extends RuntimeException {
        public EditFileException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
