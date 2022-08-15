package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;
import online.kancl.server.WebServer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

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
		ZoomHookController zoomHookController = new ZoomHookController(meetings);
		MainPageController mainPageController = new MainPageController(pebbleTemplateRenderer, meetings);
		ExceptionHandler exceptionHandler = new ExceptionHandler();

		WebServer webServer = new WebServer(8081, exceptionHandler);
		webServer.addRoute("/", mainPageController);
		webServer.addRoute("/zoomhook", zoomHookController);
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
				.build();

		return new PebbleTemplateRenderer(pebbleEngine);
	}

	private static Connection getConnection() throws SQLException
	{
		String user = System.getenv("MYSQL_USER");
		String password = System.getenv("MYSQL_PASSWORD");
		String database = System.getenv("MYSQL_DATABASE");

		return DriverManager.getConnection("jdbc:mariadb://localhost/" + database, user, password);
	}
}
