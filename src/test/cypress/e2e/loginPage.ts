import * as application from '../testrig/application';

describe('Login page', () => {
    beforeEach(() => {
        application.recreateDatabase();
    })

    it("Unsuccessful login", () => {
        application.openLoginPage();
        application.loginUser("correct", "incorrect")
    });

    it("appends a comment", () => {
        application.openCommentPage();
        application.postComment('John Doe', 'Foo');
        application.postComment('Mark Wheel', 'Bar');

        application.showsComment(0, 'John Doe', 'Foo');
        application.showsComment(1, 'Mark Wheel', 'Bar');
    });
});
