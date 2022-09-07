package online.kancl.auth;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import online.kancl.util.HashUtils;

import java.sql.Timestamp;
import java.util.Optional;

import static online.kancl.auth.AuthReturnCode.*;


public class Auth {

    public static final int BLOCKED_DURATION_IN_MILLISECONDS = 5 * 60 * 1000;
    private UserStorage userStorage;

    public AuthReturnCode checkCredentialsWithBruteForcePrevention(DatabaseRunner dbRunner,
                                                                   String username, String password) {

        userStorage = new UserStorage(dbRunner);

        if (username == null || password == null) {
            return BAD_CREDENTIALS;
        }

        if (isBlocked(dbRunner, username)) {
            return BLOCKED_USER;
        }

        String hashedPassword = HashUtils.sha256Hash(password);
        boolean validCredentials = userStorage.findUser(dbRunner, username, hashedPassword);

        if (validCredentials) {
            clearBadLoginCount(dbRunner, username);
            return CORRECT;
        } else {
            preventBruteForce(dbRunner, username);
            return BAD_CREDENTIALS;
        }
    }

    private void clearBadLoginCount(DatabaseRunner dbRunner, String username) {
        userStorage.nullBadLoginCount(dbRunner, username);
    }

    private void preventBruteForce(DatabaseRunner databaseRunner, String username) {
        userStorage.incrementBadLoginCount(databaseRunner, username);
        Optional<Integer> badLoginCount = userStorage.getBadLoginCount(databaseRunner, username);
        if (badLoginCount.isPresent() && badLoginCount.get() >= 5) {
            blockUser(databaseRunner, username);
        }
    }

    private void blockUser(DatabaseRunner dbRunner, String username) {
        userStorage.setBadLoginTimestamp(dbRunner, username, new Timestamp(System.currentTimeMillis()));
    }

    private boolean isBlocked(DatabaseRunner dbRunner, String username) {
        Optional<Timestamp> badLoginTimestamp = userStorage.getBadLoginTimestamp(dbRunner, username);

        if (badLoginTimestamp.isEmpty()) {
            return false;
        } else {
            Timestamp check = new Timestamp(badLoginTimestamp.get().getTime() + BLOCKED_DURATION_IN_MILLISECONDS);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            return currentTime.before(check);
        }

    }
}
