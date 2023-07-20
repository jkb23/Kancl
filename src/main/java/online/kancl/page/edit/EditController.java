package online.kancl.page.edit;

import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class EditController extends Controller {
    private final PebbleTemplateRenderer pebbleTemplateRenderer;

    public EditController(PebbleTemplateRenderer pebbleTemplateRenderer) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, new PageContext(request));
    }
}
