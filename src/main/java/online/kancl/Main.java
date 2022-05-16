package online.kancl;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import online.kancl.controller.MainPageController;

public class Main
{
	public static void main(String[] args)
	{
		Meetings meetings = new Meetings();
		ZoomHook zoomHook = new ZoomHook(meetings);
		MustacheFactory mustacheFactory = new DefaultMustacheFactory();
		MainPageController mainPageController = new MainPageController(mustacheFactory, meetings);

		WebServer webServer = new WebServer(8080, zoomHook, mainPageController);
		webServer.start();

		System.out.println("Server running");
	}
}
