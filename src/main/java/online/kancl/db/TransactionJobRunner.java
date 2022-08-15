package online.kancl.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class TransactionJobRunner
{
	public static <T> T runInTransaction(Connection connection, Supplier<T> job)
	{
		Boolean originalAutoCommit = null;
		try
		{
			originalAutoCommit = connection.getAutoCommit();

			T result = job.get();

			connection.commit();

			return result;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			rollback(connection, originalAutoCommit);
		}
	}

	private static void rollback(Connection connection, Boolean originalAutoCommit)
	{
		try
		{
			connection.rollback();

			if (originalAutoCommit != null)
				connection.setAutoCommit(originalAutoCommit);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
