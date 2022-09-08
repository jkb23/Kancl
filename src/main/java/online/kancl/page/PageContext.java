package online.kancl.page;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import spark.Request;

public class PageContext {
    private String username = "";

    private UserStorage userStorage;

    public PageContext(Request request) {
        String username = request.session().attribute("user");
        if(username != null) {
            this.username = username;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return userStorage.getStatusFromDb(username);
    }
}
