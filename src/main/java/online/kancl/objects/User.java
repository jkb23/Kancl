package online.kancl.objects;

import online.kancl.db.UserStorage;

import java.util.Objects;

public class User extends OfficeObject {
    public final String username;
    private final String avatarBackgroundColor;

    private String status;

    public User(String username, UserStorage userStorage) {
        super();
        this.username = username;
        this.status = userStorage.getStatusFromDb(username);
        this.avatarBackgroundColor = userStorage.getAvatarBackgroundColor(username);
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarBackgroundColor() {
        return avatarBackgroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(avatarBackgroundColor, user.avatarBackgroundColor) && Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, avatarBackgroundColor, status);
    }
}
