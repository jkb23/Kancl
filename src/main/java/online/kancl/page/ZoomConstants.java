package online.kancl.page;

public class ZoomConstants {
    public static final String CLIENT_ID = "OvUwO2BtTuqx9z9Yagp5tQ";
    public static final String CLIENT_SECRET = "LJ1qIc465XUba5RlIA8cXI5Lbw9NggGK";
    public static final String TOKEN_URL = "https://zoom.us/oauth/token";
    public static final String OFFICE_URL = "http://localhost:8081/";
    public static final String ZOOM_CALLBACK_URL = "http://localhost:8081/zoom-callback";
    public static final String CREATE_MEETING_URL = "https://api.zoom.us/v2/users/me/meetings";
    public static final String DELETE_MEETING_URL = "https://api.zoom.us/v2/meetings/";
    public static String ACCESS_TOKEN = "";
    public static String REFRESH_TOKEN = "";

    private ZoomConstants() {
        throw new UnsupportedOperationException("ZoomConstants is a utility class and should not be instantiated");
    }
}
