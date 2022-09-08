package online.kancl.page.registration;

import online.kancl.auth.Auth;
import online.kancl.auth.AuthReturnCode;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.User;
import online.kancl.page.login.Login;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import static online.kancl.auth.AuthReturnCode.CORRECT;

public class RegistrationController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private final RegistrationInfo RegistrationInfo;
    private final UserStorage userStorage;

    public RegistrationController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner,
                                  RegistrationInfo registration, UserStorage userStorage) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
        this.RegistrationInfo = registration;
        this.userStorage = userStorage;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, RegistrationInfo);
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            var auth = new Auth(new UserStorage(dbRunner));
            var registration = new Registration(
                    request.queryParams("username"),
                    request.queryParams("password"),
                    request.queryParams("email"));
            //return authenticate(request, response, auth, user);
            return "";
        });
    }

    private boolean usernameExists(String username){
        return userStorage.usernameExists(username);
    };

    private boolean emailExists(String email){
        return userStorage.emailExists(email);
    };


    String registerUser(Request request, Response response, Auth auth, Login user){
        return "";
    }

    String authenticate(Request request, Response response, Auth auth, Login user) {
        AuthReturnCode returnCode = auth.checkCredentialsWithBruteForcePrevention(user.username(), user.password());
        if (returnCode == CORRECT) {
            User userObject = new User(user.username(), auth);
            request.session(true);
            request.session().attribute("user", user.username());
            response.redirect("/app");
            return "";
        }
        RegistrationInfo.setErrorMessage(returnCode.message);
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, RegistrationInfo);
    }
}
