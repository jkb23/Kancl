package online.kancl.page.login;

public class LoginInfo {
    private String errorMessage;

    public LoginInfo(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
