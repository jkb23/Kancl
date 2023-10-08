import {CREATE_MEETING_URL, DELETE_MEETING_URL} from "./constants.js";

export async function createZoomMeeting(meetingName) {
    let accessToken = localStorage.getItem('accessToken');

    const headers = {
        "Authorization": `Bearer ${accessToken}`,
        "Content-Type": "application/json"
    };

    const payload = {
        "agenda": meetingName,
        "topic": meetingName,
        "type": 1
    };

    try {
        const response = await fetch(CREATE_MEETING_URL, {
            method: "POST",
            headers: headers,
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            return await response.json();
        } else {
            console.error("Error creating Zoom meeting:", await response.text());

            return null;
        }
    } catch (error) {
        console.error("Network error:", error);
        return null;
    }
}

export async function deleteZoomMeeting(meetingId) {
    if (!meetingId) {
        console.error("Meeting ID is required to delete a Zoom meeting.");
        return null;
    }

    let accessToken = localStorage.getItem('accessToken');

    const headers = {
        "Authorization": `Bearer ${accessToken}`,
        "Content-Type": "application/json"
    };

    try {
        const response = await fetch(DELETE_MEETING_URL + `${meetingId}`, {
            method: "DELETE",
            headers: headers,
        });

        if (response.ok) {
            return true;
        } else {
            console.error("Error deleting Zoom meeting:", await response.text());
            return null;
        }
    } catch (error) {
        console.error("Network error:", error);
        return null;
    }
}

