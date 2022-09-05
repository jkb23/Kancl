package online.kancl.page.another;

import online.kancl.page.main.Meetings;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class AnotherController extends Controller
{
    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final Meetings meetings;

    public AnotherController(PebbleTemplateRenderer pebbleTemplateRenderer, Meetings meetings) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.meetings = meetings;
    }


    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, meetings);
    }
}
