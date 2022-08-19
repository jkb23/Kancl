package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.CommentsController;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.db.ConnectionProvider;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.WebServer;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

public class Main {

	public static final Path TEMPLATE_DIRECTORY = Paths.get("src", "main", "pebble", "templates");
	private static final Path DB_DIRECTORY = Paths.get("db");
	private static final String DB_NAME = "data";

	private static final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.forDatabaseInFile(DB_DIRECTORY, DB_NAME);

	public static void main(String[] args) {
		PebbleTemplateRenderer pebbleTemplateRenderer = createPebbleTemplateRenderer();

		var meetings = new Meetings();

		var webServer = new WebServer(8081, new ExceptionHandler());
		webServer.addRoute("/", new MainPageController(pebbleTemplateRenderer, meetings));
		webServer.addRoute("/comments", new CommentsController(pebbleTemplateRenderer));
		webServer.addRoute("/zoomhook", new ZoomHookController(meetings));
		webServer.start();

		System.out.println("Server running");
	}

	private static PebbleTemplateRenderer createPebbleTemplateRenderer() {
		var pebbleTemplateLoader = new FileLoader();
		pebbleTemplateLoader.setPrefix(TEMPLATE_DIRECTORY.toAbsolutePath().toString());
		var pebbleEngine = new PebbleEngine.Builder()
				.loader(pebbleTemplateLoader)
				.extension(new PebbleExtension())
				.cacheActive(false)
				.build();

		return new PebbleTemplateRenderer(pebbleEngine);
	}
	
	public static Connection getConnection() {
		return CONNECTION_PROVIDER.getConnection();
	}
}
