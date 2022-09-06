package online.kancl.login;

import online.kancl.login.MockAuth;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest {
    @Test
    void check5minTimeout(){
        var mockAuth = new MockAuth();
        assertThat(mockAuth.checkCredentials(""))).isEqualTo(false);
    }
}
