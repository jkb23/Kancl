package online.kancl.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionJobRunner {


    private final ConnectionProvider connectionProvider;

    public TransactionJobRunner(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public <T> T runInTransaction(Function<DatabaseRunner, T> job) {
        try (Connection connection = connectionProvider.getConnection()) {
            return disableAutocommitAndRunTransaction(job, connection);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to execute transactional job", e);
        }
    }

    private <T> T disableAutocommitAndRunTransaction(Function<DatabaseRunner, T> job, Connection connection) throws SQLException {
        Boolean originalAutoCommit = null;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            return runJobAndCommitOrRollback(job, connection);
        } finally {
            if (originalAutoCommit != null) {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private <T> T runJobAndCommitOrRollback(Function<DatabaseRunner, T> job, Connection connection) throws SQLException {
        try {
            DatabaseRunner databaseRunner = new DatabaseRunner(connection);
            T result = job.apply(databaseRunner);
            connection.commit();

            return result;
        } catch (Exception e) {
            connection.rollback();
            throw new DatabaseOperationException("Error occurred during transaction execution. Transaction was rolled back.", e);
        }
    }

    public static class DatabaseOperationException extends RuntimeException {
        public DatabaseOperationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
