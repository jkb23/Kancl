package online.kancl.page.app;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.JsonArrayBuilder;

import static javax.json.Json.createArrayBuilder;
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
        JsonArrayBuilder usersBuilder = createArrayBuilder();

        for (User user : gridData.getUsers()) {
            usersBuilder.add(user.username);
            usersBuilder.add(user.getX());
        }

        return usersBuilder;
    }

    @Override
    public String post(Request request, Response response) {
        return super.post(request, response);
    }
}
