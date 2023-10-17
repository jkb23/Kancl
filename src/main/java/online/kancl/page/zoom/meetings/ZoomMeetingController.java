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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static online.kancl.page.ZoomConstants.ACCESS_TOKEN;
import static online.kancl.page.ZoomConstants.CREATE_MEETING_URL;
import static online.kancl.page.ZoomConstants.DELETE_MEETING_URL;

public class ZoomMeetingController extends Controller {
    private final GridData gridData;

    StringBuilder responseContent = new StringBuilder();

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
        MeetingObject meetingObject = getMeetingObject(jsonObject);

        if (action.equals("create")) {
            return createZoomMeeting(meetingObject);
        } else if (action.equals("delete")) {
            return deleteZoomMeeting(meetingObject);
        }

        return "Invalid action";
    }

    private static MeetingObject getMeetingObject(JsonObject jsonObject) {
        String meetingName = jsonObject.getString("meetingName");
        String meetingId = jsonObject.getString("meetingId");
        String meetingLink = jsonObject.getString("meetingLink");
        int xCoordinate = jsonObject.getInt("xCoordinate");
        int yCoordinate = jsonObject.getInt("yCoordinate");

        return new MeetingObject(xCoordinate, yCoordinate, meetingLink, meetingName, meetingId);
    }

    private String createZoomMeeting(MeetingObject meetingObject) {
        try {
            HttpURLConnection connection = getHttpURLConnection(CREATE_MEETING_URL, "POST");

            String payload = "{ \"agenda\": \"" + meetingObject.getName() + "\", \"topic\": \"" + meetingObject.getName() + "\", \"type\": 1 }";

            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                JsonObject jsonObject = getJsonObject(connection);

                meetingObject.setId(jsonObject.getJsonNumber("id").toString());
                meetingObject.setLink(jsonObject.getString("join_url"));

                gridData.addMeeting(meetingObject);

                return "Meeting created and data sent to /api/edit";
            } else {
                return "Error creating Zoom meeting";
            }
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }

    private String deleteZoomMeeting(MeetingObject meetingObject) {
        try {
            HttpURLConnection connection = getHttpURLConnection(DELETE_MEETING_URL + meetingObject.getId(), "DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                gridData.deleteMeeting(meetingObject);
                return "Meeting deleted";
            } else {
                return "Error deleting Zoom meeting";
            }
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }

    private static HttpURLConnection getHttpURLConnection(String url2, String requestMethod) throws IOException {
        URL url = new URL(url2);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }

    private JsonObject getJsonObject(HttpURLConnection connection) throws IOException {
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
        return jsonObject;
    }
}
