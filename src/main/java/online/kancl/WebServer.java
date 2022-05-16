package online.kancl;

import online.kancl.controller.MainPageController;
import spark.Request;
import spark.Response;
import spark.Spark;

public class WebServer
{
	private final int port;
	private final ZoomHook zoomHook;
	private final MainPageController mainPageController;

	public WebServer(int port, ZoomHook zoomHook, MainPageController mainPageController)
	{
		this.port = port;
		this.zoomHook = zoomHook;
		this.mainPageController = mainPageController;
	}

	public void start() {
		Spark.port(port);

		Spark.staticFiles.externalLocation("web");
		Spark.get("/", mainPageController::get);
		Spark.get("/zoomhook", this::callZoomHook);
		Spark.post("/zoomhook", this::callZoomHook);

		Spark.exception(Exception.class, (exception, request, response) -> exception.printStackTrace());

		Spark.init();
	}

	private String callZoomHook(Request request, Response response) {
		zoomHook.handleZoomMessage(request);
		return "OK";
	}
}
