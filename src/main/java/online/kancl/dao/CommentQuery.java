package online.kancl.dao;

import online.kancl.db.ListHandler;
import online.kancl.model.Comment;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommentQuery {

	public static List<Comment> loadAllComments(Connection connection) {
		try {
			return new QueryRunner().query(
					connection,
					"SELECT * FROM Comment ORDER BY id",
					ListHandler.of(CommentQuery::handle));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static long save(Connection connection, Comment comment) {
		try {
			return new QueryRunner().insert(
					connection,
					"INSERT INTO Comment(author, message) VALUES (?, ?)",
					new ScalarHandler<>(),
					comment.author, comment.message);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static Optional<Comment> handle(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			return Optional.empty();
		}

		return Optional.of(new Comment(
				rs.getLong("id"),
				rs.getString("author"),
				rs.getString("message")
		));
	}
}
