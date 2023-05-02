package online.kancl.auth;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class AuthenticatorTest {

    private final Authenticator authenticator;
    private final UserStorage userStorage;

    public AuthenticatorTest(DatabaseRunner dbRunner) {
        this.userStorage = new UserStorage(dbRunner);
        this.authenticator = new Authenticator(userStorage);
    }

    @BeforeEach
    void setUp() {
        userStorage.createUser("username", "password", "neexistuju@baf.com");
    }

    @Test
    void successfulLogin() {
        assertThat(authenticator.checkCredentialsWithBruteForcePrevention("username", "password"))
                .isEqualTo(AuthReturnCode.CORRECT);
    }

    @Test
    void unknownUserWithPasswordKnownForDifferentUser() {
        assertThat(authenticator.checkCredentialsWithBruteForcePrevention("incorrect", "password"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void incorrectPassword() {
        assertThat(authenticator.checkCredentialsWithBruteForcePrevention("username", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void unknownUserWithUnknownPassword() {
        assertThat(authenticator.checkCredentialsWithBruteForcePrevention("incorrect", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void blockTest() {
        for (int i = 0; i < 5; i++) {
            assertThat(authenticator.checkCredentialsWithBruteForcePrevention("username", "block"))
                    .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
        }

        assertThat(authenticator.checkCredentialsWithBruteForcePrevention("username", "block"))
                .isEqualTo(AuthReturnCode.BLOCKED_USER);
    }
}
