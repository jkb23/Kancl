package online.kancl;

public class Main
{
	public static void main(String[] args)
	{
		WebSocketHandler webSocketHandler = new WebSocketHandler();
		Meetings meetings = new Meetings(webSocketHandler);
		ZoomHook zoomHook = new ZoomHook(meetings);

		WebServer webServer = new WebServer(8080, zoomHook, webSocketHandler);
		webServer.start();

		System.out.println("Server running");
	}
}
