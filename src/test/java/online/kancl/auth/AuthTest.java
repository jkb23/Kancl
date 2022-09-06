package online.kancl.auth;

import online.kancl.auth.Auth;
import online.kancl.auth.AuthReturnCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTest {

    @Test
    void credentialsTest() {
        Auth auth = new Auth();
        assertThat(auth.checkCredentialsWithBruteForcePrevention("correct", "correct"))
                .isEqualTo(AuthReturnCode.CORRECT);

        assertThat(auth.checkCredentialsWithBruteForcePrevention("incorrect", "correct"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

        assertThat(auth.checkCredentialsWithBruteForcePrevention("correct", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

        assertThat(auth.checkCredentialsWithBruteForcePrevention("incorrect", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void blockTest() {
        Auth auth = new Auth();

        for (int i = 0; i < 5; i++){
            assertThat(auth.checkCredentialsWithBruteForcePrevention("block", "block"))
                    .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
        }

        assertThat(auth.checkCredentialsWithBruteForcePrevention("block", "block"))
                .isEqualTo(AuthReturnCode.BLOCKED_USER);
    }

}