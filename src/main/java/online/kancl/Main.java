package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.CommentsController;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.db.ConnectionProvider;
import online.kancl.db.SchemaCreator;
import online.kancl.db.TransactionJobRunner;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.WebServer;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static final Path TEMPLATE_DIRECTORY = Paths.get("src", "main", "pebble", "templates");
	private static final Path SQL_SCRATCH_DIRECTORY = Paths.get("src", "main", "resources", "sql");
	private static final Path DB_DIRECTORY = Paths.get("db");
	private static final String DB_NAME = "data";

	public static void main(String[] args) {
		PebbleTemplateRenderer pebbleTemplateRenderer = createPebbleTemplateRenderer();

		ConnectionProvider connectionProvider = ConnectionProvider.forDatabaseInFile(DB_DIRECTORY, DB_NAME);
		new SchemaCreator().recreateSchemaIfNeeded(connectionProvider, SQL_SCRATCH_DIRECTORY);
		var transactionJobRunner = new TransactionJobRunner(connectionProvider);

		var meetings = new Meetings();

		var webServer = new WebServer(8081, new ExceptionHandler());
		webServer.addRoute("/", new MainPageController(pebbleTemplateRenderer, meetings));
		webServer.addRoute("/comments", new CommentsController(pebbleTemplateRenderer, transactionJobRunner));
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
}
