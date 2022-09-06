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

    public AuthTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    @BeforeEach
    void setUp() {
        UserStorage.createUser(dbRunner, "username", HashUtils.sha256Hash("password"));
    }

    @Test
    void credentialsTest() {
        Auth auth = new Auth();

        assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "username", "password"))
                .isEqualTo(AuthReturnCode.CORRECT);

        assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "incorrect", "password"))
              .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

        assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "username", "incorrect"))
               .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);

        assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "incorrect", "incorrect"))
                .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
    }

    @Test
    void blockTest() {
        Auth auth = new Auth();

        for (int i = 0; i < 5; i++){
            assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "block", "block"))
                    .isEqualTo(AuthReturnCode.BAD_CREDENTIALS);
        }

        assertThat(auth.checkCredentialsWithBruteForcePrevention(dbRunner, "block", "block"))
                .isEqualTo(AuthReturnCode.BLOCKED_USER);
    }

}