package online.kancl.dao;

import online.kancl.db.DatabaseRunner;
import online.kancl.model.Comment;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class CommentQueryTest {
	private final DatabaseRunner dbRunner;

	CommentQueryTest(DatabaseRunner dbRunner) {
		this.dbRunner = dbRunner;
	}

	@Test
	void noCommentsTest() {
		assertThat(CommentQuery.loadAllComments(dbRunner))
				.isEmpty();
	}

	@Test
	void insertAndRetreiveCommentTest() {
		saveComment("John", "message");

		assertThat(CommentQuery.loadAllComments(dbRunner))
				.containsExactly(new Comment(1L, "John", "message"));
	}

	@Test
	void queryMultipleTest() {
		saveComment("John", "first");
		saveComment("David", "second");

		assertThat(CommentQuery.loadAllComments(dbRunner))
				.containsExactlyInAnyOrder(
						new Comment(1L, "John", "first"),
						new Comment(2L, "David", "second")
				);
	}

	private void saveComment(String author, String message) {
		CommentQuery.save(dbRunner, new Comment(null, author, message));
	}
}