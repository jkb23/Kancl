package online.kancl.auth;

public enum AuthReturnCode {
    CORRECT("Login successful"),
    BAD_CREDENTIALS("Invalid credentials"),
    BLOCKED_USER("Too many unsuccessful attempts. Try again later.");

    public final String message;

    AuthReturnCode(String message) {
        this.message = message;
    }
}
