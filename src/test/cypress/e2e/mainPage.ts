import * as application from '../testrig/application';
import * as zoom from '../testrig/zoom';

describe('Main page', () => {
	beforeEach(() => {
		application.recreateDatabase();
	})

	it("shows meeting is empty", () => {
		application.openMainPage();
		application.showsEmptyMeeting();
	});

	it("reports user's name in meeting", () => {
		zoom.joinParticipant('John Doe');

		application.openMainPage();
		application.showsConnectedUser('John Doe');
	});
});
