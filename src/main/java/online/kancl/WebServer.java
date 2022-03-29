package online.kancl;

import spark.Request;
import spark.Response;
import spark.Spark;

public class WebServer
{
	private final int port;
	private final ZoomHook zoomHook;

	public WebServer(int port, ZoomHook zoomHook)
	{
		this.port = port;
		this.zoomHook = zoomHook;
	}

	public void start() {
		Spark.port(port);

		Spark.staticFiles.location("/public");
		Spark.get("/zoomhook", this::callZoomHook);
		Spark.post("/zoomhook", this::callZoomHook);

		Spark.init();
	}

	private String callZoomHook(Request request, Response response) {
		zoomHook.accept(request);
		return "OK";
	}
}
