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

    public AuthTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
        this.auth = new Auth(dbRunner);
    }

    @BeforeEach
    void setUp() {
        UserStorage.createUser(dbRunner, "username", HashUtils.sha256Hash("password"));
    }

    @Test
    void credentialsTest() {
        assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "password"))
                .isEqualTo(AuthReturnCode.CORRECT);

        assertThat(auth.checkCredentialsWithBruteForcePrevention("incorrect", "password"))
              .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

        assertThat(auth.checkCredentialsWithBruteForcePrevention("username", "incorrect"))
               .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

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
