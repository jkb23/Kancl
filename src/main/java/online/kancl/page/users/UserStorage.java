package online.kancl.page.users;

import java.sql.Timestamp;

public class UserStorage {

    public boolean findUser(String username, String hash) {
        return true;
    }

    public void createUser(String username, String hash) {

    }

    public void setBadLoginCnt(String username, int cnt) {

    }

    public int getBadLoginCnt(String username){
        return 1;
    }

    public void setBadLoginTimestamp(String username, Timestamp timestamp) {

    }

    public Timestamp getBadLoginTimestamp(String username) {
        return null;
    }
}
