import * as application from '../testrig/application';

describe('Main page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    })

    it("shows username when logged", () => {
        application.openLoginPage();
        application.enterCredentials('correct', 'correct');
        application.clickSubmit();
        application.openMainPage();
        application.showsLoggedUser('correct');
    });
});
