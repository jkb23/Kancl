package online.kancl.db;

import online.kancl.db.UserStorage.DuplicateUserException;
import online.kancl.test.ProductionDatabase;
import online.kancl.util.HashUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class UserStorageTest {

    private final UserStorage userStorage;

    UserStorageTest(DatabaseRunner dbRunner) {
        this.userStorage = new UserStorage(dbRunner);
    }

    @BeforeEach
    public void initialize(DatabaseRunner dbRunner) {
        dbRunner.insert("INSERT INTO AppUser (username, password, email, user_status)" +
                " VALUES('john', ?, 'john@gmail.com','Mam se dobre!')",
                HashUtils.sha256Hash("12345"));
    }

    @Test
    public void findUser_whenNonExistingUser_thenFalse() {
        assertThat(userStorage.findUser("bhjawbd@dabd.cy", "hgdhgawvd"))
                .isFalse();
    }

    @Test
    public void findUser_whenExistingUser_thenTrue() {
        assertThat(userStorage.findUser("john", "12345"))
                .isTrue();
    }

    @Test
    public void createUser_whenNonExistingUser_thenUserIsCreated() {
        userStorage.createUser("daniel", "11111", "danie@gmail.com");
        assertThat(userStorage.findUser("daniel", "11111"))
                .isTrue();
    }

    @Test
    public void createUser_whenExistingUser_thenExceptionThrown() {
        Assertions.assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userStorage.createUser( "john", "12345", "john@gmail.com"));

    }

    @Test
    public void setBadLoginTimestamp_whenTimeStampPassed_thenCorrectTimestampIsRetrievedFromDB() {
        Timestamp actualTimeStamp = new Timestamp(System.currentTimeMillis());
        userStorage.setBadLoginTimestamp("john", actualTimeStamp);
        assertThat(userStorage.getBadLoginTimestamp("john"))
                .contains(actualTimeStamp);
    }

    @Test
    public void incrementBadLoginCount_counter_then_default() {
        assertThat(userStorage.getBadLoginCount("john")).contains(0);
    }

    @Test
    public void incrementBadLoginCount_to_one() {
        userStorage.incrementBadLoginCount("john");
        assertThat(userStorage.getBadLoginCount("john")).contains(1);
    }

    @Test
    public void  incrementBadLoginCount_to_five() {
        for (int i = 0; i < 5; i++) {
            userStorage.incrementBadLoginCount("john");
        }
        assertThat(userStorage.getBadLoginCount("john")).contains(5);
    }

    @Test
    public void nullBadLoginCount_equal_null() {
        userStorage.nullBadLoginCount("john");
        assertThat(userStorage.getBadLoginCount("john")).contains(0);
    }

    @Test
    public void usernameIDIsNotNull() {
        userStorage.getUserIdFromUsername("john");
        assertThat(userStorage.getUserIdFromUsername("john")).isNotNull();
    }

    @Test
    public void getStatusFromDb_true() {
        userStorage.getStatusFromDb("john");
        assertThat(userStorage.getStatusFromDb("john")).isEqualTo("Mam se dobre!");
    }

    @Test
    public void getStatusFromDb_false() {
        userStorage.getStatusFromDb("john");
        assertThat(userStorage.getStatusFromDb("john")).isNotEqualTo("Mam se spatne!");
    }

    @Test
    public void setStatusFromDb_true() {
        userStorage.setStatusToDb("john", "Aktualne testuji");
        assertThat(userStorage.getStatusFromDb("john")).isEqualTo("Aktualne testuji");
    }

    @Test
    public void setStatusFromDb_false() {
        userStorage.setStatusToDb("john", "Aktualne testuji");
        assertThat(userStorage.getStatusFromDb("john")).isNotEqualTo("Mam se dobre!");
    }


}