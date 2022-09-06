package online.kancl.auth;

import online.kancl.page.users.UserStorage;
import online.kancl.util.HashUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

import static online.kancl.auth.AuthReturnCode.*;

public class Auth {
    public AuthReturnCode checkCredentials(String username, String password){
        String hashedPassword;

        if(isBlocked(username))
        {
            return BLOCKED_USER;
        }

        hashedPassword = HashUtils.sha256Hash(password);

        if(UserStorage.findUser(username,hashedPassword))
        {
            //clears count on successful login
            UserStorage.setBadLoginCnt(username, 0);
            return CORRECT;
        }
        else
        {
            UserStorage.setBadLoginCnt(username, UserStorage.getBadLoginCnt(username) + 1);
            //user gets blocked for a while after failing to log in 5 times
            if (UserStorage.getBadLoginCnt(username) >= 5) {
                blockUser(username);
            }
            return BAD_CREDENTIALS;
        }
    }

    private static void blockUser(String username) {
        Timestamp timestampPlus5min = new Timestamp(System.currentTimeMillis());
        UserStorage.setBadLoginTimestamp(username, timestampPlus5min);
    }

    public static boolean isBlocked(String username){
        boolean blocked;
        Timestamp badLoginTimestamp = UserStorage.getBadLoginTimestamp(username);
        //Get current date, add 300000 ms (5m) and check if the time has passed
        Date date = new Date(System.currentTimeMillis() + 300000);
        int i = badLoginTimestamp.compareTo(date);
        //minulost, 5 min ubehlo.
        blocked = i >= 0;
        return blocked;
    }
}
