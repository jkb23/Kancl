package online.kancl.db;

import online.kancl.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionJobRunner
{
	public static <T> T runInTransactionAndRelease(Function<Connection, T> job)
	{
		Connection connection = Main.getConnection();
		Boolean originalAutoCommit = null;
		try
		{
			originalAutoCommit = connection.getAutoCommit();

			T result = job.apply(connection);

			connection.commit();

			return result;
		}
		catch (SQLException e)
		{
			rollback(connection);
			throw new RuntimeException(e);
		}
		finally
		{
			restoreAndClose(connection, originalAutoCommit);

		}
	}

	private static void rollback(Connection connection)
	{
		try
		{
			connection.rollback();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static void restoreAndClose(Connection connection, Boolean originalAutoCommit)
	{
		try
		{
			if (originalAutoCommit != null)
				connection.setAutoCommit(originalAutoCommit);

			connection.close();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
