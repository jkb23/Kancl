import * as application from '../testrig/application';

describe('recreateDb page', () => {
    it("recreates DB", () => {
        application.openCommentPage();
        application.postComment("John", "message")
        application.recreateDatabase();

        application.openCommentPage();
        application.showsNoComments();
    });
});
