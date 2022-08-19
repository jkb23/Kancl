package online.kancl.dao;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.ListHandler;
import online.kancl.model.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommentQuery {

	public static List<Comment> loadAllComments(DatabaseRunner dbRunner) {
		return dbRunner.query(
				"SELECT * FROM Comment ORDER BY id",
				ListHandler.of(CommentQuery::handle));
	}

	public static void save(DatabaseRunner dbRunner, Comment comment) {
		dbRunner.update(
				"INSERT INTO Comment(author, message) VALUES (?, ?)",
				comment.author, comment.message);
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
