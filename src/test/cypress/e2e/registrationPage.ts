import * as application from '../testrig/application';

describe('Registration page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    });

    it("enters correct credentials and submits the registration", () => {
            application.openRegistrationPage();
            application.enterCredentialsRegistration("newUser", "newuser@mail.com", "Password123", "Password123");
            application.clickSubmit();
//             application.doesNotShowInvalidCredentials();
            application.doesNotDisplayRegistrationPage();
        });

    it("enters email in a wrong format", () => {
            application.openRegistrationPage();
            application.enterCredentialsRegistration("newUser1", "notemail", "Password123", "Password123");
            application.clickSubmit();
            application.displaysRegistrationPage();
        });

    it("enters password in a wrong format", () => {
            application.openRegistrationPage();
            application.enterCredentialsRegistration("newUser2", "newuser2@mail.com", "pa", "Password123");
            application.clickSubmit();
            application.displaysRegistrationPage();
        });


});
