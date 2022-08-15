import * as application from '../testrig/application';

describe('Comment page', () => {

	it("shows no comments", () => {
		application.openCommentPage();
		application.showsNoComments();
	});

	it("persists a comment", () => {
		application.openCommentPage();
		application.postComment('John Doe', 'Foo');

		application.openCommentPage()
		application.showsComment('John Doe', 'Foo');
	});

	it("appends a comment", () => {
		application.openCommentPage();
		application.postComment('Mark Wheel', 'Bar');

		application.openCommentPage()
		application.showsComment('John Doe', 'Foo');
		application.showsComment('Mark Wheel', 'Bar');
	});
});
