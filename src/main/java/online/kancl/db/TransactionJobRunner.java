package online.kancl.db;

import online.kancl.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionJobRunner {

	public static <T> T runInTransactionAndRelease(Function<DatabaseRunner, T> job) {
		try (Connection connection = Main.getConnection()) {
			return disableAutocommitAndRunTransaction(job, connection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T disableAutocommitAndRunTransaction(Function<DatabaseRunner, T> job, Connection connection) throws SQLException {
		Boolean originalAutoCommit = null;
		try {
			originalAutoCommit = connection.getAutoCommit();

			return runJobAndCommitOrRollback(job, connection);
		}  finally {
			if (originalAutoCommit != null)
				connection.setAutoCommit(originalAutoCommit);
		}
	}

	private static <T> T runJobAndCommitOrRollback(Function<DatabaseRunner, T> job, Connection connection) throws SQLException {
		try {
			var databaseRunner = new DatabaseRunner(connection);
			T result = job.apply(databaseRunner);

			connection.commit();

			return result;
		} catch (Exception e) {
			connection.rollback();
			throw e;
		}
	}
}
