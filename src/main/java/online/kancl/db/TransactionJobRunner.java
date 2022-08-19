package online.kancl.db;

import online.kancl.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionJobRunner {

	public static <T> T runInTransactionAndRelease(Function<DatabaseRunner, T> job) {
		Connection connection = Main.getConnection();
		Boolean originalAutoCommit = null;
		try {
			originalAutoCommit = connection.getAutoCommit();

			var databaseRunner = new DatabaseRunner(connection);
			T result = job.apply(databaseRunner);

			connection.commit();

			return result;
		} catch (SQLException e) {
			rollback(connection);
			throw new RuntimeException(e);
		} finally {
			restoreAndClose(connection, originalAutoCommit);
		}
	}

	private static void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void restoreAndClose(Connection connection, Boolean originalAutoCommit) {
		try {
			if (originalAutoCommit != null)
				connection.setAutoCommit(originalAutoCommit);

			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
