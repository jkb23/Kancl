package online.kancl.page.app;

import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class AppController extends Controller {
    private final PebbleTemplateRenderer pebbleTemplateRenderer;


    public AppController(PebbleTemplateRenderer pebbleTemplateRenderer) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
    }

    @Override
    public String get(Request request, Response response){
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, null);
    }

}
