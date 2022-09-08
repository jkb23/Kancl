package online.kancl.auth;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import online.kancl.test.ProductionDatabase;
import online.kancl.util.HashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class AuthTest {
    private final DatabaseRunner dbRunner;
    private final Auth auth;
    private final UserStorage userStorage;

    public AuthTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
        this.userStorage = new UserStorage(dbRunner);
        this.auth = new Auth(userStorage);
    }

    @BeforeEach
    void setUp() {
        userStorage.createUser(dbRunner, "username", HashUtils.sha256Hash("password"), "neexistuju@baf.com");
    }

    @Test
    void successfulLogin() {
        assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "password"))
                .isEqualTo(AuthReturnCode.CORRECT);
    }

    @Test
    void unknownUserWithPasswordKnownForDifferentUser() {
        assertThat(auth.checkCredentialsWithBruteForcePrevention("incorrect", "password"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void incorrectPassword() {
        assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void unknownUserWithUnknownPassword() {
        assertThat(auth.checkCredentialsWithBruteForcePrevention("incorrect", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

   @Test
    void blockTest() {
        for (int i = 0; i < 5; i++) {
            assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "block"))
                    .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
        }

        assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "block"))
                .isEqualTo(AuthReturnCode.BLOCKED_USER);
    }
}
