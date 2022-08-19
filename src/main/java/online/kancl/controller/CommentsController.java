package online.kancl.controller;

import online.kancl.dao.CommentQuery;
import online.kancl.model.Comment;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.util.List;

public class CommentsController extends Controller
{
	private final PebbleTemplateRenderer pebbleTemplateRenderer;

	public CommentsController(PebbleTemplateRenderer pebbleTemplateRenderer)
	{
		this.pebbleTemplateRenderer = pebbleTemplateRenderer;
	}

	@Override
	public String get(Request request, Response response, Connection connection)
	{
		var comments = new Comments(CommentQuery.loadAllComments(connection));

		return pebbleTemplateRenderer.renderTemplate("Comments.peb", comments);
	}

	@Override
	public String post(Request request, Response response, Connection connection)
	{
		var comment = new Comment(
				null,
				request.queryParams("author"),
				request.queryParams("message"));

		CommentQuery.save(connection, comment);

		response.redirect("/comments");
		return "";
	}

	public static class Comments
	{
		public final List<Comment> comments;

		public Comments(List<Comment> comments)
		{
			this.comments = comments;
		}
	}
}
