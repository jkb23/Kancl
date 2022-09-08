import * as application from '../testrig/application';

describe('Login page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    })

    it("enters correct credentials", () => {
        application.openLoginPage();
        application.enterCredentials('correct', 'correct');
        application.clickSubmit();
        application.doesNotShowInvalidCredentials();
        application.doesNotDisplayLoginPage();
    });

    it("enters incorrect username", () => {
        application.openLoginPage();
        application.enterCredentials('incorrect', 'correct');
        application.clickSubmit();
        application.displaysInvalidCredentials();
        application.displaysLoginPage();
    });

    it("enters incorrect password", () => {
        application.openLoginPage();
        application.enterCredentials('correct', 'incorrect');
        application.clickSubmit();
        application.displaysInvalidCredentials();
        application.displaysLoginPage();
    });

    it("leaves username empty", () => {
        application.openLoginPage();
        application.enterCredentials('', 'correct');
        application.submitButtonIsDisabled();
    });

    it("leaves password empty", () => {
        application.openLoginPage();
        application.enterCredentials('user', '');
        application.submitButtonIsDisabled();
        application.displaysLoginPage();
    });

    it("enters incorrect credentials 5 times", () => {
        application.openLoginPage();

        for (let i = 0; i < 6; i++) {
            application.enterCredentials('correct', 'password')
            application.clickSubmit();
        }

        application.displaysTooManyUnsuccessfulLoginAttempts();
        application.displaysLoginPage();
    });
});
