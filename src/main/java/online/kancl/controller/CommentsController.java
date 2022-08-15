package online.kancl.controller;

import online.kancl.Main;
import online.kancl.db.ListHandler;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommentsController extends Controller
{
	private final PebbleTemplateRenderer pebbleTemplateRenderer;

	public CommentsController(PebbleTemplateRenderer pebbleTemplateRenderer)
	{
		this.pebbleTemplateRenderer = pebbleTemplateRenderer;
	}

	@Override
	public String get(Request request, Response response) throws Exception
	{
		Connection connection = Main.getConnection();
		Comments comments = new Comments();
		comments.comments = loadAllComments(connection);

		return pebbleTemplateRenderer.renderTemplate("Comments.peb", comments);
	}

	@Override
	public String post(Request request, Response response) throws Exception
	{
		Comment comment = new Comment(
				null,
				request.queryParams("author"),
				request.queryParams("message"));

		Connection connection = Main.getConnection();
		save(connection, comment);

		response.redirect("/comments");
		return "";
	}

	private static class Comments
	{
		public List<Comment> comments;
	}

	private static class Comment
	{
		public final Long id;
		public final String author;
		public final String message;

		public Comment(Long id, String author, String message)
		{
			this.id = id;
			this.author = author;
			this.message = message;
		}
	}

	private List<Comment> loadAllComments(Connection connection)
	{
		try
		{
			return new QueryRunner().query(
					connection,
					"SELECT * FROM Comment ORDER BY id",
					ListHandler.of(CommentsController::handle));
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private long save(Connection connection, Comment comment)
	{
		try
		{
			return new QueryRunner().insert(
					connection,
					"INSERT INTO Comment(author, message) VALUES (?, ?)",
					new ScalarHandler<>(),
					comment.author, comment.message);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static Optional<Comment> handle(ResultSet rs) throws SQLException
	{
		if (!rs.next())
		{
			return Optional.empty();
		}

		return Optional.of(new Comment(
				rs.getLong("id"),
				rs.getString("author"),
				rs.getString("message")
		));
	}
}
