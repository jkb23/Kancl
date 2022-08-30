package online.kancl.dao;

import online.kancl.page.comments.CommentQuery;
import online.kancl.db.DatabaseRunner;
import online.kancl.page.comments.Comment;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class CommentQueryTest {

	private final DatabaseRunner dbRunner;

	CommentQueryTest(DatabaseRunner dbRunner) {
		this.dbRunner = dbRunner;
	}

	@Test
	void noComments() {
		assertThat(CommentQuery.loadAllComments(dbRunner))
				.isEmpty();
	}

	@Test
	void insertAndRetrieveComment() {
		addComment("John", "message");

		assertThat(CommentQuery.loadAllComments(dbRunner))
				.containsExactly(new Comment(Optional.of(1L), "John", "message"));
	}

	@Test
	void queryMultiple() {
		addComment("John", "first");
		addComment("David", "second");

		assertThat(CommentQuery.loadAllComments(dbRunner))
				.containsExactlyInAnyOrder(
						new Comment(Optional.of(1L), "John", "first"),
						new Comment(Optional.of(2L), "David", "second")
				);
	}

	private void addComment(String author, String message) {
		CommentQuery.add(dbRunner, new Comment(Optional.empty(), author, message));
	}
}
