package online.kancl.page.login;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import online.kancl.auth.Authenticator;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.server.template.PebbleTemplateRenderer;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

import static online.kancl.auth.AuthReturnCode.BAD_CREDENTIALS;
import static online.kancl.auth.AuthReturnCode.BLOCKED_USER;
import static online.kancl.auth.AuthReturnCode.CORRECT;
import static online.kancl.loginTestEnum.blocked_username;
import static online.kancl.loginTestEnum.correct_password;
import static online.kancl.loginTestEnum.correct_username;
import static online.kancl.loginTestEnum.wrong_password;
import static org.assertj.core.api.Assertions.assertThat;

class LoginTest {

    @Tested
    LoginController tested;

    @Injectable
    @Mocked
    Authenticator authenticator;

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

    @Test
    void checkIfCorrectCredentialsRedirect() {
        new Expectations() {{
            authenticator.checkCredentialsWithBruteForcePrevention(correct_username, correct_password);
            result = CORRECT;
        }};

        tested.authenticate(request, response, authenticator, new Login(correct_username, correct_password));

        List<String> capturedUrls = new ArrayList<>();

        new Verifications() {{
            response.redirect(withCapture(capturedUrls));

            assertThat(capturedUrls.get(0)).matches("https://zoom\\.us/oauth\\S*");
            times = 1;
        }};
    }


    @Test
    void checkIfInCorrectCredentialsRedirect() {
        new Expectations() {{
            authenticator.checkCredentialsWithBruteForcePrevention(correct_username, wrong_password);
            result = BAD_CREDENTIALS;
        }};

        tested.authenticate(request, response, authenticator, new Login(correct_username, wrong_password));

        new Verifications() {{
            loginInfo.setErrorMessage(BAD_CREDENTIALS.message);
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, loginInfo);
            times = 1;
        }};
    }

    @Test
    void checkIfUserIsBlocked() {
        new Expectations() {{
            authenticator.checkCredentialsWithBruteForcePrevention(blocked_username, wrong_password);
            result = BLOCKED_USER;
        }};

        tested.authenticate(request, response, authenticator, new Login(blocked_username, wrong_password));

        new Verifications() {{
            loginInfo.setErrorMessage(BLOCKED_USER.message);
            pebbleTemplateRenderer.renderDefaultControllerTemplate(tested, loginInfo);
            times = 1;
        }};
    }

    private record Matches(String regex) implements Delegate<String> {

        @Mock
        public boolean matches(String arg) {
            return arg.matches(regex);
        }
    }
}
