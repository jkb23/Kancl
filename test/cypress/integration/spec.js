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

	it("reports user's name in meeting without page reload", () => {
		application.openMainPage();
		zoom.joinParticipant('John Doe');
		application.showsConnectedUser('John Doe');
	});
});
