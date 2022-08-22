package online.kancl.db;

import online.kancl.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionJobRunner {

	public static <T> T runInTransactionAndRelease(Function<DatabaseRunner, T> job) {
		try (Connection connection = Main.getConnection()) {
			Boolean originalAutoCommit = null;
			try {
				originalAutoCommit = connection.getAutoCommit();

				var databaseRunner = new DatabaseRunner(connection);
				T result = job.apply(databaseRunner);

				connection.commit();

				return result;
			} catch (Exception e) {
				rollback(connection);
				throw e;
			} finally {
				restoreAutocommit(connection, originalAutoCommit);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void restoreAutocommit(Connection connection, Boolean originalAutoCommit) throws SQLException {
		if (originalAutoCommit != null)
			connection.setAutoCommit(originalAutoCommit);
	}
}
