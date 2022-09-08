package online.kancl.page.main;

import online.kancl.db.UserStorage;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class MainPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final Meetings meetings;
    private final UserStorage userStorage;
    public MainPageController(PebbleTemplateRenderer pebbleTemplateRenderer, Meetings meetings, UserStorage userStorage) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.meetings = meetings;
        this.userStorage = userStorage;
    }


    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, meetings, new PageContext(request, userStorage));
    }
}
