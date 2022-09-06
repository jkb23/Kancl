import * as application from '../testrig/application';

<<<<<<< HEAD
=======

>>>>>>> e37545e (Created loginPage.ts, added basic functions to application.ts)
describe('Login page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    })

    it("enters correct credentials", () => {
        application.openLoginPage()
        application.enterCredentials('correct', 'correct')
        application.clickSubmit()
        application.checkInvalidCredentialsFalse()
        application.redirect()
    });

    it("enters incorrect username", () => {
        application.openLoginPage()
        application.enterCredentials('incorrect', 'correct')
        application.clickSubmit()
        application.checkNotificationCredentials()
        application.notRedirected()
    });

    it("enters incorrect password", () => {
        application.openLoginPage()
        application.enterCredentials('correct', 'incorrect')
        application.clickSubmit()
        application.checkNotificationCredentials()
        application.notRedirected()
    });

    it("leaves username empty", () => {
        application.openLoginPage()
        application.enterCredentials('', 'correct')
        application.checkVisibility()
        application.notRedirected()
    });

    it("leaves password empty", () => {
        application.openLoginPage()
        application.enterCredentials('user', '')
        application.checkVisibility()
        application.notRedirected()
    });

    it("enters incorrect credentials 5 times", () => {
        application.openLoginPage()

        for(let i = 0; i < 6; i++){
            application.enterCredentials('correct', 'password');
            application.clickSubmit()
        }
        application.checkLockoutCredentials()
        application.notRedirected()
    });

});