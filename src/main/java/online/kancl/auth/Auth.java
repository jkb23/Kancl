package online.kancl.auth;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import online.kancl.util.HashUtils;

import java.sql.Timestamp;
import java.util.Objects;

import static online.kancl.auth.AuthReturnCode.*;


public class Auth {

    public static final int BLOCKED_DURATION_IN_MILLISECONDS = 5 * 60 * 1000;

    public AuthReturnCode checkCredentialsWithBruteForcePrevention(DatabaseRunner dbRunner,
                                                                   String username, String password) {
        if (username == null || password == null){
            return BAD_CREDENTIALS;
        }

        if (isBlocked(dbRunner, username)) {
            return BLOCKED_USER;
        }

        String hashedPassword = HashUtils.sha256Hash(password);
        boolean validCredentials = UserStorage.findUser(dbRunner, username, hashedPassword);

        if (validCredentials) {
            clearBadLoginCount(dbRunner, username);
            return CORRECT;
        } else {
            //preventBruteForce(dbRunner, username);
            return BAD_CREDENTIALS;
        }
    }

    private void clearBadLoginCount(DatabaseRunner dbRunner, String username) {
        UserStorage.nullBadLoginCount(dbRunner, username);
    }

    private void preventBruteForce(DatabaseRunner databaseRunner, String username) {
        UserStorage.incrementBadLoginCount(databaseRunner, username);
        int badLoginCount = UserStorage.getBadLoginCount(databaseRunner, username);
        if (badLoginCount >= 5) {
            blockUser(databaseRunner, username);
        }
    }

    private void blockUser(DatabaseRunner dbRunner, String username) {
        UserStorage.setBadLoginTimestamp(dbRunner, username, new Timestamp(System.currentTimeMillis()));
    }

    private boolean isBlocked(DatabaseRunner dbRunner, String username) {
        if(UserStorage.getBadLoginTimestamp(dbRunner, username) == null)
        {
            return false;
        }
        else
        {
            Timestamp badLogin = UserStorage.getBadLoginTimestamp(dbRunner, username);

            Timestamp check = new Timestamp(badLogin.getTime() + BLOCKED_DURATION_IN_MILLISECONDS);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            return currentTime.before(check);
        }

    }
}
