package online.kancl.auth;

import online.kancl.page.users.UserStorage;

public class Auth {
    public int checkCredentials(String username, String password)
    {
        if(isBlocked(username))
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
                blockUser(username);
            }
            return 1;
        }
    }
}
