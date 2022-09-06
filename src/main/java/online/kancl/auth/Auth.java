package online.kancl.auth;

import online.kancl.page.users.UserStorage;
import online.kancl.util.HashUtils;

import java.sql.Timestamp;

import static online.kancl.auth.AuthReturnCode.*;

public class Auth {

    public static final int BLOCKED_DURATION_IN_MILLISECONDS = 5 * 60 * 1000;

    public AuthReturnCode checkCredentialsWithBruteForcePrevention(String username, String password) {
        if (isBlocked(username)) {
            return BLOCKED_USER;
        }

        String hashedPassword = HashUtils.sha256Hash(password);
        boolean validCredentials = UserStorage.findUser(username, hashedPassword);

        if (validCredentials) {
            clearBadLoginCount(username);
            return CORRECT;
        } else {
            preventBruteForce(username);
            return BAD_CREDENTIALS;
        }
    }

    private void clearBadLoginCount(String username) {
        UserStorage.setBadLoginCnt(username, 0);
    }

    private void preventBruteForce(String username) {
        int badLoginCount = icrementBadLoginCount(username);
        if (badLoginCount >= 5) {
            blockUser(username);
        }
    }

    private static int icrementBadLoginCount(String username) {
        int badLoginCount = UserStorage.getBadLoginCnt(username) + 1;
        UserStorage.setBadLoginCnt(username, badLoginCount);
        return badLoginCount;
    }

    private void blockUser(String username) {
        UserStorage.setBadLoginTimestamp(username, new Timestamp(System.currentTimeMillis()));
    }

    static boolean isBlocked(String username) {
        Timestamp badLogin = UserStorage.getBadLoginTimestamp(username);
        badLogin.setTime(badLogin.getTime() + BLOCKED_DURATION_IN_MILLISECONDS);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        return currentTime.before(badLogin);
    }
}
