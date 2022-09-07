package online.kancl.page.logout;

import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

public class LogoutController extends Controller {
    @Override
    public String get(Request request, Response response){
        request.session().invalidate();
        response.redirect("/login");
        return "";
    }
}
