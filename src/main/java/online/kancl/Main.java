package online.kancl;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import online.kancl.controller.MainPageController;

import java.sql.*;

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

		try
		{
			Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost/kanclOnline", "user", "password");
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
}
