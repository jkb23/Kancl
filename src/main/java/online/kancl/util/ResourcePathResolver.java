package online.kancl.util;

import java.net.URL;
import java.nio.file.Path;

public class ResourcePathResolver {
	public static Path getResourcePath(Object test, String fileOrDirectoryName) {
		ClassLoader classLoader = test.getClass().getClassLoader();
		URL resource = classLoader.getResource(fileOrDirectoryName);

		if (resource == null)
			throw new IllegalArgumentException("Resource " + fileOrDirectoryName + " not found");

		return Path.of(resource.getPath());
	}
}
