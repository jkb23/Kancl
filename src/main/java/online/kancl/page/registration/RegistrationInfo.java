package online.kancl.page.registration;

public class RegistrationInfo {
    private String errorMessage;

    public RegistrationInfo() {
        this.errorMessage = "";
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addTextToErrorMessage(String errorMessage) {
        this.errorMessage += errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}


