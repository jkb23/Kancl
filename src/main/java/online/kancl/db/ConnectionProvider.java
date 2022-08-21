package online.kancl.db;

import online.kancl.util.ResourcePathResolver;
import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static online.kancl.util.ResourcePathResolver.getResourcePath;

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

		recreateSchemaIfNeeded(dataSource);

		return dataSource;
	}

	private void recreateSchemaIfNeeded(BasicDataSource dataSource) {
		try (Connection connection = dataSource.getConnection()) {
			var dbRunner = new DatabaseRunner(connection);

			Path scratchDirectory = getResourcePath(this, SQL_SCRATCH_DIRECTORY);

			Optional<String> storedSchemaHash = getStoredSchemaHash(dbRunner);
			String scratchDirectoryHash = new DirectoryHashCalculator().calculateEncodedHash(scratchDirectory);

			if (storedSchemaHash.isEmpty() || !storedSchemaHash.get().equals(scratchDirectoryHash)) {
				recreateSchema(dbRunner, connection);
				storeSchemaHash(dbRunner, scratchDirectoryHash);
				connection.commit();
			}
		} catch (SQLException e) {
			throw new DatabaseRunner.DatabaseAccessException(e);
		}
	}

	private Optional<String> getStoredSchemaHash(DatabaseRunner dbRunner) {

		int tableCount = dbRunner.<Optional<Integer>>query(
				"SELECT count(1) FROM information_schema.Tables WHERE table_schema = 'PUBLIC'",
				(row) -> {
					if (row.next())
						return Optional.of(row.getInt(1));
					else
						return Optional.empty();
				}).orElse(0);

		if (tableCount == 0)
			return Optional.empty();

		return dbRunner.query("SELECT hash FROM DatabaseSchemaHash", (row) -> {
			if (row.next())
				return Optional.of(row.getString(1));
			else
				return Optional.empty();
		});
	}

	private void recreateSchema(DatabaseRunner dbRunner, Connection connection) {
		System.out.println("Recreating DB schema");

		try (InputStream scratchFile = this.getClass().getClassLoader().getResourceAsStream("sql/scratch.sql")) {
			dbRunner.update("DROP ALL OBJECTS");
			RunScript.execute(connection, new InputStreamReader(scratchFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new SchemaCreationException(e);
		}
	}

	private void storeSchemaHash(DatabaseRunner dbRunner, String schemaHash) {
		dbRunner.update("INSERT INTO DatabaseSchemaHash (hash) VALUES (?)", schemaHash);
	}

	public static class SchemaCreationException extends RuntimeException {
		public SchemaCreationException(Throwable cause) {
			super("Error when creating schema", cause);
		}
	}
}
