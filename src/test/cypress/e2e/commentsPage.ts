import * as application from '../testrig/application';

describe('Comments page', () => {

	it("persists a comment", () => {
		application.openCommentPage();
		application.postComment('John Doe', 'Foo');

		application.openCommentPage()
		application.showsComment(0, 'John Doe', 'Foo');
	});

	it("appends a comment", () => {
		application.openCommentPage();
		application.postComment('Mark Wheel', 'Bar');

		application.openCommentPage()
		application.showsComment(0, 'John Doe', 'Foo');
		application.showsComment(1, 'Mark Wheel', 'Bar');
	});
});