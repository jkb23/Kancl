package online.kancl.auth;

import online.kancl.page.users.UserStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class Auth {
    public int checkCredentials(String username, String password)
    {
        Timestamp t = UserStorage.getBadLoginTimestamp(username);
        t.setTime(t.getTime() + 5 * 60 * 1000);
        if(t.compareTo(Timestamp.from(Instant.now())) > 0)
        {
            return 2;
        }
        //TODO hash
        if(UserStorage.findUser(username,password))
        {
            UserStorage.setBadLoginCnt(username, 0);
            return 0;
        }
        else
        {
            UserStorage.setBadLoginCnt(username, UserStorage.getBadLoginCnt(username) + 1);
            if (UserStorage.getBadLoginCnt(username) >= 5) {
                UserStorage.setBadLoginTimestamp(username, Timestamp.from(Instant.now()));
            }
            return 1;
        }
    }
}
