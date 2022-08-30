package online.kancl.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a single row from result set into T.
 */
public interface RowMapper<T> {

	T map(ResultSet rs) throws SQLException;
}
