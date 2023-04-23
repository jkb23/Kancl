package online.kancl.objects;

import online.kancl.db.UserStorage;

public class User extends OfficeObject {
    public final String username;
    private final UserStorage userStorage;
    private final String avatarBackgroundColor;

    private String status;

    public User(String username, UserStorage userStorage) {
        super();
        this.username = username;
        this.userStorage = userStorage;
        this.status = userStorage.getStatusFromDb(username);
        this.avatarBackgroundColor = userStorage.getAvatarBackgroundColor(username);
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getAvatarBackgroundColor() {
        return avatarBackgroundColor;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
