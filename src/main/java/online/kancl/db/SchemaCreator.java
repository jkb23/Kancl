package online.kancl.db;

import org.h2.tools.RunScript;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SchemaCreator {
	public void recreateSchemaIfNeeded(ConnectionProvider connectionProvider, Path scratchDirectory) {
		try (Connection connection = connectionProvider.getConnection()) {
			var dbRunner = new DatabaseRunner(connection);

			String scratchDirectoryHash = new DirectoryHashCalculator().calculateEncodedHash(scratchDirectory);
			if (shouldRecreateSchema(dbRunner, scratchDirectoryHash)) {
				recreateSchema(dbRunner, connection, scratchDirectory);
				storeSchemaHash(connection, scratchDirectoryHash);
			}
		} catch (SQLException e) {
			throw new DatabaseRunner.DatabaseAccessException(e);
		}
	}

	private boolean shouldRecreateSchema(DatabaseRunner dbRunner, String scratchDirectoryHash) {
		Optional<String> storedSchemaHash = getStoredSchemaHash(dbRunner);
		return storedSchemaHash.isEmpty() || !storedSchemaHash.get().equals(scratchDirectoryHash);
	}

	private Optional<String> getStoredSchemaHash(DatabaseRunner dbRunner) {

		int tableCount = dbRunner.query(
				"SELECT count(1) FROM information_schema.Tables WHERE table_schema = 'PUBLIC'",
				(row) -> row.getInt(1)
		).orElse(0);

		if (tableCount == 0)
			return Optional.empty();

		return dbRunner.query("SELECT hash FROM DatabaseSchemaHash", (row) -> row.getString(1));
	}

	private void recreateSchema(DatabaseRunner dbRunner, Connection connection, Path scratchDirectory) {
		try {
			System.out.println("Recreating DB schema");
			dbRunner.update("DROP ALL OBJECTS");

			for (Path sqlFile : getSqlFilesInDirectory(scratchDirectory)) {
				System.out.println("Running " + scratchDirectory.relativize(sqlFile));

				byte[] fileContent = Files.readAllBytes(sqlFile);
				var reader = new InputStreamReader(new ByteArrayInputStream(fileContent), StandardCharsets.UTF_8);

				RunScript.execute(connection, reader);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new SchemaCreationException(e);
		}
	}

	private List<Path> getSqlFilesInDirectory(Path scratchDirectory) {
		try {
			List<Path> files = new ArrayList<>();

			Files.walkFileTree(scratchDirectory, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					files.add(file);
					return FileVisitResult.CONTINUE;
				}
			});

			return files.stream()
					.filter(file -> file.toString().endsWith(".sql"))
					.sorted()
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void storeSchemaHash(Connection connection, String schemaHash) throws SQLException {
		new DatabaseRunner(connection).update("INSERT INTO DatabaseSchemaHash (hash) VALUES (?)", schemaHash);
		connection.commit();
	}

	public static class SchemaCreationException extends RuntimeException {
		public SchemaCreationException(Throwable cause) {
			super("Error when creating schema", cause);
		}
	}
}
