package online.kancl.page.login;

import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
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
    private GridData gridData;

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner,
                           LoginInfo loginInfo, GridData gridData) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
        this.loginInfo = loginInfo;
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        loginInfo = new LoginInfo();
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            var auth = new Auth(new UserStorage(dbRunner));
            var user = new Login(
                    request.queryParams("username"),
                    request.queryParams("password"));
            return authenticate(request, response, auth, user);
        });
    }

    String authenticate(Request request, Response response, Auth auth, Login user) {
        AuthReturnCode returnCode = auth.checkCredentialsWithBruteForcePrevention(user.username(), user.password());
        if (returnCode == CORRECT) {
            String status = UserStorage.getStatus(dbRunner, user.username());
            User userObject = new User(user.username(), status);
            gridData.addUser(userObject);
            request.session(true);
            request.session().attribute("user", user.username());
            response.redirect("/app");
            return "";
        }
        loginInfo.setErrorMessage(returnCode.message);
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }
}
