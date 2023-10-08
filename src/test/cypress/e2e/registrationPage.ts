import * as application from '../testrig/application';

describe('Registration page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    });

    it("enters correct credentials and submits the registration", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("newUser", "newuser@mail.com", "Password123", "Password123");
        application.clickSubmit();
        application.doesNotDisplayRegistrationPage();
    });

    it("username already exists", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("correct", "newuser@mail.com", "Password123", "Password123");
        application.hasErrorMessage();
        application.displaysRegistrationPage();
    });

    it("email already exists", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("newUser0", "neexistuju@baf.com", "Password123", "Password123");
        application.hasErrorMessage();
        application.displaysRegistrationPage();
    });

    it("enters email in a wrong format", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("newUser1", "notemail", "Password123", "Password123");
        application.clickSubmit();
        application.displaysRegistrationPage();
    });

    it("enters password in a wrong format", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("newUser2", "newuser2@mail.com", "pa", "pa");
        application.clickSubmit();
        application.displaysRegistrationPage();
    });

    it("enters two different passwords", () => {
        application.openRegistrationPage();
        application.enterCredentialsRegistration("newUser2", "newuser2@mail.com", "Password123", "Password1234");
        application.submitButtonIsDisabled();
    });
});
