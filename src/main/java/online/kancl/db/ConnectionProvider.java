package online.kancl.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

	private static final String SQL_SCRATCH_DIRECTORY = "sql";

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
			throw new RuntimeException(e);
		}
	}

	private DataSource createDataSource(String dbLocation) {
		var dataSource = new BasicDataSource();

		dataSource.setUrl("jdbc:h2:" + dbLocation);
		dataSource.setMaxOpenPreparedStatements(100);

		new SchemaCreator().recreateSchemaIfNeeded(dataSource, SQL_SCRATCH_DIRECTORY);

		return dataSource;
	}
}
