package online.kancl.db;

import online.kancl.util.HashUtils;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;

public class UserStorage {
    private static final String[] AVATAR_BACKGROUND_COLORS = {"#ffe6e6", "#ffcccc", "#ffb3b3", "#ff9999", "#ff8080", "#ff6666", "#ff4d4d",
            "#ff3333", "#ff1a1a", "#ff0000", "#ffffe6", "#ffffcc", "#ffffb3", "#ffff99", "#ffff80", "#ffff66", "#ffff4d",
            "#ffff33", "#ffff1a", "#ffff00", "#e6ffe6", "#ccffcc", "#b3ffb3", "#99ff99", "#80ff80", "#66ff66", "#4dff4d",
            "#33ff33", "#1aff1a", "#00ff00", "#e6ffff", "#ccffff", "#b3ffff", "#99ffff", "#80ffff", "#66ffff", "#4dffff",
            "#33ffff", "#1affff", "#00ffff", "#e6e6ff", "#ccccff", "#b3b3ff", "#9999ff", "#8080ff", "#6666ff", "#4d4dff",
            "#3333ff", "#1a1aff", "#0000ff"};

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
        Random rand = new Random();
        try {
            dbRunner.insert("INSERT INTO AppUser (username, password, email, bg_color, user_status)" +
                            " VALUES(?, ?, ?, ?, ?)", username, HashUtils.sha256Hash(password), email,
                    AVATAR_BACKGROUND_COLORS[rand.nextInt(AVATAR_BACKGROUND_COLORS.length)], "Hello everyone");
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
                r -> r.getInt(1),
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
                r -> r.getTimestamp(1), username);
    }

    public int getUserIdFromUsername(String username) {
        return dbRunner.selectInt("SELECT id FROM AppUser WHERE username = ?", username);
    }

    public String getStatusFromDb(String username) {
        return dbRunner.selectString("SELECT user_status FROM AppUser WHERE username = ?", username);
    }

    public void setStatusToDb(String username, String status) {
        dbRunner.update("UPDATE AppUser SET user_status = ? WHERE username = ?", status, username);
    }

    public String getAvatarBackgroundColor(String username) {
        return dbRunner.selectString("SELECT bg_color FROM AppUser WHERE username = ?", username);
    }


    public String getPassword(String username) {
        return dbRunner.selectString("SELECT password FROM AppUser WHERE username = ?", username);
    }

    public void setPassword(String username, String password) {
        dbRunner.update("UPDATE AppUser SET password = ? WHERE username = ?", password, username);
    }

    public static class DuplicateUserException extends RuntimeException {
        public DuplicateUserException(Throwable cause) {
            super(cause);
        }
    }
}
