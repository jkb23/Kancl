package online.kancl.page.logout;

import online.kancl.db.TransactionJobRunner;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class LogoutController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;

    public LogoutController(PebbleTemplateRenderer pebbleTemplateRenderer){
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
    }

    @Override
    public String get(Request request, Response response){
        request.session().invalidate();
        response.redirect("/login");
        return "";
    }
}
