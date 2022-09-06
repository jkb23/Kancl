package online.kancl.db;

import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class UserStorageTest {

    private final DatabaseRunner dbRunner;
    private final UserStorage userStorage;

    UserStorageTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
        this.userStorage = new UserStorage();
    }

    @BeforeEach
    public void initialize(DatabaseRunner dbRunner) {
        dbRunner.insert("INSERT INTO AppUser (username, password, nickname, avatar, avatar_color, bad_login_count, time_timestamp)" +
                " VALUES('john@gmail.com', 12345, null, null, null, null, null)");
    }

    @Test
    public void findUser_whenNonExistingUser_thenFalse() {
        assertThat(userStorage.findUser(dbRunner, "bhjawbd@dabd.cy", "hgdhgawvd"))
                .isEqualTo(false);
    }

    @Test
    public void findUser_whenExistingUser_thenTrue() {
        assertThat(userStorage.findUser(dbRunner, "john@gmail.com", "12345"))
                .isEqualTo(true);
    }

}