package online.kancl.page.api;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.objects.Wall;
import online.kancl.objects.ZoomObject;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javax.json.Json.createObjectBuilder;
import static online.kancl.util.HttpUtil.dontCache;

public class OfficeController extends Controller {
    private final GridData gridData;

    public OfficeController(GridData gridData) {
        this.gridData = gridData;
    }


    @Override
    public String get(Request request, Response response){
        dontCache(response);
        return createObjectBuilder()
                .add("objects", createObjectsJsonArray())
                .build()
                .toString();
    }

    @Override
    public String post (Request request, Response response) {
        request.body();
        JsonReader jsonReader = Json.createReader(new StringReader(request.body()));
        var jsonObject = jsonReader.readObject();
        String type = jsonObject.getString("objectType");
        if (type.equals("user")){
            String username = jsonObject.getString("username");
            int x = jsonObject.getInt("x");
            int y = jsonObject.getInt("y");
            for(User user : gridData.getUsers()) {
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

        for (ZoomObject zoomObject : gridData.getZoomObjects()) {
            JsonObjectBuilder zoomObjectBuilder = Json.createObjectBuilder();
            zoomObjectBuilder.add("type", "zoom");
            zoomObjectBuilder.add("link", zoomObject.getZoomLink());
            zoomObjectBuilder.add("x", zoomObject.getX());
            zoomObjectBuilder.add("y", zoomObject.getY());
            objects.add(zoomObjectBuilder);
        }

        return objects;
    }

}


