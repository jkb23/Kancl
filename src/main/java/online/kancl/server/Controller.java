package online.kancl.server;

import online.kancl.Main;
import online.kancl.db.TransactionJobRunner;
import spark.Request;
import spark.Response;

import java.sql.Connection;

public class Controller
{
	public String handleGet(Request request, Response response)
	{
		Connection connection = Main.getConnection();
		return TransactionJobRunner.runInTransaction(connection, () -> get(request, response, connection));
	}

	public String handlePost(Request request, Response response)
	{
		Connection connection = Main.getConnection();
		return TransactionJobRunner.runInTransaction(connection, () -> post(request, response, connection));
	}

	protected String get(Request request, Response response, Connection connection)
	{
		return "";
	}

	protected String post(Request request, Response response, Connection connection)
	{
		return "";
	}
}
