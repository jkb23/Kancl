package online.kancl;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import online.kancl.controller.MainPageController;
import online.kancl.server.MustacheTemplateRenderer;

import java.sql.*;

public class Main
{
	public static void main(String[] args)
	{
		Meetings meetings = new Meetings();
		ZoomHook zoomHook = new ZoomHook(meetings);
		MustacheFactory mustacheFactory = new DefaultMustacheFactory();
		MustacheTemplateRenderer mustacheTemplateRenderer = new MustacheTemplateRenderer(mustacheFactory);
		MainPageController mainPageController = new MainPageController(mustacheTemplateRenderer, meetings);

		WebServer webServer = new WebServer(8081, zoomHook, mainPageController);
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

	private static Connection getConnection() throws SQLException
	{
		String user = System.getenv("MYSQL_USER");
		String password = System.getenv("MYSQL_PASSWORD");
		String database = System.getenv("MYSQL_DATABASE");

		return DriverManager.getConnection("jdbc:mariadb://localhost/" + database, user, password);
	}
}
