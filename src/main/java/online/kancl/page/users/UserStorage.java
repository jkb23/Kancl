package online.kancl.page.users;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.RowMapper;

import java.sql.Timestamp;

public class UserStorage {

    public boolean findUser(DatabaseRunner dbRunner, String username, String hash) {
        return dbRunner.selectInt(
                "SELECT COUNT(*) FROM User WHERE username= ? AND password = ?",
                username, hash);
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
