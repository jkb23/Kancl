import * as application from '../testrig/application';

describe('Login page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    })

    it("enters correct credentials", () => {
        application.openLoginPage();
        application.enterCredentials('correct', 'correct');
        application.clickSubmit()

    });

    it("enters incorrect username", () => {
        application.openLoginPage();
        application.enterCredentials('incorrect', 'correct');
        application.clickSubmit()
        application.checkNotificationCredentials()

    });

    it("enters incorrect password", () => {
        application.openLoginPage();
        application.enterCredentials('correct', 'incorrect');
        application.clickSubmit()
        application.checkNotificationCredentials()
    });

    it("leaves username empty", () => {
        application.openLoginPage();
        application.enterCredentials('', 'correct');
        application.checkVisibility()
    });

    it("leaves password empty", () => {
        application.openLoginPage();
        application.enterCredentials('user', '');
        application.checkVisibility()
    });

    it("enters incorrect credentials 5 times", () => {
        application.openLoginPage();

        for(let i = 0; i < 5; i++){
            application.enterCredentials('user', 'password');
            application.clickSubmit()
        }

        application.checkLockoutCredentialsTrue()
    });

    it("check if user can sign in after 5 minute lock-out ", () => {
        application.openLoginPage();

        for(let i = 0; i < 5; i++){
            application.enterCredentials('user', 'password');
            application.clickSubmit()
        }

        application.checkLockoutCredentialsTrue()
        application.timelapse()
//         application.checkLockoutCredentialsFalse()
// FIXME

    });

});