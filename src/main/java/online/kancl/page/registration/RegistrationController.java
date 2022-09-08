package online.kancl.page.registration;

import online.kancl.auth.Auth;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

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
            return registerUser(request, response, auth, registration);
        });
    }

    String registerUser(Request request, Response response, Auth auth, Registration registration)
    {

        if (!userStorage.usernameExists(registration.username()) && !userStorage.emailExists(registration.email())){
            userStorage.createUser(registration.username(), registration.password(), registration.email());
            var returnCode =  auth.checkCredentialsWithBruteForcePrevention(registration.username(), registration.password());
            RegistrationInfo.setErrorMessage(returnCode.message);
            request.session(true);
            request.session().attribute("user", registration.username());
            response.redirect("/app");
        }
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, RegistrationInfo);
    }
}