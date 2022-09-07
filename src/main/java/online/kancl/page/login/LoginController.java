package online.kancl.page.login;

import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.page.hello.HelloController;
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
    private LoginInfo loginInfo;

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
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
            var auth = new Auth(new UserStorage(dbRunner));
            AuthReturnCode returnCode = auth.checkCredentialsWithBruteForcePrevention(user.username(), user.password());
            if (returnCode == CORRECT) {
                response.redirect("/");
                request.session(true);
                request.session().attribute("user", user.username());
                return pebbleTemplateRenderer.renderDefaultControllerTemplate(new HelloController(pebbleTemplateRenderer), new Object());
            }
            loginInfo = new LoginInfo(returnCode.message);
            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
        });
    }
}
