package online.kancl.controller;

import online.kancl.dao.CommentQuery;
import online.kancl.db.TransactionJobRunner;
import online.kancl.model.Comment;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.util.List;

public class CommentsController extends Controller {

	private final PebbleTemplateRenderer pebbleTemplateRenderer;

	public CommentsController(PebbleTemplateRenderer pebbleTemplateRenderer) {
		this.pebbleTemplateRenderer = pebbleTemplateRenderer;
	}

	@Override
	public String get(Request request, Response response) {
		return TransactionJobRunner.runInTransactionAndRelease((dbRunner) -> {
			var comments = new Comments(CommentQuery.loadAllComments(dbRunner));

			return pebbleTemplateRenderer.renderTemplate("Comments.peb", comments);
		});
	}

	@Override
	public String post(Request request, Response response) {
		return TransactionJobRunner.runInTransactionAndRelease((dbRunner) -> {
			var comment = new Comment(
					null,
					request.queryParams("author"),
					request.queryParams("message"));

			CommentQuery.save(dbRunner, comment);

			response.redirect("/comments");
			return "";
		});
	}

	public static class Comments {

		public final List<Comment> comments;

		public Comments(List<Comment> comments) {
			this.comments = comments;
		}
	}
}
