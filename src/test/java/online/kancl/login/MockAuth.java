package online.kancl.login;

import online.kancl.auth.AuthReturnCode;
import online.kancl.page.users.UserStorage;
import online.kancl.util.HashUtils;


import java.sql.Timestamp;

import static online.kancl.auth.AuthReturnCode.*;

public class MockAuth {
    public AuthReturnCode checkCredentials(String username, String password){
        if (username == "username" && password == "password") {
            return CORRECT;
        } else if (username == "blocked_username" && password == "password") {
            return BLOCKED_USER;
        } else if (username == username && password == "wrong_password") {
            return BAD_CREDENTIALS;
        }
        assert false;
        return BAD_CREDENTIALS;
    }
}
