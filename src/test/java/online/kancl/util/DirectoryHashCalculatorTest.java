package online.kancl.util;

import online.kancl.util.DirectoryHashCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class DirectoryHashCalculatorTest {
	private final static Path HASH_CALCULATOR_TEST_RESOURCE_DIRECTORY = Path.of("src", "test", "resources", "hashCalculator");

	private final DirectoryHashCalculator hashCalculator = new DirectoryHashCalculator();

	@Test
	void sameFileNameSameContent_sameHash() {
		Assertions.assertThat(hashForDirectory("dirWithFileAContentBar"))
				.isEqualTo(hashForDirectory("anotherDirWithFileAContentBar"));
	}

	@Test
	void sameFileNameDifferentContent_differentHash() {
		Assertions.assertThat(hashForDirectory("dirWithFileAContentBar"))
				.isNotEqualTo(hashForDirectory("dirWithFileAContentFoo"));
	}

	@Test
	void differentFileNameSameContent_differentHash() {
		Assertions.assertThat(hashForDirectory("dirWithFileAContentBar"))
				.isNotEqualTo(hashForDirectory("dirWithFileBContentBar"));
	}

	private String hashForDirectory(String directoryName) {
		Path directoryPath = HASH_CALCULATOR_TEST_RESOURCE_DIRECTORY.resolve(directoryName);
		return hashCalculator.calculateEncodedHash(directoryPath);
	}
}