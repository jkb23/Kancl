package online.kancl.controller;

import online.kancl.Main;
import online.kancl.dao.CommentQuery;
import online.kancl.model.Comment;
import online.kancl.model.Comments;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.sql.Connection;

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
		Comments comments = new Comments(CommentQuery.loadAllComments(connection));

		return pebbleTemplateRenderer.renderTemplate("Comments.peb", comments);
	}

	@Override
	public String post(Request request, Response response, Connection connection)
	{
		Comment comment = new Comment(
				null,
				request.queryParams("author"),
				request.queryParams("message"));

		CommentQuery.save(connection, comment);

		response.redirect("/comments");
		return "";
	}
}
