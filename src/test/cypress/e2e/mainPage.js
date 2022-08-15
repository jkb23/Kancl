import * as application from '../testrig/application';
import * as zoom from '../testrig/zoom';

describe('Main page', () => {

	it("shows meeting is empty", () => {
		application.openMainPage();
		application.showsEmptyMeeting();
	});

	it("reports user's name in meeting", () => {
		zoom.joinParticipant('John Doe');

		application.openMainPage();
		application.showsConnectedUser('John Doe');
	});

	it("persists a comment", () => {
		application.openMainPage();
		application.postComment('John Doe', 'Foo');

		application.openMainPage()
		application.showsComment('John Doe', 'Foo');
	});
});
