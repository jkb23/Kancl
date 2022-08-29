package online.kancl.page.comments;

import java.util.Objects;

public class Comment {

	public final Long id;
	public final String author;
	public final String message;

	public Comment(Long id, String author, String message) {
		this.id = id;
		this.author = author;
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Comment comment = (Comment) o;
		return Objects.equals(id, comment.id) && author.equals(comment.author) && message.equals(comment.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, author, message);
	}
}
