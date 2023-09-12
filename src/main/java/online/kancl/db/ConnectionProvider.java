package online.kancl.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

    private final DataSource dataSource;

    public static ConnectionProvider forInMemoryDatabase(String dbName) {
        return new ConnectionProvider("mem:" + dbName);
    }

    public static ConnectionProvider forDatabaseInFile(Path path, String dbName) {
        String dbLocation = path.resolve(dbName).toAbsolutePath().toString();
        return new ConnectionProvider(dbLocation);
    }

    private ConnectionProvider(String dbLocation) {
        this.dataSource = createDataSource(dbLocation);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to get database connection.", e);
        }
    }

    private DataSource createDataSource(String dbLocation) {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setUrl("jdbc:h2:" + dbLocation);
        basicDataSource.setMaxOpenPreparedStatements(100);

        return basicDataSource;
    }

    public static class DatabaseConnectionException extends RuntimeException {
        public DatabaseConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
