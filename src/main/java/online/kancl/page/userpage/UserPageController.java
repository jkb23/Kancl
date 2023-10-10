package online.kancl.page.userpage;

import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import online.kancl.util.HashUtils;
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
            if ("passwordMismatch".equals(request.queryParams("error"))) {
                pageContext.setErrorMessage("Entered new password did not match the current password.");
            }

            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, pageContext);
        }
    }


    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction(dbRunner -> {
            String newStatus = request.queryParams("status");
            String username = request.session().attribute("user");
            String newPassword = HashUtils.sha256Hash(request.queryParams("password-new"));
            String currentPasswordEntered = HashUtils.sha256Hash(request.queryParams("password-current"));
            String currentPasswordFromDatabase = userStorage.getPassword(username);

            if (!Objects.equals(newStatus, "")) {
                userStorage.setStatusToDb(username, newStatus);
                gridData.updateStatus(username, newStatus);
            }

            if (!Objects.equals(newPassword, HashUtils.sha256Hash(""))) {
                if (Objects.equals(currentPasswordEntered, currentPasswordFromDatabase)) {
                    userStorage.setPassword(username, newPassword);
                } else {
                    response.redirect("/user?error=passwordMismatch");
                    return "";
                }
            }

            response.redirect("/");
            return "";
        });
    }
}