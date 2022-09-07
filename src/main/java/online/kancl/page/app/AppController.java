package online.kancl.page.app;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import static javax.json.Json.createObjectBuilder;

public class AppController extends Controller {
    private final GridData gridData;


    public AppController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response){
        return createObjectBuilder()
                .add("users", createUsersJsonArray())
                .build()
                .toString();

    }

    private JsonArrayBuilder createUsersJsonArray() {

        JsonArrayBuilder users = Json.createArrayBuilder();

        for (User user : gridData.getUsers()) {
            JsonObjectBuilder userBuilder = Json.createObjectBuilder();
            userBuilder.add("username", user.getUsername());
            userBuilder.add("x", user.getX());
            userBuilder.add("y", user.getY());
            users.add(userBuilder);
        }

        return users;
    }

}
