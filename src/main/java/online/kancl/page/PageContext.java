package online.kancl.page;

import spark.Request;

public class PageContext {
    private String username = "";

    public PageContext(Request request) {
        String username = request.session().attribute("user");
        if(username != null) {
            this.username = username;
        }
    }

    public String getUsername() {
        return username;
    }
}
