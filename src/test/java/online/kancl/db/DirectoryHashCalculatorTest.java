package online.kancl.db;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static online.kancl.testutil.ResourcePathResolver.getResourcePath;

class DirectoryHashCalculatorTest {
	private final DirectoryHashCalculator hashCalculator = new DirectoryHashCalculator();

	@Test
	void sameFileNameSameContent_sameHash() {
		Assertions.assertThat(hasForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isEqualTo(hasForDirectory("hashCalculator/anotherDirWithFileAContentBar"));
	}

	@Test
	void sameFileNameDifferentContent_differentHash() {
		Assertions.assertThat(hasForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isNotEqualTo(hasForDirectory("hashCalculator/dirWithFileAContentFoo"));
	}

	@Test
	void differentFileNameSameContent_differentHash() {
		Assertions.assertThat(hasForDirectory("hashCalculator/dirWithFileAContentBar"))
				.isNotEqualTo(hasForDirectory("hashCalculator/dirWithFileBContentBar"));
	}

	private String hasForDirectory(String directoryName) {
		return hashCalculator.calculateEncodedHash(getResourcePath(this, directoryName));
	}
}