package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.controller.MainPageController;
import online.kancl.controller.ZoomHookController;
import online.kancl.model.Meetings;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.PebbleTemplateRenderer;
import online.kancl.server.WebServer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

		try
		{
			Connection connection = getConnection();
			try (PreparedStatement statement = connection.prepareStatement("SELECT testString FROM TestTable"))
			{
				try (ResultSet rs = statement.executeQuery())
				{
					if (!rs.next())
						System.out.println("Table TestTable is empty");
					else
						System.out.println("TestTable contains " + rs.getString(1));
				}
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static PebbleTemplateRenderer createPebbleTemplateRenderer()
	{
		FileLoader pebbleTemplateLoader = new FileLoader();
		pebbleTemplateLoader.setPrefix(TEMPLATE_DIRECTORY.toAbsolutePath().toString());
		PebbleEngine pebbleEngine = new PebbleEngine.Builder().loader(pebbleTemplateLoader).build();
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
