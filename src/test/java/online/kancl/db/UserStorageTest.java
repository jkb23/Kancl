package online.kancl.db;

import online.kancl.db.UserStorage.DuplicateUserException;
import online.kancl.test.ProductionDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void createUser_whenNonExistingUser_thenUserIsCreated() {
        userStorage.createUser(dbRunner, "daniel@gmail.com", "11111");
        assertThat(userStorage.findUser(dbRunner, "daniel@gmail.com", "11111"))
                .isEqualTo(true);
    }

    @Test
    public void createUser_whenExistingUser_thenExceptionThrown() {
        Assertions.assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userStorage.createUser(dbRunner, "john@gmail.com", "12345"));

    }

    /*
    *@Test
    public void setBadLoginTimestamp_whenWrongOrNoneTimestamp_thenFalse() {
        userStorage.setBadLoginTimestamp(dbRunner, "john@doe.com", new Timestamp());
        assertThat(userStorage.getBadLoginTimestamp(dbRunner, "john@doe.com"))
                .isEqualTo(true);
        }

     */
}