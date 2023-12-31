package online.kancl.auth;

import online.kancl.db.UserStorage;

import java.sql.Timestamp;
import java.util.Optional;

import static online.kancl.auth.AuthReturnCode.BAD_CREDENTIALS;
import static online.kancl.auth.AuthReturnCode.BLOCKED_USER;
import static online.kancl.auth.AuthReturnCode.CORRECT;

public final class Authenticator {

    public static final int BLOCKED_DURATION_IN_MILLISECONDS = 5 * 60 * 1000;

    private final UserStorage userStorage;

    public Authenticator(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public AuthReturnCode checkCredentialsWithBruteForcePrevention(String username, String password) {
        if (username == null || password == null) {
            return BAD_CREDENTIALS;
        }

        if (isBlocked(username)) {
            return BLOCKED_USER;
        }

        boolean validCredentials = userStorage.findUser(username, password);

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
