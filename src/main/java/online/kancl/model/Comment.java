package online.kancl.model;

public class Comment {

	public final Long id;
	public final String author;
	public final String message;

	public Comment(Long id, String author, String message) {
		this.id = id;
		this.author = author;
		this.message = message;
	}
}
