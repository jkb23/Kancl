package online.kancl.auth;

import online.kancl.page.users.UserStorage;
import online.kancl.util.HashUtils;

import java.sql.Timestamp;

import static online.kancl.auth.AuthReturnCode.*;

public class Auth {

    public static final int FIVE_MINUTES = 5 * 60 * 1000;

    /**
     * checks whether the user is able to log in and if he's using the correct credentials.
     * If the credentials of a particular user were incorrect 5 times in a row, the user is blocked from logging in
     * for 5 minutes
     * @param username username specified in login
     * @param password password specified in login
     * @return enum describing the return code
     */
    public AuthReturnCode checkCredentials(String username, String password){
        String hashedPassword;

        if(isBlocked(username))
        {
            return BLOCKED_USER;
        }

        hashedPassword = HashUtils.sha256Hash(password);

        if(!UserStorage.findUser(username,hashedPassword))
        {
            UserStorage.setBadLoginCnt(username, UserStorage.getBadLoginCnt(username) + 1);
            if (UserStorage.getBadLoginCnt(username) >= 5)
            {
                blockUser(username);
            }

            return BAD_CREDENTIALS;
        }
        //if the login is successful, clears the bad login count
        UserStorage.setBadLoginCnt(username, 0);

        return CORRECT;
    }

    /**
     * Sets the timestamp of when the user was blocked from logging in
     * @param username user to block
     */
    private static void blockUser(String username)
    {
        UserStorage.setBadLoginTimestamp(username, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Checks if the user were blocked from logging in within the last 5 minutes
     * @param username the user being checked
     * @return true if the user was blocked, false otherwise
     */
    public static boolean isBlocked(String username)
    {
        Timestamp badLogin = UserStorage.getBadLoginTimestamp(username);
        badLogin.setTime(badLogin.getTime() + FIVE_MINUTES);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        return currentTime.before(badLogin);
    }
}
