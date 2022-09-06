package online.kancl.db;

import java.sql.Timestamp;

public class UserStorage {

    public boolean findUser(DatabaseRunner dbRunner, String username, String hash) {
         int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM AppUser WHERE username= ? AND password = ?",
                username, hash);
        return isFound == 1;
    }

    public void createUser(DatabaseRunner dbRunner, String username, String hash) {

    }

    public int getBadLoginCount(DatabaseRunner dbRunner, String username) {
        return dbRunner.selectInt("SELECT bad_login_count FROM AppUser WHERE username= ?",
                username);
    }

    public void incrementBadLoginCount(DatabaseRunner dbRunner, String username) {
        int BadLoginCount = dbRunner.selectInt("SELECT bad_login_count FROM AppUser WHERE username= ?",
                username);
        dbRunner.update("UPDATE AppUser SET bad_login_count = ? WHERE username= ?",
                BadLoginCount + 1, username);
    }

    public void nullBadLoginCount(DatabaseRunner dbRunner, String username) {
        dbRunner.update("UPDATE AppUser SET bad_login_count = 0 WHERE username= ?",
                username);
    }

    public void setBadLoginTimestamp(DatabaseRunner dbRunner, String username, Timestamp timestamp) {

    }

    public Timestamp getBadLoginTimestamp(DatabaseRunner dbRunner, String username) {
        return null;
    }
}
