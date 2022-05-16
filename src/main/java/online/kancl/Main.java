package online.kancl;

import online.kancl.controller.MainPageController;

public class Main
{
	public static void main(String[] args)
	{
		Meetings meetings = new Meetings();
		ZoomHook zoomHook = new ZoomHook(meetings);
		MainPageController mainPageController = new MainPageController(meetings);

		WebServer webServer = new WebServer(8080, zoomHook, mainPageController);
		webServer.start();

		System.out.println("Server running");
	}
}
