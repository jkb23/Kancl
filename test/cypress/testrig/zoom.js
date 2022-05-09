const baseUrl = Cypress.config('baseUrl');
const zoomVerificationToken = Cypress.env('ZOOM_VERIFICATION_TOKEN');

// https://marketplace.zoom.us/docs/api-reference/zoom-api/events/#/paths/meeting.participant_joined/post
export function joinParticipant(name) {
	sendZoomWebHookRequest({
		"event": "meeting.participant_joined",
		"event_ts": 10,
		"payload": {
			"account_id": "accountId3",
			"object": {
				"id": "idOfTheMeeting10",
				"uuid": "uuidOfTheMeeting10",
				"host_id": "meetingHostId7",
				"topic": "Meeting topic",
				"type": 1,
				"start_time": "2022-05-02T16:25:10Z",
				"timezone": "Europe/Prague",
				"duration": 5,
				"participant": {
					"user_id": "idOfTheUserInMeeting10",
					"user_name": name,
					"id": "idOfTheUser98",
					"joint_time": "2022-05-02T16:25:10Z",
					"email": "user.email@example.com",
					"registrant_id": "registrantId3",
					"participant_user_id": "",
					"customer_key": "SDKId4"
				}
			}
		}
	});
}

function sendZoomWebHookRequest(body) {
	cy.request({
		method: 'POST',
		url: baseUrl + '/zoomhook',
		body: JSON.stringify(body),
		followRedirect: false,
		headers: {
			'content-type': 'application/json',
			authorization: zoomVerificationToken,
			'user-agent': 'Zoom Marketplace/1.0a'
		}
	});
}
