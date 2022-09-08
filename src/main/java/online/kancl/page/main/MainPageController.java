package online.kancl.page.main;

import online.kancl.page.PageContext;
import online.kancl.page.login.LoginInfo;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class MainPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final Meetings meetings;
    public MainPageController(PebbleTemplateRenderer pebbleTemplateRenderer, Meetings meetings) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.meetings = meetings;

    }


    @Override
    public String get(Request request, Response response) {
        PageContext pageContext = new PageContext(request);
        if ("".equals(pageContext.getUsername())) {
            response.redirect("/login");
            return "";
        } else {
            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, meetings, new PageContext(request));
        }

    }
}
