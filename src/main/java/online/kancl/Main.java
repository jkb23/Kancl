package online.kancl;

import spark.Spark;

public class Main
{
	public static void main(String[] args)
	{
		WebSocketHandler webSocketHandler = new WebSocketHandler();
		Meetings meetings = new Meetings(webSocketHandler);
		ZoomHook zoomHook = new ZoomHook(meetings);

		WebServer webServer = new WebServer(8080, zoomHook, webSocketHandler);

		Spark.exception(Exception.class, (exception, request, response) -> exception.printStackTrace());

		webServer.start();

		System.out.println("Server running");
	}
}
