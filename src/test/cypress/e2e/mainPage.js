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

	it("persists a test field", () => {
		application.openMainPage();
		application.setTestFieldValue('Foo');

		application.openMainPage()
		application.showsTestFieldWithValue('Foo');
	});
});
