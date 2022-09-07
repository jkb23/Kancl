package online.kancl.page.login;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;
import online.kancl.auth.Auth;
import online.kancl.auth.AuthReturnCode;

import static online.kancl.auth.AuthReturnCode.CORRECT;

public class LoginController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private final LoginInfo loginInfo;
    private final Auth auth;

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner, Auth auth, LoginInfo loginInfo) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
        this.auth = auth;
        this.loginInfo = loginInfo;
    }

    @Override
    public String get(Request request, Response response) {
        loginInfo = new LoginInfo("");
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            var user = new Login(
                    request.queryParams("username"),
                    request.queryParams("password"));
            return authenticate(request, response, dbRunner, user);
        });
    }

    String authenticate(Request request, Response response, DatabaseRunner dbRunner, Login user) {
        AuthReturnCode returnCode = auth.checkCredentialsWithBruteForcePrevention(dbRunner, user.username(), user.password());
        if (returnCode == CORRECT) {
            request.session(true);
            request.session().attribute("user", user.username());
            response.redirect("/app");
            return "";
        }
        loginInfo.setErrorMessage(returnCode.message);
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }
}
