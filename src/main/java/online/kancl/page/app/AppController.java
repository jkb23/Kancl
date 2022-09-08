package online.kancl.page.app;

import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import static javax.json.Json.createObjectBuilder;

public class AppController extends Controller {
    private final GridData gridData;
    private UserStorage userStorage;


    public AppController(GridData gridData, UserStorage userStorage) {

        this.gridData = gridData;
        this.userStorage = userStorage;

    }

    @Override
    public String get(Request request, Response response){
        PageContext pageContext = new PageContext(request, userStorage);
        if ("".equals(pageContext.getUsername())) {
            return createObjectBuilder()
                    .add("users", createUsersJsonArray())
                    .build()
                    .toString();
        } else {
            response.redirect("/login");
            return "";
        }
    }

    private JsonArrayBuilder createUsersJsonArray() {

        JsonArrayBuilder objects = Json.createArrayBuilder();

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
