package online.kancl;

public class Main
{
	public static void main(String[] args)
	{

		WebSocketHandler webSocketHandler = new WebSocketHandler();
		new World(webSocketHandler);
		WebServer webServer = new WebServer(8080, new ZoomHook(), webSocketHandler);
		webServer.start();

		System.out.println("Server running");
	}
}
