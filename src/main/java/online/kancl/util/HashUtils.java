package online.kancl.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {
    private HashUtils() {
        throw new UnsupportedOperationException("JsonFileUtil is a utility class and should not be instantiated");
    }

    public static String sha256Hash(String original) {
        try {
            MessageDigest hashCalculator = MessageDigest.getInstance("SHA-256");

            return toHexString(hashCalculator.digest(original.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new HashingException("SHA-256 algorithm not found", e);
        }
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
