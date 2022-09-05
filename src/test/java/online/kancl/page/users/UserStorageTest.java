package online.kancl.page.users;

import online.kancl.db.DatabaseRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    private final DatabaseRunner dbRunner;
    private final UserStorage userStorage;

    UserStorageTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
        this.userStorage = new UserStorage();
    }

    public void initialize(DatabaseRunner dbRunner) {
        dbRunner.insert("INSERT INTO User VALUES('john@gmail.com', 12345, null, null, null, null, null)");
    }

    public void findUser_whenNonExistingUser_thenFalse() {
        assertThat(userStorage.findUser(dbRunner, "bhjawbd@dabd.cy", "hgdhgawvd"))
                .isEqualTo(false);
    }

    public void findUser_whenExistingUser_thenTrue() {
        initialize(dbRunner);
        assertThat(userStorage.findUser(dbRunner, "john@gmail.com", "12345"))
                .isEqualTo(true);
    }

}