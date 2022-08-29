package online.kancl.page.comments;

import online.kancl.db.TransactionJobRunner;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.util.List;

public class CommentsController extends Controller {

	private final PebbleTemplateRenderer pebbleTemplateRenderer;
	private final TransactionJobRunner transactionJobRunner;

	public CommentsController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner) {
		this.pebbleTemplateRenderer = pebbleTemplateRenderer;
		this.transactionJobRunner = transactionJobRunner;
	}

	@Override
	public String get(Request request, Response response) {
		return transactionJobRunner.runInTransaction((dbRunner) -> {
			var comments = new Comments(CommentQuery.loadAllComments(dbRunner));

			return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, comments);
		});
	}

	@Override
	public String post(Request request, Response response) {
		return transactionJobRunner.runInTransaction((dbRunner) -> {
			var comment = new Comment(
					null,
					request.queryParams("author"),
					request.queryParams("message"));

			CommentQuery.save(dbRunner, comment);

			response.redirect("/comments");
			return "";
		});
	}

	public record Comments(
			List<Comment> comments
	) {
	}
}
