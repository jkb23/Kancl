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
        try {
            dbRunner.insert("INSERT INTO AppUser (username, password, nickname, avatar, avatar_color, bad_login_count, time_timestamp)" +
                    " VALUES(?, ?, null, null, null, null, null)", username, hash);
        } catch (DatabaseRunner.DatabaseAccessException e) {
            if (e.sqlErrorCode == 23505) {
                throw new DuplicateUserException(e);
            } else {
                throw e;
            }


        }
    }

    public void setBadLoginCount(String username, int cnt) {

    }

    public int getBadLoginCount(String username){
        return 1;
    }

    public void setBadLoginTimestamp(DatabaseRunner dbRunner, String username, Timestamp timestamp) {

    }

    public Timestamp getBadLoginTimestamp(DatabaseRunner dbRunner, String username) {
        return null;
    }

    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(Throwable cause) {
            super(cause);
        }
    }
}
