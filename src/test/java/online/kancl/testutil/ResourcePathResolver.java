package online.kancl.testutil;

import java.net.URL;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourcePathResolver {
	public static Path getResourcePath(Object test, String fileOrDirectoryName) {
		ClassLoader classLoader = test.getClass().getClassLoader();
		URL resource = classLoader.getResource(fileOrDirectoryName);

		assertThat(resource)
				.as("Resource " + fileOrDirectoryName + " not found")
				.isNotNull();

		return Path.of(resource.getPath());
	}
}
