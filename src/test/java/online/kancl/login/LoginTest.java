package online.kancl.login;

import org.junit.jupiter.api.Test;

import static online.kancl.auth.AuthReturnCode.*;
import static online.kancl.loginTestEnum.*;
import static org.assertj.core.api.Assertions.assertThat;

//TODO not finished yet - do mocks
public class LoginTest {
    @Test
    void CheckIfUserCanLoginWthValidCredentials(){
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(correct_username,correct_password)).isEqualTo(CORRECT);
    }

    @Test
    void CheckIfUserCannotLoginWthValidCredentials(){
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(correct_username,wrong_password)).isEqualTo(BAD_CREDENTIALS);
    }

    @Test
    void CheckIfUserCannotLoginAfterBlock(){
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(blocked_username,correct_password)).isEqualTo(BLOCKED_USER);
    }

}
