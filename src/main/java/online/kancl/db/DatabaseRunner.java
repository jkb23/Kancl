package online.kancl.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseRunner {
	private final Connection connection;
	private final QueryRunner queryRunner;

	public DatabaseRunner(Connection connection) {
		this.connection = connection;
		this.queryRunner = new QueryRunner();
	}

	/**
	 * Execute an SQL SELECT query.
	 */
	public <T> T query(String sql, ResultSetHandler<T> rsh, Object... binds) {
		return executeAndWrapSQLException(() -> queryRunner.query(connection, sql, rsh, binds));
	}

	/**
	 * Execute an SQL INSERT, UPDATE, or DELETE query.
	 * @return The number of rows updated.
	 */
	public int update(String sql, Object... binds) {
		return executeAndWrapSQLException(() -> queryRunner.update(connection, sql, binds));
	}

	/**
	 * Executes the given INSERT SQL statement.
	 * @return auto-generated keys
	 */
	public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... binds) {
		return executeAndWrapSQLException(() -> queryRunner.insert(connection, sql, rsh, binds));
	}

	private <T> T executeAndWrapSQLException(TrowingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (SQLException e) {
			throw new DatabaseAccessException(e);
		}
	}

	public interface TrowingSupplier<T> {
		T get() throws SQLException;
	}

	public static class DatabaseAccessException extends RuntimeException {
		public DatabaseAccessException(Throwable cause) {
			super("Error when accessing database", cause);
		}
	}
}
