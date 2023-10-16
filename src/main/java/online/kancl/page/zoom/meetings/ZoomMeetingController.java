package online.kancl.page.zoom.meetings;

import online.kancl.objects.GridData;
import online.kancl.objects.MeetingObject;
import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

import static online.kancl.page.ZoomConstants.ACCESS_TOKEN;
import static online.kancl.page.ZoomConstants.CREATE_MEETING_URL;
import static online.kancl.page.ZoomConstants.DELETE_MEETING_URL;

public class ZoomMeetingController extends Controller {
    private final GridData gridData;

    StringBuilder responseContent = new StringBuilder();
    int xCoordinate;
    int yCoordinate;

    public ZoomMeetingController(GridData gridData) {
        this.gridData = gridData;
    }

    @Override
    public String post(Request request, Response response) {
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(request.body()))) {
            jsonObject = jsonReader.readObject();
        }

        String action = jsonObject.getString("action");
        xCoordinate = jsonObject.getInt("xCoordinate");
        yCoordinate = jsonObject.getInt("yCoordinate");

        if (action.equals("create")) {
            String meetingName = jsonObject.getString("meetingName");

            return createZoomMeeting(meetingName);
        } else if (action.equals("delete")) {
            String meetingId = jsonObject.getString("meetingId");

            return deleteZoomMeeting(meetingId);
        }

        return "Invalid action";
    }

    private String createZoomMeeting(String meetingName) {
        try {
            URL url = new URL(CREATE_MEETING_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");

            String payload = "{ \"agenda\": \"" + meetingName + "\", \"topic\": \"" + meetingName + "\", \"type\": 1 }";

            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                }

                JsonObject jsonObject;
                try (JsonReader jsonReader = Json.createReader(new StringReader(responseContent.toString()))) {
                    jsonObject = jsonReader.readObject();
                }

                sendToEditEndpoint(meetingName, jsonObject.getString("join_url"), jsonObject.getJsonNumber("id").toString());

                return "Meeting created and data sent to /api/edit";
            } else {
                return "Error creating Zoom meeting";
            }
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }

    private void sendToEditEndpoint(String meetingName, String meetingLink, String meetingId) {
        MeetingObject meetingObject = new MeetingObject(xCoordinate, yCoordinate, meetingLink, meetingName, meetingId);
        gridData.addMeeting(meetingObject);
    }

    private String deleteZoomMeeting(String meetingId) {
        try {
            URL url = new URL(DELETE_MEETING_URL + meetingId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Meeting deleted";
            } else {
                return "Error deleting Zoom meeting";
            }
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }
}
