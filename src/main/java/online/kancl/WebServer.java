package online.kancl;

import spark.Request;
import spark.Response;
import spark.Spark;

public class WebServer
{
	private final int port;
	private final ZoomHook zoomHook;
	private final Object webSocketHandler;

	public WebServer(int port, ZoomHook zoomHook, Object webSocketHandler)
	{
		this.port = port;
		this.zoomHook = zoomHook;
		this.webSocketHandler = webSocketHandler;
	}

	public void start() {
		Spark.port(port);

		Spark.staticFiles.externalLocation("web");
		Spark.webSocket("/websocket", webSocketHandler);
		Spark.get("/zoomhook", this::callZoomHook);
		Spark.post("/zoomhook", this::callZoomHook);

		Spark.init();
	}

	private String callZoomHook(Request request, Response response) {
		zoomHook.handleZoomMessage(request);
		return "OK";
	}
}
