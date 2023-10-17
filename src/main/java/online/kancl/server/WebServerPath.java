package online.kancl.server;

public enum WebServerPath {
    ROOT("/"),
    RECREATE_DB("/recreateDb"),
    USER("/user"),
    REGISTER("/register"),
    LOGIN("/login"),
    LOGOUT("/logout"),
    API_OFFICE("/api/office"),
    API_EDIT("/api/edit"),
    EDIT("/edit"),
    ZOOM_CALLBACK("/zoom-callback"),
    API_ZOOM_MEETING("/zoom-meeting");

    private final String path;

    WebServerPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

