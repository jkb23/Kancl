package online.kancl.db;

import online.kancl.util.DirectoryHashCalculator;
import org.h2.tools.RunScript;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class SchemaCreator {

    private final DirectoryHashCalculator directoryHashCalculator;
    private final ConnectionProvider connectionProvider;
    private final Path scratchDirectory;

    public SchemaCreator(DirectoryHashCalculator directoryHashCalculator, ConnectionProvider connectionProvider, Path scratchDirectory) {
        this.directoryHashCalculator = directoryHashCalculator;
        this.connectionProvider = connectionProvider;
        this.scratchDirectory = scratchDirectory;
    }

    public void recreateSchemaIfNeeded() {
        if (shouldRecreateSchema()) {
            System.out.println("Recreating DB schema");
            recreateSchema();
        }
    }

    public void recreateSchema() {
        try (Connection connection = connectionProvider.getConnection()) {
            DatabaseRunner dbRunner = new DatabaseRunner(connection);
            recreateSchema(dbRunner, connection, scratchDirectory);
        } catch (SQLException e) {
            throw new DatabaseRunner.DatabaseAccessException(e);
        }
    }

    private boolean shouldRecreateSchema() {
        try (Connection connection = connectionProvider.getConnection()) {
            DatabaseRunner dbRunner = new DatabaseRunner(connection);

            String scratchDirectoryHash = directoryHashCalculator.calculateEncodedHash(scratchDirectory);

            Optional<String> storedSchemaHash = getStoredSchemaHash(dbRunner);

            return storedSchemaHash.isEmpty() || !storedSchemaHash.get().equals(scratchDirectoryHash);
        } catch (SQLException e) {
            throw new DatabaseRunner.DatabaseAccessException(e);
        }
    }

    private void recreateSchema(DatabaseRunner dbRunner, Connection connection, Path scratchDirectory) {
        dbRunner.update("DROP ALL OBJECTS");

        for (Path sqlFile : getSqlFilesInDirectory(scratchDirectory)) {
            runSqlFile(connection, sqlFile);
        }

        String schemaHash = directoryHashCalculator.calculateEncodedHash(scratchDirectory);
        storeSchemaHash(dbRunner, schemaHash);
    }

    private Optional<String> getStoredSchemaHash(DatabaseRunner dbRunner) {

        int tableCount = dbRunner.selectInt(
                "SELECT count(1) FROM information_schema.Tables WHERE table_schema = 'PUBLIC'"
        );

        if (tableCount == 0)
            return Optional.empty();

        return dbRunner.select("SELECT hash FROM DatabaseSchemaHash", (row) -> row.getString(1));
    }

    private void runSqlFile(Connection connection, Path sqlFile) {
        try {
            byte[] fileContent = Files.readAllBytes(sqlFile);
            InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(fileContent), StandardCharsets.UTF_8);

            RunScript.execute(connection, reader);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read sql file " + sqlFile, e);
        } catch (SQLException e) {
            throw new InvalidSchemaScriptException("Error when executing " + sqlFile, e);
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

    private void storeSchemaHash(DatabaseRunner dbRunner, String schemaHash) {
        dbRunner.update("INSERT INTO DatabaseSchemaHash (hash) VALUES (?)", schemaHash);
    }

    public static class InvalidSchemaScriptException extends RuntimeException {
        public InvalidSchemaScriptException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
