package online.kancl.page.logout;

import online.kancl.objects.GridData;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

public class LogoutController extends Controller {
    private final GridData gridData;

    public LogoutController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response){
        gridData.removeUser(request.session().attribute("user"));
        request.session().invalidate();
        response.redirect("/login");
        return "";
    }
}
