package online.kancl.page.comments;

import online.kancl.db.DatabaseRunner;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class CommentStorageTest {

    private final DatabaseRunner dbRunner;

    CommentStorageTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    @Test
    void noComments() {
        assertThat(CommentStorage.loadAllComments(dbRunner))
                .isEmpty();
    }

    @Test
    void insertAndRetrieveComment() {
        addComment("John", "message");

        assertThat(CommentStorage.loadAllComments(dbRunner))
                .containsExactly(new Comment(Optional.of(1L), "John", "message"));
    }

    @Test
    void queryMultiple() {
        addComment("John", "first");
        addComment("David", "second");

        assertThat(CommentStorage.loadAllComments(dbRunner))
                .containsExactlyInAnyOrder(
                        new Comment(Optional.of(1L), "John", "first"),
                        new Comment(Optional.of(2L), "David", "second")
                );
    }

    private void addComment(String author, String message) {
        CommentStorage.add(dbRunner, new Comment(Optional.empty(), author, message));
    }
}
