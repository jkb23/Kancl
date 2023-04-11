package online.kancl.db;

import online.kancl.util.HashUtils;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Optional;

public class UserStorage {

    private final DatabaseRunner dbRunner;

    public UserStorage(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    public boolean usernameExists(String username) {
        int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM AppUser WHERE username= ?", username);
        return isFound == 1;
    }

    public boolean emailExists(String email) {
        int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM AppUser WHERE email= ?", email);
        return isFound == 1;
    }

    public boolean findUser(String username, String password) {
        String hashedPassword = HashUtils.sha256Hash(password);
        int isFound = dbRunner.selectInt(
                "SELECT COUNT(*) FROM AppUser WHERE username= ? AND password = ?",
                username, hashedPassword);
        return isFound == 1;
    }

    public void createUser(String username, String password, String email) {
        try {
            dbRunner.insert("INSERT INTO AppUser (username, password, email, border_color, user_status)" +
                    " VALUES(?, ?, ?, ?, ?)", username, HashUtils.sha256Hash(password), email, "skyblue", "Hello everyone");
        } catch (DatabaseRunner.DatabaseAccessException e) {
            if (e.sqlErrorCode == 23505) {
                throw new DuplicateUserException(e);
            } else {
                throw e;
            }
        }
    }

    public Optional<Integer> getBadLoginCount(String username) {
        return dbRunner.select("SELECT bad_login_count FROM AppUser WHERE username= ?",
                (r) -> r.getInt(1),
                username);
    }

    public void incrementBadLoginCount(String username) {
        dbRunner.update("UPDATE AppUser SET bad_login_count = nvl(bad_login_count, 0) + 1 WHERE username= ?", username);
    }

    public void nullBadLoginCount(String username) {
        dbRunner.update("UPDATE AppUser SET bad_login_count = 0 WHERE username= ?",
                username);
    }

    public void setBadLoginTimestamp(String username, Timestamp timestamp) {
        dbRunner.update("UPDATE AppUser SET bad_login_timestamp = ? WHERE username= ?",
                timestamp, username);
    }

    public Optional<Timestamp> getBadLoginTimestamp(String username) {
        return dbRunner.select("SELECT bad_login_timestamp FROM AppUser WHERE username= ?",
                (r) -> r.getTimestamp(1),
                username);
    }

    public int getUserIdFromUsername(String username) {
        return dbRunner.selectInt("SELECT id FROM AppUser WHERE username = ?", username);
    }

    public String getStatusFromDb (String username) {
        return dbRunner.selectString("SELECT user_status FROM AppUser WHERE username = ?", username);
    }

    public void setStatusToDb(String username, String status) {
        dbRunner.update("UPDATE AppUser SET user_status = ? WHERE username = ?", status, username);
    }

    public String getBorderColor(String username) {
        return dbRunner.selectString("SELECT border_color FROM AppUser WHERE username = ?", username);
    }

    public class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(Throwable cause) {
            super(cause);
        }
    }
}
