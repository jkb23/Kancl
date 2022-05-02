import * as application from '../testrig/application';
import * as zoom from '../testrig/zoom';

describe('Main page', () => {

	it("reports user in meeting", () => {
		new zoom.Meeting('topic', 1)
			.joinParticipant(10, 'John Doe', 'john@example.com');

		application.openMainPage()
		application.showsConnectedUser('John Doe');
	});
});
