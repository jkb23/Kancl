package online.kancl.util;

import spark.Response;

public class HttpUtil {

    public static void dontCache(Response response) {
        response.header("Cache-Control", "private, max-age=0, no-cache, no-store");
    }
}
