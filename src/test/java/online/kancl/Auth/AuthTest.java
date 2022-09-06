package online.kancl.Auth;

import online.kancl.auth.Auth;
import online.kancl.page.users.UserStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest {
    @Test
    void check5minTimeout(){
        UserStorage.createUser("ahoj", "");
        assertThat(Auth.isBlocked("ahoj")).isEqualTo(false);
    }
}
