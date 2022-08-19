package online.kancl.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
			throw new RuntimeException(e);
		}
	}

	private DataSource createDataSource(String dbLocation) {
		var ds = new BasicDataSource();

		ds.setUrl("jdbc:h2:" + dbLocation);
		ds.setMaxOpenPreparedStatements(100);

		recreateSchema(ds);

		return ds;
	}

	private void recreateSchema(DataSource ds) {
		try (Connection connection = ds.getConnection()) {
			try (InputStream scratchFile = this.getClass().getClassLoader().getResourceAsStream("sql/scratch.sql")) {
				var dbRunner = new DatabaseRunner(connection);

				dbRunner.update("DROP ALL OBJECTS");
				RunScript.execute(connection, new InputStreamReader(scratchFile));
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
