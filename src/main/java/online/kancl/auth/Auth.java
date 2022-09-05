package online.kancl.auth;

public class Auth {
    public int checkCredentials(String username, String password)
    {
        if(isBlocked(username))
        {
            return 2;
        }
        if(checkCredentialsFromDb(username,password))
        {
            setBadLoginCount(username, 0);
            return 0;
        }
        else
        {
            setBadLoginCount(username, 1);
            if (getBadLoginCount(username) >= 5) {
                blockUser(username);
            }
            return 1;
        }
    }
}
