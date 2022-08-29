package online.kancl.page.comments;

public record Comment(
		Long id,
		String author,
		String message
) {
}
