package online.kancl.auth;

import online.kancl.db.UserStorage;
import online.kancl.util.HashUtils;

import java.sql.Timestamp;
import java.util.Optional;

import static online.kancl.auth.AuthReturnCode.*;


public class Auth {

    public static final int BLOCKED_DURATION_IN_MILLISECONDS = 5 * 60 * 1000;
    private final UserStorage userStorage;

    public Auth(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public AuthReturnCode checkCredentialsWithBruteForcePrevention(String username, String password) {
        if (username == null || password == null) {
            return BAD_CREDENTIALS;
        }

        if (isBlocked(username)) {
            return BLOCKED_USER;
        }

        String hashedPassword = HashUtils.sha256Hash(password);
        boolean validCredentials = userStorage.findUser(username, hashedPassword);

        if (validCredentials) {
            clearBadLoginCount(username);
            return CORRECT;
        } else {
            preventBruteForce(username);
            return BAD_CREDENTIALS;
        }
    }

    private void clearBadLoginCount(String username) {
        userStorage.nullBadLoginCount(username);
    }

    private void preventBruteForce(String username) {
        userStorage.incrementBadLoginCount(username);
        Optional<Integer> badLoginCount = userStorage.getBadLoginCount(username);
        if (badLoginCount.isPresent() && badLoginCount.get() >= 5) {
            blockUser(username);
        }
    }

    private void blockUser(String username) {
        userStorage.setBadLoginTimestamp(username, new Timestamp(System.currentTimeMillis()));
    }

    private boolean isBlocked(String username) {
        Optional<Timestamp> badLoginTimestamp = userStorage.getBadLoginTimestamp(username);

        if (badLoginTimestamp.isEmpty()) {
            return false;
        } else {
            Timestamp check = new Timestamp(badLoginTimestamp.get().getTime() + BLOCKED_DURATION_IN_MILLISECONDS);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            return currentTime.before(check);
        }
    }

}
