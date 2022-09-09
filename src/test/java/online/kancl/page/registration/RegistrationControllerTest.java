package online.kancl.page.registration;

import mockit.*;
import online.kancl.auth.Auth;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.server.template.PebbleTemplateRenderer;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import static online.kancl.loginTestEnum.*;

class RegistrationControllerTest {

    @Tested
    RegistrationController tested;

    @Injectable
    @Mocked
    Auth auth;

    @Injectable
    PebbleTemplateRenderer pebbleTemplateRenderer;

    @Injectable
    TransactionJobRunner transactionJobRunner;

    @Injectable
    GridData gridData;

    @Injectable
    UserStorage userStorage;

    @Injectable
    Request request;
    @Injectable
    Response response;

    @Injectable
    RegistrationInfo registrationInfo;

    @Injectable
    DatabaseRunner databaseRunner;

    @Test
    void checkIfCorrectRegistrationRedirect() {
        new Expectations() {{
            userStorage.usernameExists(correct_username);
            result = false;
        }};

        new Expectations() {{
            userStorage.emailExists(email);
            result = false;
        }};

        tested.registerUser(request, response, auth, new Registration(correct_username, correct_password, correct_password, email));
        new Verifications() {{
            response.redirect("/");
            times = 1;
        }};
    }

    @Test
    void checkIfUsedUserNotRedirect() {
        new Expectations() {{
            userStorage.usernameExists(correct_username);
            result = true;
        }};

        new Expectations() {{
            userStorage.emailExists(email);
            result = false;
        }};

        tested.registerUser(request, response, auth, new Registration(correct_username, correct_password, correct_password, email));
        new Verifications() {{
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, registrationInfo);
            times = 1;
        }};
    }

    @Test
    void checkIfUsedEmailNotRedirect() {
        new Expectations() {{
            userStorage.usernameExists(correct_username);
            result = false;
        }};

        new Expectations() {{
            userStorage.emailExists(email);
            result = true;
        }};

        tested.registerUser(request, response, auth, new Registration(correct_username, correct_password, correct_password, email));
        new Verifications() {{
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, registrationInfo);
            times = 1;
        }};
    }

    @Test
    void checkIfNotSamePasswordRedirect() {
        new Expectations() {{
            userStorage.usernameExists(correct_username);
            result = false;
        }};

        new Expectations() {{
            userStorage.emailExists(email);
            result = false;
        }};

        tested.registerUser(request, response, auth, new Registration(correct_username, correct_password, wrong_password, email));
        new Verifications() {{
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, registrationInfo);
            times = 1;
        }};
    }

}
