package online.kancl.page.userpage;

import online.kancl.db.UserStorage;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class UserPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final UserStorage userStorage;


    public UserPageController(PebbleTemplateRenderer pebbleTemplateRenderer, UserStorage userStorage) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.userStorage = userStorage;
    }

    @Override
    public String get(Request request, Response response) {
        PageContext pageContext = new PageContext(request, userStorage);
        if ("".equals(pageContext.getUsername())) {
            response.redirect("/login");
            return "";
        } else {
            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, new PageContext(request, userStorage));
        }
    }



}