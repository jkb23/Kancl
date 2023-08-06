package online.kancl.page.registration;

import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class RegistrationController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private final RegistrationInfo registrationInfo;
    private final UserStorage userStorage;
    private final GridData gridData;

    public RegistrationController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner,
                                  RegistrationInfo registration, UserStorage userStorage, GridData gridData) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
        this.registrationInfo = registration;
        this.userStorage = userStorage;
        this.gridData = gridData;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, registrationInfo);
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction(dbRunner -> {
            Registration registration = new Registration(
                    request.queryParams("username"),
                    request.queryParams("password"),
                    request.queryParams("passwordCheck"),
                    request.queryParams("email"));
            return registerUser(request, response, registration);
        });
    }

    String registerUser(Request request, Response response, Registration user) {
        boolean canBeRegistered = true;
        if (!user.password().equals(user.passwordCheck())) {
            registrationInfo.setErrorMessage("Passwords are not same");
            canBeRegistered = false;
        }

        if (userStorage.usernameExists(user.username())) {
            registrationInfo.addTextToErrorMessage("Username exists");
            canBeRegistered = false;
        }

        if (userStorage.emailExists(user.email())) {
            registrationInfo.addTextToErrorMessage("Email exists");
            canBeRegistered = false;
        }

        if (!canBeRegistered) {
            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, registrationInfo);
        }

        userStorage.createUser(user.username(), user.password(), user.email());
        User userObject = new User(user.username(), userStorage);
        gridData.addUser(userObject);
        request.session(true);
        request.session().attribute("user", user.username());
        response.redirect("/");
        return "";
    }
}
