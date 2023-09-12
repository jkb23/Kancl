import {CLIENT_ID, CLIENT_SECRET, CREATE_MEETING_URL, DELETE_MEETING_URL, TOKEN_URL} from "./constants.js";

async function refreshAccessToken() {
    const btoa = window.btoa;
    const authString = `${CLIENT_ID}:${CLIENT_SECRET}`;
    const headers = {
        Authorization: 'Basic ' + btoa(authString),
        'Content-Type': 'application/x-www-form-urlencoded'
    };

    let refreshToken = localStorage.getItem('refreshToken');
    const response = await fetch(TOKEN_URL, {
        method: 'POST',
        headers: headers,
        body: `grant_type=refresh_token&refresh_token=${refreshToken}`
    });

    if (response.ok) {
        const data = await response.json();

        return {
            accessToken: data.access_token,
            refreshToken: data.refresh_token
        };
    } else {
        console.error("Error refreshing access token:", await response.text());

        return null;
    }
}

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
        } /*else if (response.status === 401) {
            const newTokens = await refreshAccessToken();

            if (newTokens) {
                localStorage.setItem('accessToken', newTokens.accessToken);
                localStorage.setItem('refreshToken', newTokens.refreshToken);

                return createZoomMeeting(meetingName);
            } else {
                console.error("Failed to refresh access token.");

                return null;
            }
        }*/ else {
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
        }/* else if (response.status === 401) {
            const newTokens = await refreshAccessToken();

            if (newTokens) {
                localStorage.setItem('accessToken', newTokens.accessToken);
                localStorage.setItem('refreshToken', newTokens.refreshToken);

                return deleteZoomMeeting(meetingId);
            } else {
                console.error("Failed to refresh access token.");
                return null;
            }
        }*/ else {
            console.error("Error deleting Zoom meeting:", await response.text());
            return null;
        }
    } catch (error) {
        console.error("Network error:", error);
        return null;
    }
}

