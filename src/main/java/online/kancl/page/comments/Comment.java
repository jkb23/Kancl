package online.kancl.page.comments;

import java.util.Optional;

public record Comment(
		Optional<Long> id,
		String author,
		String message
) {
}
