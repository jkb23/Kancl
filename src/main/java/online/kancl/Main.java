package online.kancl;

public class Main
{
	public static void main(String[] args)
	{
		WebServer webServer = new WebServer(8080, new ZoomHook());
		webServer.start();

		System.out.println("Server running");
	}
}
