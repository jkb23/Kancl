package online.kancl.db;

import online.kancl.db.UserStorage.DuplicateUserException;
import online.kancl.test.ProductionDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class UserStorageTest {

    private final DatabaseRunner dbRunner;
    private final UserStorage userStorage;

    UserStorageTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
        this.userStorage = new UserStorage(dbRunner);
    }

    @BeforeEach
    public void initialize(DatabaseRunner dbRunner) {
        dbRunner.insert("INSERT INTO AppUser (username, password, nickname, avatar, avatar_color, bad_login_count, bad_login_timestamp, user_status)" +
                " VALUES('john@gmail.com', 12345, null, null, null, null, null, 'Mam se dobre!')");
    }

    @Test
    public void findUser_whenNonExistingUser_thenFalse() {
        assertThat(userStorage.findUser("bhjawbd@dabd.cy", "hgdhgawvd"))
                .isEqualTo(false);
    }

    @Test
    public void findUser_whenExistingUser_thenTrue() {
        assertThat(userStorage.findUser("john@gmail.com", "12345"))
                .isEqualTo(true);
    }

    @Test
    public void createUser_whenNonExistingUser_thenUserIsCreated() {
        userStorage.createUser("daniel@gmail.com", "11111");
        assertThat(userStorage.findUser("daniel@gmail.com", "11111"))
                .isEqualTo(true);
    }

    @Test
    public void createUser_whenExistingUser_thenExceptionThrown() {
        Assertions.assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userStorage.createUser("john@gmail.com", "12345"));

    }

    @Test
    public void setBadLoginTimestamp_whenTimeStampPassed_thenCorrectTimestampIsRetrievedFromDB() {
        Timestamp actualTimeStamp = new Timestamp(System.currentTimeMillis());
        userStorage.setBadLoginTimestamp("john@gmail.com", actualTimeStamp);
        assertThat(userStorage.getBadLoginTimestamp("john@gmail.com"))
                .contains(actualTimeStamp);
    }

    @Test
    public void incrementBadLoginCount_counter_then_default() {
        assertThat(userStorage.getBadLoginCount("john@gmail.com")).contains(0);
    }

    @Test
    public void incrementBadLoginCount_to_one() {
        userStorage.incrementBadLoginCount("john@gmail.com");
        assertThat(userStorage.getBadLoginCount("john@gmail.com")).contains(1);
    }

    @Test
    public void  incrementBadLoginCount_to_five() {
        for (int i = 0; i < 5; i++) {
            userStorage.incrementBadLoginCount("john@gmail.com");
        }
        assertThat(userStorage.getBadLoginCount("john@gmail.com")).contains(5);
    }

    @Test
    public void nullBadLoginCount_equal_null() {
        userStorage.nullBadLoginCount("john@gmail.com");
        assertThat(userStorage.getBadLoginCount("john@gmail.com")).contains(0);
    }

    @Test
    public void usernameIDIsNotNull() {
        userStorage.getUserIdFromUsername("john@gmail.com");
        assertThat(userStorage.getUserIdFromUsername("john@gmail.com")).isNotEqualTo(null);
    }

    @Test
    public void getStatusFromDb_true() {
        userStorage.getStatusFromDb("john@gmail.com");
        assertThat(userStorage.getStatusFromDb("john@gmail.com")).isEqualTo("Mam se dobre!");
    }

    @Test
    public void getStatusFromDb_false() {
        userStorage.getStatusFromDb("john@gmail.com");
        assertThat(userStorage.getStatusFromDb("john@gmail.com")).isNotEqualTo("Mam se spatne!");
    }

    @Test
    public void setStatusFromDb_true() {
        userStorage.setStatusToDb("john@gmail.com", "Aktualne testuji");
        assertThat(userStorage.getStatusFromDb("john@gmail.com")).isEqualTo("Aktualne testuji");
    }

    @Test
    public void setStatusFromDb_false() {
        userStorage.setStatusToDb("john@gmail.com", "Aktualne testuji");
        assertThat(userStorage.getStatusFromDb("john@gmail.com")).isNotEqualTo("Mam se dobre!");
    }


}