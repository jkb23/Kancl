package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.CommentsController;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.WebServer;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

	public static final Path TEMPLATE_DIRECTORY = Paths.get("src", "main", "pebble", "templates");

	private static final DataSource DATA_SOURCE = createDataSource();

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

	private static DataSource createDataSource() {
		var ds = new BasicDataSource();

		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		String database = System.getenv("DB_NAME");

		ds.setUrl("jdbc:mariadb://localhost/" + database);
		ds.setUsername(user);
		ds.setPassword(password);
		ds.setMaxOpenPreparedStatements(100);

		return ds;
	}

	public static Connection getConnection() {
		try {
			return DATA_SOURCE.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
