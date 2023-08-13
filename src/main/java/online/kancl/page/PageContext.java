package online.kancl.page;

import online.kancl.db.UserStorage;
import spark.Request;

public class PageContext {
    private String username = "";

    private UserStorage userStorage;

    public PageContext(Request request) {
        String user = request.session().attribute("user");
        if (user != null) {
            this.username = user;
        }
    }

    public PageContext(Request request, UserStorage userStorage) {
        this.userStorage = userStorage;
        String user = request.session().attribute("user");
        if (user != null) {
            this.username = user;
        }
    }

    public String getUsername() {
        return username;
    }

    public String showLogoutWhenLoggedIn() {
        if (username == null) {
            return "";
        } else {
            return "Logout";
        }
    }

    public String getStatus() {
        return userStorage.getStatusFromDb(username);
    }
}
