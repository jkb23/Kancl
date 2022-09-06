package online.kancl.db;

import java.sql.Timestamp;

public class UserStorage {

    public static boolean findUser(DatabaseRunner dbRunner, String username, String hash) {
         int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM AppUser WHERE username= ? AND password = ?",
                username, hash);
        return isFound == 1;
    }

    public static void createUser(DatabaseRunner dbRunner, String username, String hash) {
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

    public static int getBadLoginCount(DatabaseRunner dbRunner, String username) {
        return dbRunner.selectInt("SELECT bad_login_count FROM AppUser WHERE username= ?",
                username);
    }

    public static void incrementBadLoginCount(DatabaseRunner dbRunner, String username) {
        int BadLoginCount = dbRunner.selectInt("SELECT bad_login_count FROM AppUser WHERE username= ?",
                username);
        dbRunner.update("UPDATE AppUser SET bad_login_count = ? WHERE username= ?",
                BadLoginCount + 1, username);
    }

    public static void nullBadLoginCount(DatabaseRunner dbRunner, String username) {
        dbRunner.update("UPDATE AppUser SET bad_login_count = 0 WHERE username= ?",
                username);
    }

    public static void setBadLoginTimestamp(DatabaseRunner dbRunner, String username, Timestamp timestamp) {

    }

    public static Timestamp getBadLoginTimestamp(DatabaseRunner dbRunner, String username) {
        return null;
    }

    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(Throwable cause) {
            super(cause);
        }
    }
}
