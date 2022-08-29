package online.kancl.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DirectoryHashCalculator {
	public String calculateEncodedHash(Path directory) {
		try {
			byte[] hash = calculateHashBytes(directory);
			return encodeIntoBase64(hash);
		} catch (NoSuchAlgorithmException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] calculateHashBytes(Path directory) throws NoSuchAlgorithmException, IOException {
		MessageDigest hashCalculator = MessageDigest.getInstance("SHA-1");
		Files.walkFileTree(directory, new HashFileVisitor(directory, hashCalculator));
		return hashCalculator.digest();
	}

	private String encodeIntoBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	private static class HashFileVisitor extends SimpleFileVisitor<Path> {
		private final Path startDirectory;
		private final MessageDigest hashCalculator;

		public HashFileVisitor(Path startDirectory, MessageDigest hashCalculator) {
			this.startDirectory = startDirectory;
			this.hashCalculator = hashCalculator;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String relativeFile = startDirectory.relativize(file).toString();
			hashCalculator.update(relativeFile.getBytes(StandardCharsets.UTF_8));
			hashCalculator.update(Files.readAllBytes(file));

			return FileVisitResult.CONTINUE;
		}
	}
}
