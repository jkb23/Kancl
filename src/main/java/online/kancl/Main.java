package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.CommentsController;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;
import online.kancl.server.WebServer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main
{
	public static final Path TEMPLATE_DIRECTORY = Paths.get("src", "main", "pebble", "templates");

	public static void main(String[] args)
	{
		PebbleTemplateRenderer pebbleTemplateRenderer = createPebbleTemplateRenderer();

		Meetings meetings = new Meetings();

		WebServer webServer = new WebServer(8081, new ExceptionHandler());
		webServer.addRoute("/", new MainPageController(pebbleTemplateRenderer, meetings));
		webServer.addRoute("/comments", new CommentsController(pebbleTemplateRenderer));
		webServer.addRoute("/zoomhook", new ZoomHookController(meetings));
		webServer.start();

		System.out.println("Server running");

		/*try
		{
			Connection connection = getConnection();

			String test = new QueryRunner().query(connection,
					"SELECT testString FROM TestTable", new ScalarHandler<>());

			System.out.println("TestTable contains " + test);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}*/
	}

	private static PebbleTemplateRenderer createPebbleTemplateRenderer()
	{
		FileLoader pebbleTemplateLoader = new FileLoader();
		pebbleTemplateLoader.setPrefix(TEMPLATE_DIRECTORY.toAbsolutePath().toString());
		PebbleEngine pebbleEngine = new PebbleEngine.Builder()
				.loader(pebbleTemplateLoader)
				.extension(new PebbleExtension())
				.cacheActive(false)
				.build();

		return new PebbleTemplateRenderer(pebbleEngine);
	}

	public static Connection getConnection()
	{
		try
		{
			String user = System.getenv("MYSQL_USER");
			String password = System.getenv("MYSQL_PASSWORD");
			String database = System.getenv("MYSQL_DATABASE");

			return DriverManager.getConnection("jdbc:mariadb://localhost/" + database, user, password);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
