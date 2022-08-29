package online.kancl.db;

import online.kancl.util.DirectoryHashCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static online.kancl.util.ResourcePathResolver.getResourcePath;

class DirectoryHashCalculatorTest {
	private final DirectoryHashCalculator hashCalculator = new DirectoryHashCalculator();

	@Test
	void sameFileNameSameContent_sameHash() {
		Assertions.assertThat(hashForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isEqualTo(hashForDirectory("hashCalculator/anotherDirWithFileAContentBar"));
	}

	@Test
	void sameFileNameDifferentContent_differentHash() {
		Assertions.assertThat(hashForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isNotEqualTo(hashForDirectory("hashCalculator/dirWithFileAContentFoo"));
	}

	@Test
	void differentFileNameSameContent_differentHash() {
		Assertions.assertThat(hashForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isNotEqualTo(hashForDirectory("hashCalculator/dirWithFileBContentBar"));
	}

	private String hashForDirectory(String directoryName) {
		return hashCalculator.calculateEncodedHash(getResourcePath(this, directoryName));
	}
}