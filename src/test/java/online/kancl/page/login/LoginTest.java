package online.kancl.page.login;

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

import static online.kancl.auth.AuthReturnCode.*;
import static online.kancl.loginTestEnum.*;

public class LoginTest {

    @Tested
    LoginController tested;

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
    LoginInfo loginInfo;

    @Injectable
    DatabaseRunner databaseRunner;

    @Test
    void checkIfCorrectCredentialsRedirect() {
        new Expectations() {{
            auth.checkCredentialsWithBruteForcePrevention(correct_username, correct_password);
            result = CORRECT;
        }};

        tested.authenticate(request, response, auth, new Login(correct_username, correct_password ), databaseRunner);

        new Verifications() {{
            response.redirect("/");
            times = 1;
        }};
    }

    @Test
    void checkIfInCorrectCredentialsRedirect() {
        new Expectations() {{
            auth.checkCredentialsWithBruteForcePrevention(correct_username, wrong_password);
            result = BAD_CREDENTIALS;
        }};

        tested.authenticate(request, response, auth, new Login(correct_username, wrong_password), databaseRunner);

        new Verifications() {{
            loginInfo.setErrorMessage(BAD_CREDENTIALS.message);
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, loginInfo);
            times = 1;
        }};
    }

    @Test
    void checkIfUserIsBlocked() {
        new Expectations() {{
            auth.checkCredentialsWithBruteForcePrevention(blocked_username, wrong_password);
            result = BLOCKED_USER;
        }};

        tested.authenticate(request, response, auth, new Login(blocked_username, wrong_password), databaseRunner);

        new Verifications() {{
            loginInfo.setErrorMessage(BLOCKED_USER.message);
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, loginInfo);
            times = 1;
        }};
    }

}
