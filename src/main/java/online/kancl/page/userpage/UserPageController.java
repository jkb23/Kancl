package online.kancl.page.userpage;

import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class UserPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private final UserStorage userStorage;
    private final GridData gridData;


    public UserPageController(PebbleTemplateRenderer pebbleTemplateRenderer, UserStorage userStorage, GridData gridData, TransactionJobRunner transactionJobRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.userStorage = userStorage;
        this.gridData = gridData;
        this.transactionJobRunner = transactionJobRunner;
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

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            String newStatus = request.queryParams("status");
            String newProfilePicture = request.queryParams("profile-picture");
            String username = request.session().attribute("user");
            if (!Objects.equals(newStatus, "")) {
                userStorage.setStatusToDb(username, newStatus);
                gridData.updateStatus(username, newStatus);
            }

            if (!Objects.equals(newProfilePicture, "")) {
                // profile pic to db
            }

            response.redirect("/");
            return "";
        });
    }
}