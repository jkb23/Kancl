package online.kancl.login;

import online.kancl.auth.AuthReturnCode;
import static online.kancl.auth.AuthReturnCode.*;
import static online.kancl.loginTestEnum.*;


public class MockAuth {
    public AuthReturnCode checkCredentials(String username, String password){
        if (username.equals(correct_username) && password.equals(correct_password)) {
            return CORRECT;
        } else if (username.equals(blocked_username) && password.equals(correct_password)) {
            return BLOCKED_USER;
        } else if (username.equals(correct_username) && password.equals(wrong_password)) {
            return BAD_CREDENTIALS;
        }
        assert false;
        return BAD_CREDENTIALS;
    }
}
