package online.kancl.page.zoom.callback;

import online.kancl.server.Controller;
import spark.Request;
import spark.Response;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static online.kancl.page.ZoomConstants.CLIENT_ID;
import static online.kancl.page.ZoomConstants.CLIENT_SECRET;
import static online.kancl.page.ZoomConstants.OFFICE_URL;
import static online.kancl.page.ZoomConstants.TOKEN_URL;
import static online.kancl.page.ZoomConstants.ZOOM_CALLBACK_URL;

public class ZoomCallbackController extends Controller {

    public static String[] extractTokens(String jsonResponse) {
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse))) {
            jsonObject = jsonReader.readObject();
        }

        String accessToken = jsonObject.getString("access_token");
        String refreshToken = jsonObject.getString("refresh_token");

        return new String[]{accessToken, refreshToken};
    }

    private static String getOfficeURL(String[] tokens) {
        return "%s?accessToken=%s&refreshToken=%s".formatted(OFFICE_URL, tokens[0], tokens[1]);
    }

    @Override
    public String get(Request request, Response response) {
        String authCode = request.queryParams("code");
        if (authCode != null) {
            requestAccessToken(authCode, response);
        }

        return "";
    }

    private void requestAccessToken(String code, Response response) {
        try {
            HttpURLConnection connection = setUpConnection();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = getRequestBody(code).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            handleResponse(connection, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection setUpConnection() throws IOException {
        URL url = new URL(TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String auth = "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8));

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        return connection;
    }

    private String getRequestBody(String code) {
        return String.format("grant_type=authorization_code&code=%s&redirect_uri=%s",
                URLEncoder.encode(code, StandardCharsets.UTF_8),
                URLEncoder.encode(ZOOM_CALLBACK_URL, StandardCharsets.UTF_8));
    }

    private void handleResponse(HttpURLConnection connection, Response response) {
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String[] tokens = extractTokens(new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));

                response.redirect(getOfficeURL(tokens));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
