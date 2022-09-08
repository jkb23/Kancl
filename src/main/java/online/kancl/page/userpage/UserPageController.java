package online.kancl.page.userpage;

import online.kancl.db.DatabaseRunner;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class UserPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final DatabaseRunner dbRunner;


    public UserPageController(PebbleTemplateRenderer pebbleTemplateRenderer, DatabaseRunner dbRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.dbRunner = dbRunner;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, new PageContext(request));
    }


}