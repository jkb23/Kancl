package online.kancl.db;

import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static online.kancl.util.ResourcePathResolver.getResourcePath;

class SchemaCreator {
	public void recreateSchemaIfNeeded(DataSource dataSource, String sqlScratchDirectory) {
		try (Connection connection = dataSource.getConnection()) {
			var dbRunner = new DatabaseRunner(connection);

			Path scratchDirectory = getResourcePath(this, sqlScratchDirectory);

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
				try (var fileReader = new FileReader(sqlFile.toFile(), StandardCharsets.UTF_8)) {
					RunScript.execute(connection, fileReader);
				}
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
