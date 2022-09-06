package online.kancl.db;

import java.sql.Timestamp;

public class UserStorage {

    public boolean findUser(DatabaseRunner dbRunner, String username, String hash) {
         int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM User WHERE username= ? AND password = ?",
                username, hash);
        return isFound == 1;
    }

    public void createUser(String username, String hash) {

    }

    public void setBadLoginCount(String username, int cnt) {

    }

    public int getBadLoginCount(String username){
        return 1;
    }

    public void setBadLoginTimestamp(String username, Timestamp timestamp) {

    }

    public Timestamp getBadLoginTimestamp(String username) {
        return null;
    }
}
