package online.kancl.db;

import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseRunner {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public DatabaseRunner(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public <T> Optional<T> select(String sql, RowMapper<T> rowMapper, Object... binds) {
        return executeAndWrapSQLException(() ->
                queryRunner.query(
                        connection,
                        sql,
                        (row) -> mapOptionalResult(rowMapper, row),
                        binds
                )
        );
    }

    public int selectInt(String sql, Object... binds) {
        return select(sql, (r) -> r.getInt(1), binds)
                .orElseThrow(NoRowSelectedException::new);
    }

    public String selectString(String sql, Object... binds) {
        return select(sql, (r) -> r.getString(1), binds)
                .orElseThrow(NoRowSelectedException::new);
    }

    public <T> List<T> selectAll(String sql, RowMapper<T> rowMapper, Object... binds) {
        return executeAndWrapSQLException(() ->
                queryRunner.query(
                        connection,
                        sql,
                        (row) -> mapListOfRows(rowMapper, row),
                        binds
                )
        );
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @return The number of rows updated.
     */
    public int update(String sql, Object... binds) {
        return executeAndWrapSQLException(() -> queryRunner.update(connection, sql, binds));
    }

    public void insert(String sql, Object... binds) {
        insert(sql, (r) -> 1, binds);
    }

    /**
     * Executes the given INSERT SQL statement.
     *
     * @return auto-generated keys
     */
    public <T> Optional<T> insert(String sql, RowMapper<T> rowMapper, Object... binds) {
        return executeAndWrapSQLException(() ->
                queryRunner.insert(
                        connection,
                        sql,
                        (row) -> mapOptionalResult(rowMapper, row),
                        binds
                )
        );
    }

    private <T> Optional<T> mapOptionalResult(RowMapper<T> rowMapper, ResultSet row) throws SQLException {
        if (row.next())
            return Optional.of(rowMapper.map(row));
        else
            return Optional.empty();
    }

    private <T> List<T> mapListOfRows(RowMapper<T> rowMapper, ResultSet row) throws SQLException {
        List<T> results = new ArrayList<>();
        while (row.next()) {
            results.add(rowMapper.map(row));
        }
        return results;
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

    public static class NoRowSelectedException extends RuntimeException {
        public NoRowSelectedException() {
            super("No row was selected");
        }
    }
}
