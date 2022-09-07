package online.kancl.page.login;

import mockit.*;
import online.kancl.auth.Auth;
import online.kancl.auth.AuthReturnCode;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import online.kancl.login.MockAuth;
import online.kancl.page.login.LoginController;
import online.kancl.server.template.PebbleTemplateRenderer;
import org.junit.jupiter.api.Test;
import spark.Response;

import javax.xml.crypto.Data;

import static online.kancl.auth.AuthReturnCode.*;
import static online.kancl.loginTestEnum.*;
import static org.assertj.core.api.Assertions.assertThat;


//TODO not finished yet - do mocks
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
    Response response;

    @Injectable
    DatabaseRunner databaseRunner;


    @Test
    void doOperationAbc() {
        new Expectations() {{
            auth.checkCredentialsWithBruteForcePrevention((DatabaseRunner) any, "John Doe", "password");
            result = CORRECT;
        }};

        tested.authenticate(response, databaseRunner, new Login("John Doe", "password" ));

        new Verifications() {{
            response.redirect("/");
            times = 1;
        }};
    }

    @Test
    void CheckIfUserCanLoginWthValidCredentials() {
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(correct_username, correct_password)).isEqualTo(CORRECT);
    }

    @Test
    void CheckIfUserCannotLoginWthValidCredentials() {
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(correct_username, wrong_password)).isEqualTo(BAD_CREDENTIALS);
    }

    @Test
    void CheckIfUserCannotLoginAfterBlock() {
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(blocked_username, correct_password)).isEqualTo(BLOCKED_USER);
    }

}
